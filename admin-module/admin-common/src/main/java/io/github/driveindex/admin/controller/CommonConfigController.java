package io.github.driveindex.admin.controller;

import io.github.driveindex.common.dto.result.ResponseData;
import io.swagger.v3.oas.annotations.tags.Tag;
import kotlin.NotImplementedError;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "常规设置")
@Slf4j
@RequiredArgsConstructor
@RestController
public class CommonConfigController {
    @PostMapping("/api/admin/common")
    public ResponseData saveConfig() {
        throw new NotImplementedError();
    }

    @PostMapping("/api/common/language")
    public ResponseData getLanguage() {
        throw new NotImplementedError();
    }
}
