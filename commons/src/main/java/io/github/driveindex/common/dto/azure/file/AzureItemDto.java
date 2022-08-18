package io.github.driveindex.common.dto.azure.file;

import io.github.driveindex.common.util.Timestamps;
import lombok.Data;

import java.io.Serializable;

/**
 * @author sgpublic
 * @Date 2022/8/15 18:20
 */
@Data
public class AzureItemDto implements Serializable, Cloneable {
    private Long createTime;
    private Long modifiedTime;
    private Long size;
    private String webUrl;

    public void setCreateTime(Long time) {
        this.createTime = time;
    }

    public void setCreateTime(String utc) {
        this.createTime = Timestamps.from(utc);
    }

    public void setModifiedTime(Long time) {
        this.modifiedTime = time;
    }

    public void setModifiedTime(String utc) {
        this.modifiedTime = Timestamps.from(utc);
    }

    public static abstract class Detail implements Serializable { }

    @SuppressWarnings("MethodDoesntCallSuperMethod")
    @Override
    public AzureItemDto clone() {
        AzureItemDto newObj = new AzureItemDto();
        newObj.setCreateTime(this.getCreateTime());
        newObj.setModifiedTime(this.getModifiedTime());
        newObj.setSize(this.getSize());
        newObj.setWebUrl(this.getWebUrl());
        return newObj;
    }
}