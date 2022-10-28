package io.github.driveindex.common.dto.azure.file;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author sgpublic
 * @Date 2022/8/2 20:27
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class DirDetailDto extends AzureItemDto.Detail implements Cloneable {
    @Schema(description = "当前目录下子项数目")
    private Integer childCount;

    @SuppressWarnings("MethodDoesntCallSuperMethod")
    @Override
    public DirDetailDto clone() {
        DirDetailDto newObj = new DirDetailDto();
        newObj.setChildCount(getChildCount());
        return newObj;
    }
}
