package io.github.driveindex.admin.controller;

import io.github.driveindex.common.dto.admin.PasswordDto;
import io.github.driveindex.common.dto.result.FailedResult;
import io.github.driveindex.common.dto.result.ResponseData;
import io.github.driveindex.common.dto.result.SuccessResult;
import io.github.driveindex.common.manager.ConfigManager;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@Tag(name = "密码设置")
public class PasswordConfigController {
    private static final FailedResult PASSWORD_NOT_MATCHED = new FailedResult(-1001, "密码错误");
    private static final FailedResult PASSWORD_NOT_REPEAT = new FailedResult(-1002, "两次密码不匹配");
    private static final FailedResult PASSWORD_CHANGE_FAILED = new FailedResult(-1003, "密码修改失败，请查看后台日志");

    @Operation(summary = "密码修改", description = "修改管理员密码")
    @PostMapping("/api/admin/password")
    public ResponseData setPassword(@RequestBody PasswordDto dto) {
        if (!dto.getNewPass().equals(dto.getRepeatPass())) {
            return PASSWORD_NOT_REPEAT;
        }
        if (!dto.getOldPass().equals(ConfigManager.Password.getValue())) {
            return PASSWORD_NOT_MATCHED;
        }
        try {
            ConfigManager.Password.setValue(dto.getNewPass());
            return SuccessResult.SAMPLE;
        } catch (Exception e) {
            log.warn("密码修改失败", e);
            return PASSWORD_CHANGE_FAILED;
        }
    }
}
