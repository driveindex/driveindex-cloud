package io.github.driveindex.common.manager;

import io.github.driveindex.common.DriveIndexCommon;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ini4j.Profile;
import org.ini4j.Wini;
import org.ini4j.spi.BeanTool;
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
@Component
public class ConfigManager {
    private static final String SectionAdmin = "admin";
    public static IniVar<String> Password = new IniVar<>(
            SectionAdmin, "password", DriveIndexCommon.APPLICATION_BASE_NAME_LOWER
    );

    private static final String SectionJwt = "jwt";
    public static IniVal<String> TokenSecurityKey = new IniVar<>(
            SectionJwt, "security", DriveIndexCommon.APPLICATION_BASE_NAME_LOWER
    );
    public static byte[] getTokenSecurityKey() {
        byte[] base = TokenSecurityKey.getValue().getBytes(StandardCharsets.UTF_8);
        if (base.length < 128) {
            byte[] clone = base.clone();
            base = new byte[128];
            for (int i = 0; i < 128; i++) {
                base[i] = clone[i % clone.length];
            }
        }
        return Base64.getEncoder().encode(base);
    }
    public static IniVal<Long> TokenExpired = new IniVar<>(
            SectionJwt, "security", 3600_000L
    );

    private static final String SectionCache = "cache";
    public static IniVar<Integer> DeltaTrackingTick = new IniVar<>(SectionCache, "expires_in", 60);


    private static final String CONFIG_NAME = "driveindex.ini";
    private static File config = new File("./conf", CONFIG_NAME);

    @Value("${driveindex.config:./conf}")
    public void setConfigFile(String path) {
        config = new File(path, CONFIG_NAME);
        String configPath;
        try {
            configPath = config.getCanonicalPath();
        } catch (IOException e) {
            configPath = config.getPath();
        }
        log.info("使用配置文件：" + configPath);
    }

    @PostConstruct
    protected void init() throws IOException {
        if (!config.exists()) {
            File parent = config.getParentFile();
            if ((parent.exists() || !parent.mkdirs()) && !config.createNewFile()) {
                log.warn("配置文件创建失败，将使用默认配置！");
            }
        }
        ini.setFile(config);
    }

    private static final Wini ini = new Wini();
    static { ini.setFile(config); }

    public static class IniVar<TypeT> extends IniVal<TypeT> {
        public IniVar(String section, String key, TypeT defVal) {
            super(section, key, defVal);
        }

        public void setValue(@NonNull TypeT value) throws IOException {
            Profile.Section section1 = getSection(section);
            section1.put(key, toIni(value));
            ini.put(section, section1);
            ini.store();
        }

        public String toIni(TypeT value) {
            return value.toString();
        }
    }

    @RequiredArgsConstructor
    public static class IniVal<TypeT> {
        protected final String section;
        protected final String key;
        @Getter
        private final TypeT DefaultValue;

        @NonNull
        public TypeT getValue() {
            try {
                return fromIni(getSection(section).get(key));
            } catch (Exception e) {
                return DefaultValue;
            }
        }

        protected TypeT fromIni(String origin) {
            //noinspection unchecked
            TypeT parse = (TypeT) BeanTool.getInstance().parse(origin, DefaultValue.getClass());
            return parse != null ? parse : DefaultValue;
        }

        protected static Profile.Section getSection(String sectionName) throws IOException {
            ini.load();
            Profile.Section section = ini.get(sectionName);
            if (section == null) {
                ini.add(sectionName);
                ini.store();
            }
            return ini.get(sectionName);
        }
    }
}
