package io.github.driveindex.common.util;

/**
 * @author sgpublic
 * @Date 2022/8/9 16:34
 */
public class Value {
    public static <T> void check(T value, Call<T> call) {
        if (value == null) return;
        if (value instanceof String) {
            if (((String) value).isBlank()) return;
        }
        call.setter(value);
    }

    public static interface Call<T> {
        void setter(T value);
    }
}
