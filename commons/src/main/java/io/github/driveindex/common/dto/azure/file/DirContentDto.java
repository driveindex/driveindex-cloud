package io.github.driveindex.common.dto.azure.file;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.tomcat.jni.File;
import org.springframework.lang.Nullable;

import java.util.LinkedList;
import java.util.List;

/**
 * @author sgpublic
 * @Date 2022/8/16 18:11
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class DirContentDto extends AzureContentDto<DirContentDto.FileList> {
    public static class FileList extends LinkedList<FileItemDto> {

    }

    @Schema(description = "分页总数")
    private Long totalPage;
    @Schema(description = "记录总数")
    private Long totalCount;

    @Schema(description = "README.md 内容")
    @Nullable
    private String readme;
    @Schema(description = "HEADER.md 内容")
    @Nullable
    private String header;
}
