package io.github.driveindex.common.util;

import java.sql.Timestamp;
import java.time.Instant;

/**
 * @author sgpublic
 * @Date 2022/8/17 9:23
 */
public class Timestamps {
    public static long from(String utc) {
        return Timestamp.from(Instant.parse(utc)).getTime();
    }
}
