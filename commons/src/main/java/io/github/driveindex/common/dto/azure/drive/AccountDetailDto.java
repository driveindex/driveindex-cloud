package io.github.driveindex.common.dto.azure.drive;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * @author sgpublic
 * @Date 2022/8/7 17:00
 */
@Data
public class AccountDetailDto implements Serializable, Cloneable {
    @Schema(description = "账号配置昵称")
    private String calledName;
    @Schema(description = "当前账号配置是否需要登陆微软账号", hidden = true)
    private Boolean needLogin = true;
    @Schema(description = "是否启用")
    private Boolean enable = true;

    @SuppressWarnings("MethodDoesntCallSuperMethod")
    @Override
    public AccountDetailDto clone() {
        AccountDetailDto newObj = new AccountDetailDto();
        newObj.setCalledName(this.getCalledName());
        newObj.setNeedLogin(this.getNeedLogin());
        newObj.setEnable(this.getEnable());
        return newObj;
    }
}
