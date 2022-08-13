package io.github.driveindex.common.dto.azure.drive;

import lombok.Data;

import java.io.Serializable;

/**
 * @author sgpublic
 * @Date 2022/8/7 17:02
 */
@Data
public class DriveConfigDetailDto implements Serializable, Cloneable {
    private String calledName;
    private String dirHome;

    @SuppressWarnings("MethodDoesntCallSuperMethod")
    @Override
    public DriveConfigDetailDto clone() {
        DriveConfigDetailDto newObj = new DriveConfigDetailDto();
        newObj.setCalledName(this.getCalledName());
        newObj.setDirHome(this.getDirHome());
        return newObj;
    }
}
