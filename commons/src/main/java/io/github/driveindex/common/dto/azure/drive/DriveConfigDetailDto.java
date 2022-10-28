package io.github.driveindex.common.dto.azure.drive;

import io.github.driveindex.common.util.CanonicalPath;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * @author sgpublic
 * @Date 2022/8/7 17:02
 */
@Data
public class DriveConfigDetailDto implements Serializable, Cloneable {
    @Schema(description = "目录配置代号")
    private String calledName;
    @Schema(description = "起始目录")
    private String dirHome;
    @Schema(description = "是否启用")
    private Boolean enable = true;
    public void setCanonicalDirHome(CanonicalPath dirHome) {
        this.dirHome = dirHome.getPath();
    }
    public CanonicalPath getCanonicalDirHome() {
        return CanonicalPath.of(this.dirHome);
    }

    @SuppressWarnings("MethodDoesntCallSuperMethod")
    @Override
    public DriveConfigDetailDto clone() {
        DriveConfigDetailDto newObj = new DriveConfigDetailDto();
        newObj.setCalledName(this.getCalledName());
        newObj.setDirHome(this.getDirHome());
        newObj.setEnable(this.getEnable());
        return newObj;
    }
}
