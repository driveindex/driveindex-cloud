package io.github.driveindex.azure.controller;

import io.github.driveindex.azure.exception.PasswordNeededException;
import io.github.driveindex.azure.module.FileModule;
import io.github.driveindex.azure.h2.dao.CacheCentralEntity;
import io.github.driveindex.common.dto.azure.file.AzureContentDto;
import io.github.driveindex.common.dto.azure.file.DirContentDto;
import io.github.driveindex.common.dto.azure.file.FileContentDto;
import io.github.driveindex.common.dto.result.FailedResult;
import io.github.driveindex.common.dto.result.ResponseData;
import io.github.driveindex.common.dto.result.SuccessResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;

/**
 * @author sgpublic
 * @Date 2022/8/15 9:32
 */
@Tag(name = "文件操作")
@Slf4j
@RequiredArgsConstructor
@RestController
public class FileController {
    private final FileModule fileModule;

    public static final FailedResult PASSWORD_NEEDED = new FailedResult(-4001, "文件被加密，请提交密码");

    public static class AzureContentDtoWrapper extends SuccessResult<AzureContentDto<?>> {
        protected AzureContentDtoWrapper(DirContentDto data) {
            super(data);
        }
    }
    @Operation(
            summary = "枚举文件", description = "获取目标目录中所有文件",
            responses = @ApiResponse(content = @Content(schema = @Schema(
                    implementation = AzureContentDtoWrapper.class
            )))
    )
    @GetMapping("/api/azure/file")
    public ResponseData listFile(
            @Schema(description = "目标 Client 配置 ID")
            @RequestParam(required = false) String client,
            @Schema(description = "目标微软账号 ID")
            @RequestParam(required = false) String account,
            @Schema(description = "目标目录配置 ID")
            @RequestParam(required = false) String drive,
            @Schema(description = "目录")
            @RequestParam String path,
            @Schema(description = "若目标目录需要密码，则附带此参数，明文传输即可")
            @RequestParam(required = false) String password,
            @Schema(description = "排序规则", allowableValues = { "name", "size", "create_time", "modified_time" }, defaultValue = "name")
            @RequestParam(name = "sort_by", required = false) CacheCentralEntity.Sort sortBy,
            @Schema(description = "是否升序", defaultValue = "true")
            @RequestParam(name = "asc", required = false) Boolean asc,
            @Schema(description = "分页大小", defaultValue = "15")
            @RequestParam(name = "page_size", required = false) Long pageSize,
            @Schema(description = "页索引", defaultValue = "0")
            @RequestParam(name = "page_index", required = false) Long pageIndex
    ) {
        try {
            AzureContentDto<?> file = fileModule.getFile(
                    client, account, drive, path, password,
                    sortBy, asc, pageSize, pageIndex
            );
            if (file == null) return FailedResult.NOT_FOUND;
            else return SuccessResult.of(file);
        } catch (IOException | ParseException e) {
            log.warn("文件获取失败", e);
            return new FailedResult(-4001, e.getMessage());
        } catch (PasswordNeededException e) {
            return PASSWORD_NEEDED;
        }
    }

    public static class DownloadWrapper extends SuccessResult<String> {
        protected DownloadWrapper(String data) {
            super(data);
        }

        @Schema(example = "https://...")
        @Override
        public String getData() {
            return super.getData();
        }
    }
    @Operation(
            summary = "文件下载", description = "获取文件下载直链",
            responses = @ApiResponse(content = @Content(schema = @Schema(
                    implementation = DownloadWrapper.class
            )))
    )
    @GetMapping("/download")
    public void download(
            @Schema(description = "目标 Client 配置 ID")
            @RequestParam(required = false) String client,
            @Schema(description = "目标微软账号 ID")
            @RequestParam(required = false) String account,
            @Schema(description = "目标目录配置 ID")
            @RequestParam(required = false) String drive,
            @Schema(description = "目录")
            @RequestParam String path,
            @Schema(description = "若目标目录需要密码，则附带此参数，明文传输即可")
            @RequestParam(required = false) String password,
            @Schema(description = "是否直接跳转，前端获取下载直链时请传入 false", defaultValue = "true")
            @RequestParam(required = false) Boolean direct,
            HttpServletResponse response
    ) throws IOException {
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        PrintWriter writer = response.getWriter();
        try {
            String itemUrl = fileModule.getItemUrl(client, account, drive, path, password);
            if (itemUrl == null) {
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                writer.write(FailedResult.NOT_FOUND.toString());
                return;
            }
            if (direct == null || direct) {
                response.sendRedirect(itemUrl);
                return;
            }
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            writer.write(SuccessResult.of(itemUrl).toString());
        } catch (IOException | ParseException e) {
            log.warn("文件获取失败", e);
            writer.write(new FailedResult(-4002, e.getMessage()).toString());
        } catch (PasswordNeededException e) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            writer.write(PASSWORD_NEEDED.toString());
        } finally {
            writer.close();
        }
    }
}
