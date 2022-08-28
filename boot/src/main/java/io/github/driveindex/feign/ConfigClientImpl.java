package io.github.driveindex.feign;

import io.github.driveindex.admin.controller.AzureConfigController;
import io.github.driveindex.azure.feign.ConfigClient;
import io.github.driveindex.common.dto.result.ResponseData;
import io.github.driveindex.common.util.GsonUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author sgpublic
 * @Date 2022/8/27 14:07
 */
@Component
@RequiredArgsConstructor
public class ConfigClientImpl implements ConfigClient {
    private final AzureConfigController controller;

    /** SpringBoot 不再需要远程调用接口获取信息，本地调用即可。 */
    @Override
    public Map<String, Object> getConfig(String client, String account, String drive) {
        ResponseData config = controller.getConfig(client, account, drive);
        return GsonUtil.toMap(GsonUtil.toJson(config));
    }
}
