package io.github.driveindex.azure.h2.dao;

import com.baomidou.mybatisplus.annotation.TableName;
import io.github.driveindex.common.dto.azure.file.DirDetailDto;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author sgpublic
 * @Date 2022/8/15 18:35
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("dir_cache")
public class DirCacheEntity extends DirDetailDto {
    private String id;
}
