package io.github.driveindex.azure.util;

import org.springframework.lang.Nullable;
import org.springframework.security.crypto.codec.Hex;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author sgpublic
 * @Date 2022/8/28 16:14
 */
public class Sha1Util {
    private final static MessageDigest messageDigest;

    static {
        try {
            messageDigest = MessageDigest.getInstance("SHA");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    @Nullable
    public static String convert(@Nullable String str) {
        if (str == null) return null;
        return new String(Hex.encode(messageDigest.digest(
                str.getBytes(StandardCharsets.UTF_8)
        )));
    }
}
