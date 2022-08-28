package io.github.driveindex.azure.h2.dao;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * @author sgpublic
 * @Date 2022/8/15 18:35
 */
@Data
@TableName("content_cache")
public class ContentCacheEntity implements Serializable {
    private String id;
    private String type;
    private String content;
}
