package io.github.driveindex.common.dto.azure.file;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.lang.Nullable;

import java.util.List;

/**
 * @author sgpublic
 * @Date 2022/8/16 18:11
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class DirContentDto extends AzureContentDto<List<FileItemDto>> {
    private Long totalPage;
    private Long totalCount;

    @Nullable
    private String readme;
    @Nullable
    private String header;
}
