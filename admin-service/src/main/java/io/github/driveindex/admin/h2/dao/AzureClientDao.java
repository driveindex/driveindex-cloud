package io.github.driveindex.admin.h2.dao;

import io.github.driveindex.common.dto.azure.drive.AzureClientDetailDto;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author sgpublic
 * @Date 2022/8/7 18:02
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class AzureClientDao extends AzureClientDetailDto {
    private String id;
    private Boolean enable;

    private Long defaultTargetFlag;

    public static AzureClientDao of(String id, AzureClientDetailDto dto) {
        AzureClientDao dao = new AzureClientDao();
        dao.setId(id);
        dao.setClientId(dto.getClientId());
        dao.setCalledName(dto.getCalledName());
        dao.setClientId(dto.getClientId());
        dao.setClientSecret(dto.getClientSecret());
        dao.setDefaultTargetFlag(-System.currentTimeMillis());
        return dao;
    }
}
