package io.github.driveindex.admin.controller;

import io.github.driveindex.common.dto.admin.AccessTokenDto;
import io.github.driveindex.common.dto.result.ResponseData;
import io.github.driveindex.common.dto.result.SuccessResult;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author sgpublic
 * @Date 2022/8/3 19:39
 */
@RestController("/api")
public class LoginController {
    /**
     * 利用 SpringSecurity 检查登录是否有效
     * @return 若登录有效直接返回 code:200
     */
    @PreAuthorize("hasRole(\"ADMIN\")")
    @PostMapping("/admin/token")
    public ResponseData checkToken() {
        return SuccessResult.SAMPLE;
    }

    @PostMapping("/login")
    public SuccessResult<AccessTokenDto> login(String password) {
        throw new BadCredentialsException("use authentication provider instead.");
    }
}
