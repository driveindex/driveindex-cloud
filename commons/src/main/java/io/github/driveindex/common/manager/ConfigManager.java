package io.github.driveindex.common.manager;

import io.github.driveindex.common.DriveIndexCommon;
import lombok.extern.slf4j.Slf4j;
import org.ini4j.Ini;
import org.ini4j.Profile;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * @author sgpublic
 * @Date 2022/8/5 11:41
 */

@Slf4j
@Component("ConfigManager")
public class ConfigManager {
    private static final String CONFIG_NAME = "driveindex.ini";
    private File config = new File("./conf", CONFIG_NAME);

    @Value("${driveindex.config:./conf}")
    public void setConfigFile(String path) {
        config = new File(path, CONFIG_NAME);
        log.info("使用配置文件：" + config);
    }

    @PostConstruct
    protected void init() throws IOException {
        if (!config.exists() && (!config.getParentFile().mkdirs()
                || !config.createNewFile())) {
            log.warn("配置文件创建失败，将使用默认配置！");
        }
        ini = new Ini(config);
    }

    private static Ini ini;

    private static Profile.Section getSection(String sectionName) throws IOException {
        Profile.Section section = ini.get(sectionName);
        if (section == null) {
            ini.add(sectionName);
        }
        ini.store();
        return ini.get(sectionName);
    }

    private static final String SECTION_ADMIN = "admin";

    private static final String KEY_PASSWORD = "password";
    public static final String DEFAULT_PASSWORD = "driveindex";
    public static String getAdminPassword() {
        try {
            return getSection(SECTION_ADMIN)
                    .getOrDefault(KEY_PASSWORD, DEFAULT_PASSWORD);
        } catch (Exception e) {
            log.warn("后台管理员密码配置信息获取失败，使用默认值", e);
            return DEFAULT_PASSWORD;
        }
    }
    public static void setAdminPassword(String password) throws IOException {
        Profile.Section section = getSection(SECTION_ADMIN);
        section.put(KEY_PASSWORD, password);
        ini.put(SECTION_ADMIN, section);
        ini.store();
    }

    private static final String SECTION_JWT = "jwt";

    private static final String KEY_JET_SECURITY = "security";
    public static byte[] getTokenSecurityKey() {
        byte[] base;
        try {
            base = getSection(SECTION_JWT)
                    .getOrDefault(KEY_JET_SECURITY, DriveIndexCommon.APPLICATION_BASE_NAME)
                    .getBytes(StandardCharsets.UTF_8);
        } catch (Exception e) {
            log.warn("token 加密密钥配置信息获取失败，使用默认值", e);
            base = DriveIndexCommon.APPLICATION_BASE_NAME.getBytes(StandardCharsets.UTF_8);
        }
        if (base.length < 128) {
            byte[] clone = base.clone();
            base = new byte[128];
            for (int i = 0; i < 128; i++) {
                base[i] = clone[i % clone.length];
            }
        }
        return Base64.getEncoder().encode(base);
    }

    private static final String KEY_JWT_EXPIRED = "expired";
    private static final long DEFAULT_JWT_EXPIRED = 3600_000;
    public static long getTokenExpired() {
        try {
            String expired = getSection(SECTION_JWT)
                    .getOrDefault(KEY_JWT_EXPIRED, Long.toString(DEFAULT_JWT_EXPIRED));
            return Long.parseLong(expired);
        } catch (Exception e) {
            log.warn("token 时效配置信息获取失败，使用默认值", e);
            return DEFAULT_JWT_EXPIRED;
        }
    }
}
