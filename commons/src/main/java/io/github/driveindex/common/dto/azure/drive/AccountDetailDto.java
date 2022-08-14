package io.github.driveindex.common.dto.azure.drive;

import lombok.Data;

import java.io.Serializable;

/**
 * @author sgpublic
 * @Date 2022/8/7 17:00
 */
@Data
public class AccountDetailDto implements Serializable, Cloneable {
    private String calledName;
    private Boolean needLogin = true;
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
