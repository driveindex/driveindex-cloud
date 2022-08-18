package io.github.driveindex.common.dto.azure.file;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author sgpublic
 * @Date 2022/8/2 20:27
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class DirDetailDto extends AzureItemDto.Detail implements Cloneable {
    private Integer childCount;

    @SuppressWarnings("MethodDoesntCallSuperMethod")
    @Override
    public DirDetailDto clone() {
        DirDetailDto newObj = new DirDetailDto();
        newObj.setChildCount(getChildCount());
        return newObj;
    }
}
