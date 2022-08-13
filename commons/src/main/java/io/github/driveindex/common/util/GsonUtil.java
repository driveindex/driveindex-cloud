package io.github.driveindex.common.util;

import com.google.gson.*;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.GsonHttpMessageConverter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

/**
 * @author sgpublic
 * @Date 2022/8/4 9:55
 */
@Configuration
public class GsonUtil {
    @Bean
    public HttpMessageConverters customConverters() {
        Collection<HttpMessageConverter<?>> messageConverters = new ArrayList<>();

        messageConverters.add(new GsonHttpMessageConverter(GsonUtil.GSON));

        return new HttpMessageConverters(true, messageConverters);
    }

    private static final Gson GSON = new GsonBuilder()
            .enableComplexMapKeySerialization()
            .disableInnerClassSerialization()
            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .create();

    public static <T extends Serializable> String toJson(T object) {
        return GSON.toJson(object);
    }

    public static <T extends Serializable> String toJson(T object, Class<T> clazz) {
        return GSON.toJson(object, clazz);
    }

    public static String fromMap(Map<?, ?> map) {
        return GSON.toJson(map);
    }

    public static <T extends Serializable> T fromJson(String json, Class<T> clazz) {
        return GSON.fromJson(json, clazz);
    }
}
