package io.github.driveindex.azure.controller;

import io.github.driveindex.azure.h2.dao.AzureClientDao;
import io.github.driveindex.azure.module.AzureClientModule;
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
@RestController("/api/admin/azure_client")
public class AzureClientController {
    private final AzureClientModule clientModule;

    @GetMapping("/")
    public ResponseData getAll() {
        return SuccessResult.of(clientModule.getAll());
    }

    @GetMapping("/{id}")
    public ResponseData get(
            @PathVariable String id
    ) {
        AzureClientDao dao = clientModule.getById(id);
        return (dao != null) ? SuccessResult.of(dao) : FailedResult.NOT_FOUND;
    }

    @PostMapping("/{id}")
    public ResponseData save(
            @PathVariable String id,
            @RequestBody AzureClientDetailDto dto
    ) {
        boolean save = clientModule.save(id, dto);
        return save ? SuccessResult.SAMPLE : FailedResult.NOT_FOUND;
    }

    @PostMapping("/enabled/{id}")
    private ResponseData setEnable(@PathVariable String id, Boolean enabled) {
        boolean setEnabled = clientModule.setEnabled(id, enabled);
        return setEnabled ? SuccessResult.SAMPLE : FailedResult.NOT_FOUND;
    }

    @PostMapping("/delete/{id}")
    public ResponseData delete(@PathVariable String id) {
        clientModule.delete(id);
        return SuccessResult.SAMPLE;
    }
}
