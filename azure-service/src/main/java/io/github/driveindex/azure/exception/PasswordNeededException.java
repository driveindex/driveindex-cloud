package io.github.driveindex.azure.exception;

/**
 * @author sgpublic
 * @Date 2022/8/16 15:51
 */
public class PasswordNeededException extends SecurityException {
    public PasswordNeededException(String path) {
        super("此目录需要密码：" + path);
    }
}
