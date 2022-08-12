package io.github.driveindex.common.util;

import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author sgpublic
 * @Date 2022/8/12 18:39
 */
@Slf4j
public class MD5Util {
    private static MessageDigest getInstance() throws NoSuchAlgorithmException {
        return MessageDigest.getInstance("MD5");
    }

    public static String standard(Serializable object) throws NoSuchAlgorithmException {
        return full(object).substring(5, 24);
    }

    public static String full(Serializable object) throws NoSuchAlgorithmException {
        String tmp;
        if (object instanceof String) {
            tmp = (String) object;
        } else {
            tmp = GsonUtil.toJson(object);
        }
        StringBuilder buffer = new StringBuilder();
        byte[] digest = getInstance().digest(tmp.getBytes(StandardCharsets.UTF_8));
        for (byte b : digest) {
            int i = ((int) b) & 0xff;
            String s = Integer.toHexString(i);
            if (s.length() < 2) {
                buffer.append("0");
            }
            buffer.append(s);
        }
        return buffer.toString();
    }

    public static String compressed(Serializable object) throws NoSuchAlgorithmException {
        return new BigInteger(standard(object), 16).toString(32);
    }

    public static String fullCompressed(Serializable object) throws NoSuchAlgorithmException {
        return new BigInteger(full(object), 16).toString(32);
    }

    public static String createTag(String password, Long issuedAt) throws NoSuchAlgorithmException {
        password = password.toLowerCase();
        issuedAt = issuedAt / 1000 * 1000;
        return fullCompressed(password + issuedAt);
    }
}
