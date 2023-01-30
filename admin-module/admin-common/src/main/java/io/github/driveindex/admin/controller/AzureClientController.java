package io.github.driveindex.admin.controller;

import io.github.driveindex.admin.h2.dao.AzureClientDao;
import io.github.driveindex.admin.module.AzureClientModule;
import io.github.driveindex.common.dto.azure.drive.AzureClientDetailDto;
import io.github.driveindex.common.dto.azure.drive.AzureClientDto;
import io.github.driveindex.common.dto.result.FailedResult;
import io.github.driveindex.common.dto.result.ResponseData;
import io.github.driveindex.common.dto.result.SuccessResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * @author sgpublic
 * @Date 2022/8/7 16:55
 */
@Tag(name = "Client 配置")
@RequiredArgsConstructor
@RestController
public class AzureClientController {
    private final AzureClientModule clientModule;

    public static class AzureClientsDtoWrapper extends SuccessResult<LinkedList<AzureClientDto>> {
        protected AzureClientsDtoWrapper(LinkedList<AzureClientDto> data) {
            super(data);
        }
    }
    @Operation(
            summary = "枚举 Client 配置", description = "获取所有 Client 配置",
            responses = @ApiResponse(content = @Content(schema = @Schema(
                    implementation = AzureClientsDtoWrapper.class
            )))
    )
    @GetMapping("/api/admin/azure_client")
    public ResponseData getAll() {
        return SuccessResult.of(clientModule.getAll());
    }

    public static class AzureClientDtoWrapper extends SuccessResult<AzureClientDao> {
        protected AzureClientDtoWrapper(AzureClientDao data) {
            super(data);
        }
    }
    @Operation(
            summary = "获取指定 Client 信息", description = "指定 ID 获取 Client 配置",
            responses = @ApiResponse(content = @Content(schema = @Schema(
                    implementation = AzureClientDtoWrapper.class
            )))
    )
    @GetMapping("/api/admin/azure_client/{id}")
    public ResponseData get(
            @PathVariable String id
    ) {
        AzureClientDao dao = clientModule.getById(id);
        return (dao != null) ? SuccessResult.of(dao) : FailedResult.NOT_FOUND;
    }

    @Operation(summary = "编辑/新增 Client 配置", description = "指定 ID 修改 Client 配置，ID 不存在则新增")
    @PostMapping("/api/admin/azure_client/{id}")
    public ResponseData save(
            @PathVariable String id,
            @RequestBody AzureClientDetailDto dto
    ) {
        boolean save = clientModule.save(id, dto);
        return save ? SuccessResult.SAMPLE : FailedResult.NOT_FOUND;
    }

    @Operation(summary = "删除 Client 配置", description = "指定 ID 删除 Client 配置，忽略不存在的 ID")
    @PostMapping("/api/admin/azure_client/delete/{aClient}")
    public ResponseData delete(@PathVariable String aClient) {
        clientModule.delete(aClient);
        return SuccessResult.SAMPLE;
    }
}
