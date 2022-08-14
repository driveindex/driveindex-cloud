package io.github.driveindex.common;

import org.springframework.boot.SpringApplication;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public static class Bootstrap {
        private final SpringApplication application;

        public Bootstrap(Class<?> clazz) {
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

        public Bootstrap setSqlSchema() {
            properties.put("spring.sql.init.schema-locations", List.of("classpath:/db/schema.sql"));
            return enableSqlInit();
        }

        public Bootstrap setSqlData() {
            properties.put("spring.sql.init.data-locations", List.of("classpath:/db/data.sql"));
            return enableSqlInit();
        }

        public Bootstrap setDatasource(String name) {
            properties.put("spring.datasource.username", APPLICATION_BASE_NAME.toLowerCase());
            properties.put("spring.datasource.url", "jdbc:h2:file:./data/" + name);
            properties.put("spring.datasource.driver-class-name", "org.h2.Driver");
            return this;
        }

        public void run(String[] args) {
            application.setDefaultProperties(properties);
            application.run(args);
        }
    }
}
