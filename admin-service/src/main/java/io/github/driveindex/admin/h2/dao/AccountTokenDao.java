package io.github.driveindex.admin.h2.dao;

import com.baomidou.mybatisplus.annotation.TableName;
import io.github.driveindex.common.dto.azure.microsoft.AccountTokenDto;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * @author sgpublic
 * @Date 2022/8/7 18:02
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("account_token")
public class AccountTokenDao extends AccountTokenDto.Response {
    private String id;
    private String parentClient;

    private Long defaultTargetFlag = -(new Date().getTime());
}
