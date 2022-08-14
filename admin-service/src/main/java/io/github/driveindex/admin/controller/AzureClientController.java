package io.github.driveindex.admin.controller;

import io.github.driveindex.admin.h2.dao.AzureClientDao;
import io.github.driveindex.admin.module.AzureClientModule;
import io.github.driveindex.common.dto.azure.drive.AzureClientDetailDto;
import io.github.driveindex.common.dto.result.FailedResult;
import io.github.driveindex.common.dto.result.ResponseData;
import io.github.driveindex.common.dto.result.SuccessResult;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * @author sgpublic
 * @Date 2022/8/7 16:55
 */
@RequiredArgsConstructor
@RestController
public class AzureClientController {
    private final AzureClientModule clientModule;

    @GetMapping("/api/admin/azure_client")
    public ResponseData getAll() {
        return SuccessResult.of(clientModule.getAll());
    }

    @GetMapping("/api/admin/azure_client/{id}")
    public ResponseData get(
            @PathVariable String id
    ) {
        AzureClientDao dao = clientModule.getById(id);
        return (dao != null) ? SuccessResult.of(dao) : FailedResult.NOT_FOUND;
    }

    @PostMapping("/api/admin/azure_client/{id}")
    public ResponseData save(
            @PathVariable String id,
            @RequestBody AzureClientDetailDto dto
    ) {
        boolean save = clientModule.save(id, dto);
        return save ? SuccessResult.SAMPLE : FailedResult.NOT_FOUND;
    }

    @PostMapping("/api/admin/azure_client/delete/{aClient}")
    public ResponseData delete(@PathVariable String aClient) {
        clientModule.delete(aClient);
        return SuccessResult.SAMPLE;
    }

    @PostMapping("/api/admin/azure_client/default/{aClient}")
    public ResponseData setDefault(@PathVariable String aClient) {
        boolean setDefault = clientModule.setDefault(aClient);
        return setDefault ? SuccessResult.SAMPLE : FailedResult.NOT_FOUND;
    }
}
