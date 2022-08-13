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

    @SuppressWarnings("MethodDoesntCallSuperMethod")
    @Override
    public AccountDetailDto clone() {
        AccountDetailDto newObj = new AccountDetailDto();
        newObj.setCalledName(newObj.getCalledName());
        return newObj;
    }
}
