package io.github.driveindex.common;

import org.springframework.boot.SpringApplication;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * @author sgpublic
 * @Date 2022/8/4 14:28
 */
public class DriveIndexCommon {
    public static void main(String[] args) {

    }

    public static final String APPLICATION_BASE_NAME = "DriveIndex";
    public static final String APPLICATION_PACKAGE = "io.github.driveindex";

    public static final String JWT_TAG = "tag";
    public static final String TOKEN_KEY = "DriveIndex-Authentication";
    public static final String SECURITY_HEADER = "DriveIndex-TAG";

    public static Bootstrap Bootstrap(Class<?> clazz) {
        return new Bootstrap(clazz);
    }

    public static class Bootstrap {
        private final SpringApplication application;

        private Bootstrap(Class<?> clazz) {
            application = new SpringApplication(clazz);
        }

        private final Map<String, Object> properties = new HashMap<>();

        public Bootstrap setPort(Integer port) {
            properties.put("server.port", port);
            return this;
        }

        public Bootstrap enableSqlInit() {
            properties.put("spring.sql.init.mode", "always");
            return this;
        }

        public Bootstrap setSqlSchema(String... name) {
            properties.put("spring.sql.init.schema-locations",
                    Stream.of(name).map((Function<String, Object>) s ->
                            "classpath:/db/schema-" + s + ".sql").toList());
            return enableSqlInit();
        }

        public Bootstrap setSqlData(String name) {
            properties.put("spring.sql.init.data-locations",
                    Stream.of(name).map((Function<String, Object>) s ->
                            "classpath:/db/data-" + s + ".sql").toList());
            return enableSqlInit();
        }

        public Bootstrap setDatasource(String name) {
            properties.put("spring.datasource.username", APPLICATION_BASE_NAME.toLowerCase());
            properties.put("spring.datasource.url", "jdbc:h2:file:./data/" + name);
            properties.put("spring.datasource.driver-class-name", "org.h2.Driver");
            return this;
        }

        public void run(String[] args) {
            properties.put("spring.profile.active", "prod");
            application.setDefaultProperties(properties);
            application.run(args);
        }
    }
}
