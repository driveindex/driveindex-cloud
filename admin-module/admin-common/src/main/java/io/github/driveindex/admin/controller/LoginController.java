package io.github.driveindex.admin.controller;

import io.github.driveindex.common.dto.admin.AccessTokenDto;
import io.github.driveindex.common.dto.admin.LoginDto;
import io.github.driveindex.common.dto.admin.PasswordDto;
import io.github.driveindex.common.dto.result.FailedResult;
import io.github.driveindex.common.dto.result.ResponseData;
import io.github.driveindex.common.dto.result.SuccessResult;
import io.github.driveindex.common.manager.ConfigManager;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author sgpublic
 * @Date 2022/8/3 19:39
 */
@Slf4j
@RestController
@Tag(name = "管理员登陆")
public class LoginController {
    /**
     * 利用 SpringSecurity 检查登录是否有效
     * @return 若登录有效直接返回 code:200
     */
    @Operation(
            summary = "token 有效性检查",
            description = "检查 token 是否有效",
            security = { @SecurityRequirement(name = "driveindex-authentication") }
    )
    @GetMapping("/api/admin/token_state")
    public ResponseData checkToken() {
        return SuccessResult.SAMPLE;
    }

    @Operation(summary = "管理员登陆", description = "使用密码登陆")
    @PostMapping("/api/login")
    public SuccessResult<AccessTokenDto> login(
            @RequestBody LoginDto dto
    ) {
        throw new BadCredentialsException("use authentication provider instead.");
    }

    private static final FailedResult PASSWORD_NOT_MATCHED = new FailedResult(-1001, "密码错误");
    private static final FailedResult PASSWORD_NOT_REPEAT = new FailedResult(-1002, "两次密码不匹配");
    private static final FailedResult PASSWORD_CHANGE_FAILED = new FailedResult(-1003, "密码修改失败，请查看后台日志");

    @Operation(summary = "密码修改", description = "修改管理员密码")
    @PostMapping("/api/admin/password")
    public ResponseData setPassword(@RequestBody PasswordDto dto) {
        if (!dto.getNewPass().equals(dto.getRepeatPass())) {
            return PASSWORD_NOT_REPEAT;
        }
        if (!dto.getOldPass().equals(ConfigManager.getAdminPassword())) {
            return PASSWORD_NOT_MATCHED;
        }
        try {
            ConfigManager.setAdminPassword(dto.getNewPass());
            return SuccessResult.SAMPLE;
        } catch (Exception e) {
            log.warn("密码修改失败", e);
            return PASSWORD_CHANGE_FAILED;
        }
    }
}
