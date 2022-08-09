package io.github.driveindex.admin.exception;

import org.springframework.security.authentication.BadCredentialsException;

/**
 * @author sgpublic
 * @Date 2022/8/6 13:53
 */
public class WrongPasswordException extends BadCredentialsException {
    private static final String message = "密码错误";

    public WrongPasswordException() {
        super(message);
    }

    public WrongPasswordException(String msg, Throwable cause) {
        super(message, cause);
    }
}
