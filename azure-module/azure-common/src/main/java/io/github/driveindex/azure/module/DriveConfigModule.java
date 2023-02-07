package io.github.driveindex.azure.module;

import io.github.driveindex.azure.feign.ConfigClient;
import io.github.driveindex.common.dto.azure.drive.AzureDriveDto;
import io.github.driveindex.common.dto.azure.drive.AzureDriveListDto;
import io.github.driveindex.common.util.GsonUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * @author sgpublic
 * @Date 2023/2/6 16:27
 */
@Component("azureDriveConfigModule")
@RequiredArgsConstructor
public class DriveConfigModule {
    private final ConfigClient configClient;

    public AzureDriveDto getConfig(
            @Nullable String client,
            @Nullable String account,
            @Nullable String drive
    ) {
        Map<String, Object> config = configClient.getConfig(client, account, drive);
        Double code = (Double) config.get("code");
        if (code != 200) return null;
        //noinspection unchecked
        Map<String, Object> token = (Map<String, Object>) config.get("data");
        return GsonUtil.fromJson(GsonUtil.fromMap(token), AzureDriveDto.class);
    }

    public AzureDriveListDto listConfig() {
        Map<String, Object> config = configClient.listConfig();
        Double code = (Double) config.get("code");
        if (code != 200) return null;
        //noinspection unchecked
        List<Object> token = (List<Object>) config.get("data");
        return GsonUtil.fromJson(GsonUtil.fromList(token), AzureDriveListDto.class);
    }
}
