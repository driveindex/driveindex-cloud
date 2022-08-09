package io.github.driveindex.download;

import io.github.driveindex.common.util.GsonUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

/**
 * @author sgpublic
 * @Date 2022/8/3 9:06
 */
@Import(GsonUtil.class)
@SpringBootApplication
public class DownloadServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(DownloadServiceApplication.class, args);
    }
}
