package io.github.driveindex.azure.exception;

/**
 * @author sgpublic
 * @Date 2022/8/16 15:51
 */
public class PasswordTooLongException extends RuntimeException {
    public PasswordTooLongException() {
        super("密码太长，请不要将密码设置为超过 " + PASSWORD_LENGTH + " 个字符！");
    }

    public static final int PASSWORD_LENGTH = 64;
}
