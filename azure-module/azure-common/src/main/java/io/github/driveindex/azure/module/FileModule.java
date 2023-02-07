package io.github.driveindex.azure.module;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.github.driveindex.azure.core.SpecialFile;
import io.github.driveindex.azure.exception.PasswordNeededException;
import io.github.driveindex.azure.exception.PasswordTooLongException;
import io.github.driveindex.azure.feign.AzureClient;
import io.github.driveindex.azure.h2.dao.AzureFileEntity;
import io.github.driveindex.azure.h2.dao.ContentCacheEntity;
import io.github.driveindex.azure.h2.dao.DirCacheEntity;
import io.github.driveindex.azure.h2.dao.FileCacheEntity;
import io.github.driveindex.azure.h2.service.AzureContentCacheService;
import io.github.driveindex.azure.h2.service.AzureDirCacheService;
import io.github.driveindex.azure.h2.service.AzureFileCacheService;
import io.github.driveindex.azure.h2.service.AzureFileService;
import io.github.driveindex.azure.util.PageUtil;
import io.github.driveindex.azure.util.Sha1Util;
import io.github.driveindex.common.dto.azure.drive.AzureDriveDto;
import io.github.driveindex.common.dto.azure.file.*;
import io.github.driveindex.common.exception.AzureDecodeException;
import io.github.driveindex.common.util.CanonicalPath;
import io.github.driveindex.common.util.GsonUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

/**
 * @author sgpublic
 * @Date 2022/8/15 11:12
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class FileModule {
    private final DriveConfigModule configModule;
    private final AzureClient azureClient;
    private final AzureFileService cacheCentralService;
    private final AzureFileCacheService fileCacheService;
    private final AzureDirCacheService dirCacheService;
    private final AzureContentCacheService contentCacheService;

    @Nullable
    public AzureContentDto<?> getFile(
            @NonNull String client, @NonNull String account,
            @Nullable String drive, @NonNull String path,
            @Nullable String password,
            final @Nullable AzureFileEntity.Sort sortBy,
            final @Nullable Boolean asc,
            @Nullable Long pageSize, @Nullable Long pageIndex
    ) throws AzureDecodeException, PasswordNeededException, IOException, ParseException {
        AzureDriveDto config = configModule.getConfig(client, account, drive);
        if (config == null) return null;
        if (path.endsWith(SpecialFile._PASSWORD.toString()))
            throw new FileNotFoundException("密码文件不允许下载！");

        CanonicalPath fillPath = checkPassword(
                CanonicalPath.of(config.getDirHome()), CanonicalPath.of(path),
                config.getToken().toString(), password
        );

        final AzureFileEntity item = getItem(fillPath, config.getToken().toString());
        if (item == null) throw new FileNotFoundException("文件不存在：\"" + fillPath.getPath() + "\"");
        AzureContentDto<?> dto;
        if (!item.getIsDir()) {
            FileItemDto itemDto = new FileItemDto();
            itemDto.setMineType(item.getMineType());
            itemDto.setDetail(fileCacheService.getById(item.getId()));
            itemDto.setInfo(item.clone());
            FileContentDto file = new FileContentDto();
            file.setContent(itemDto);
            dto = file;
        } else {
            Page<AzureFileEntity> list = getList(
                    fillPath, config.getToken().toString(),
                    PageUtil.createPage(pageSize, pageIndex),
                    sortBy != null ? sortBy : AzureFileEntity.Sort.NAME,
                    asc == null || asc
            );
            if (list == null) throw new FileNotFoundException("文件不存在");

            DirContentDto.FileList detail = new DirContentDto.FileList();
            for (AzureFileEntity index : list.getRecords()) {
                // 从目录中排除 .password 文件
                if (SpecialFile._PASSWORD.match(index.getName())) continue;
                FileItemDto itemDto = new FileItemDto();
                itemDto.setName(index.getName());
                itemDto.setMineType(index.getMineType());
                itemDto.setDetail(index.getIsDir()
                        ? dirCacheService.getById(index.getId())
                        : fileCacheService.getById(index.getId()));
                itemDto.setInfo(index.clone());
                detail.add(itemDto);
            }
            DirContentDto dir = new DirContentDto();
            dir.setContent(detail);
            dir.setTotalPage(list.getPages());
            dir.setTotalCount(list.getTotal());
            dto = dir;
        }
        dto.setName(item.getName());
        dto.setMineType(item.getMineType());
        dto.setCalledName(config.getCalledName());
        return dto;
    }

    @Nullable
    public String getItemUrl(
            @Nullable String client, @Nullable String account,
            @Nullable String drive, @NonNull String path,
            @Nullable String password
    ) throws IOException, ParseException {
        AzureDriveDto config = configModule.getConfig(client, account, drive);
        if (config == null) return null;
        CanonicalPath fillPath = checkPassword(
                CanonicalPath.of(config.getDirHome()), CanonicalPath.of(path),
                config.getToken().toString(), password
        );
        AzureFileEntity item = getItem(fillPath, config.getToken().toString());
        if (item == null || item.getIsDir()) return null;
        return fileCacheService.getDownloadUrlById(item.getId());
    }

    @Nullable
    public AzureFileEntity getItem(@NonNull CanonicalPath fillPath, @NonNull String token)
            throws PasswordNeededException {
        return cacheCentralService.getByPath(fillPath);
    }

    private Page<AzureFileEntity> getList(
            CanonicalPath fillPath, String token,
            Page<AzureFileEntity> page,
            @NonNull AzureFileEntity.Sort sort,
            @NonNull Boolean asc
    ) throws PasswordNeededException, FileNotFoundException {
        AzureFileEntity currentPath = getItem(fillPath, token);
        if (currentPath == null) return null;
        if (!currentPath.getIsDir()) throw new FileNotFoundException("不是一个文件夹");
        return cacheCentralService.pageByParentId(currentPath.getId(), page, sort, asc);
    }

    @Deprecated
    private void refreshList(CanonicalPath fillPath, String token)
            throws PasswordNeededException {
        Map<String, Object> json = azureClient.listFile(
                fillPath.toAzureCanonicalizePath(), token
        );
        //noinspection unchecked
        List<Map<String, Object>> value = (List<Map<String, Object>>) json.get("value");
        for (Map<String, Object> index : value) {
            parseItem(index, fillPath);
        }
    }

    @Deprecated
    private void parseItem(
            Map<String, Object> index,
            @Nullable CanonicalPath fillPath
    ) {
        if (!index.containsKey("folder") && !index.containsKey("file")) return;
        log.trace(GsonUtil.fromMap(index));
        AzureFileEntity item = new AzureFileEntity();
        item.setName((String) index.get("name"));
        item.setId((String) index.get("id"));
        item.setSize(((Double) index.get("size")).longValue());
        item.setWebUrl((String) index.get("webUrl"));
        item.setCanonicalPath((fillPath == null)
                ? CanonicalPath.ROOT_PATH
                : fillPath.append(item.getName()).getPath());
        item.setCanonicalPathHash(item.getCanonicalPathHash());
        //noinspection unchecked
        Map<String, Object> parentReference = (Map<String, Object>) index.get("parentReference");
        item.setParentId((String) parentReference.get("id"));
        //noinspection unchecked
        Map<String, Object> fileSystemInfo = (Map<String, Object>) index.get("fileSystemInfo");
        item.setCreateTime((String) fileSystemInfo.get("createdDateTime"));
        item.setModifiedTime((String) fileSystemInfo.get("lastModifiedDateTime"));
        if (index.containsKey("folder")) {
            //noinspection unchecked
            Map<String, Object> folder = (Map<String, Object>) index.get("folder");
            item.setMineType(AzureFileEntity.MEDIA_TYPE_DIR);
            item.setIsDir(true);
            DirCacheEntity entity = new DirCacheEntity();
            entity.setId(item.getId());
            entity.setChildCount(((Double) folder.get("childCount")).intValue());
            cacheCentralService.saveOrUpdate(item);
            dirCacheService.saveOrUpdate(entity);
        } else if (index.containsKey("file")) {
            //noinspection unchecked
            Map<String, Object> file = (Map<String, Object>) index.get("file");
            item.setMineType((String) file.get("mimeType"));
            item.setIsDir(false);
            FileCacheEntity entity = new FileCacheEntity();
            entity.setId(item.getId());
            entity.setDownloadUrl((String) index.get("@microsoft.graph.downloadUrl"));
            //noinspection unchecked
            Map<String, Object> hashes = (Map<String, Object>) file.get("hashes");
            entity.setQuickXorHash((String) hashes.get("quickXorHash"));
            entity.setSha1Hash((String) hashes.get("sha1Hash"));
            entity.setSha256Hash((String) hashes.get("sha256Hash"));
            cacheCentralService.saveOrUpdate(item);
            fileCacheService.saveOrUpdate(entity);
        }
    }

    private CanonicalPath checkPassword(
            @NonNull CanonicalPath dirHome,
            @NonNull CanonicalPath currentPath,
            String token, String password
    ) throws IOException {
        String passSet = Sha1Util.convert(
                findPassword(dirHome, currentPath, token)
        );
        if (passSet != null && !passSet.equals(password)) {
            log.debug("密码不匹配，\n密码路径：" + currentPath.getPath()
                    + "\n  本地：" + passSet + "，提交：" + password);
            throw new PasswordNeededException(currentPath.getPath());
        }
        return dirHome.append(currentPath);
    }

    private final RestTemplate rest;
    private String findPassword(
            @NonNull CanonicalPath dirHome,
            @NonNull CanonicalPath currentPath,
            String token
    ) throws AzureDecodeException, IOException {
        CanonicalPath _password = currentPath.append(SpecialFile._PASSWORD.toString());
        AzureFileEntity item = getItem(dirHome.append(_password), token);

        if (item == null || item.getIsDir() && currentPath.hasParent()) {
            return findPassword(dirHome, currentPath.getParentPath(), token);
        }

        String downloadUrl = fileCacheService.getDownloadUrlById(item.getId());
        Resource resp = rest.getForEntity(downloadUrl, Resource.class).getBody();
        if (resp == null) {
            throw new IOException("failed to read body of \".password\".");
        }
        return getItemContent(item.getId(), item.getName(), PasswordTooLongException.PASSWORD_LENGTH);
    }

    private String getItemContent(String id, String name, Integer lengthLimit) throws IOException {
        String contentById = contentCacheService.getContentById(id);
        return contentById != null ? contentById : refreshItemContent(id, name, lengthLimit);
    }

    private String refreshItemContent(String id, String name, Integer lengthLimit) throws IOException {
        String downloadUrl = fileCacheService.getDownloadUrlById(id);
        Resource resp = rest.getForEntity(downloadUrl, Resource.class).getBody();
        if (resp == null) {
            throw new IOException("failed to read body of \"" + name + "\".");
        }
        try (
                InputStream inputStream = resp.getInputStream();
                Reader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8)
        ) {
            char[] buffer = new char[1024];
            int read = reader.read(buffer);
            StringBuilder builder = new StringBuilder();
            if (lengthLimit > 0 && read > lengthLimit) {
                throw new PasswordTooLongException();
            }
            do {
                builder.append(buffer, 0, read);
            } while ((read = reader.read(buffer)) > 0);

            ContentCacheEntity entity = new ContentCacheEntity();
            entity.setContent(builder.toString().trim());
            entity.setType(name);
            entity.setId(id);
            contentCacheService.save(entity);
            return entity.getContent();
        }
    }
}
