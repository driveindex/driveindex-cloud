package io.github.driveindex.azure.controller;

import io.github.driveindex.azure.exception.PasswordNeededException;
import io.github.driveindex.azure.module.FileModule;
import io.github.driveindex.azure.h2.dao.CacheCentralEntity;
import io.github.driveindex.common.dto.azure.file.AzureContentDto;
import io.github.driveindex.common.dto.result.FailedResult;
import io.github.driveindex.common.dto.result.ResponseData;
import io.github.driveindex.common.dto.result.SuccessResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
@RequiredArgsConstructor
@RestController
public class FileController {
    private final FileModule fileModule;

    @GetMapping("/api/azure/file")
    public ResponseData listFile(
            @RequestParam(required = false) String client,
            @RequestParam(required = false) String account,
            @RequestParam(required = false) String drive,
            @RequestParam String path,
            @RequestParam(required = false) String password,
            @RequestParam(name = "sort_by", required = false) CacheCentralEntity.Sort sortBy,
            @RequestParam(name = "asc", required = false) Boolean asc,
            @RequestParam(name = "page_size", required = false) Long pageSize,
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
            return FailedResult.NOT_FOUND;
        }
    }

    @GetMapping("/download")
    public void download(
            @RequestParam(required = false) String client,
            @RequestParam(required = false) String account,
            @RequestParam(required = false) String drive,
            @RequestParam String path,
            @RequestParam(required = false) String password,
            @RequestParam(required = false) Boolean direct,
            HttpServletResponse response
    ) throws IOException {
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        try (
                PrintWriter writer = response.getWriter()
        ) {
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
        } catch (IOException | ParseException | PasswordNeededException e) {
            log.warn("文件获取失败", e);
            try (
                    PrintWriter writer = response.getWriter()
            ) {
                writer.write((e instanceof PasswordNeededException
                        ? FailedResult.WRONG_PASSWORD
                        : new FailedResult(-4001, e.getMessage())).toString());
            }
        }
    }
}
