package io.github.driveindex.azure.h2.dao;

import com.baomidou.mybatisplus.annotation.TableName;
import io.github.driveindex.common.dto.azure.file.FileDetailDto;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author sgpublic
 * @Date 2022/8/15 18:35
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("file_cache")
public class FileCacheEntity extends FileDetailDto {
    private String id;
    private String downloadUrl;
}
