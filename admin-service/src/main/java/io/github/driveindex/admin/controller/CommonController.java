package io.github.driveindex.admin.controller;

import io.github.driveindex.common.dto.admin.CommonSettingsDto;
import io.github.driveindex.common.dto.result.ResponseData;
import io.github.driveindex.common.dto.result.SuccessResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author sgpublic
 * @Date 2022/8/3 12:50
 */
@RestController("/api/admin")
public class CommonController {
    @GetMapping("/common")
    public ResponseData getCommonSettings() {
        return SuccessResult.of(new CommonSettingsDto());
    }

    @PostMapping("/common")
    public ResponseData setCommonSettings(@RequestParam CommonSettingsDto settings) {
        return SuccessResult.SAMPLE;
    }
}
