package io.github.driveindex.azure.h2.dao;

import com.baomidou.mybatisplus.annotation.TableName;
import io.github.driveindex.common.dto.azure.file.AzureItemDto;
import io.github.driveindex.common.manager.ConfigManager;
import lombok.Data;

/**
 * @author sgpublic
 * @Date 2022/8/15 18:35
 */
@Data
@TableName("cache_central")
public class CacheCentralEntity extends AzureItemDto {
    private String id;
    private String name;
    private String canonicalPath;
    private Integer canonicalPathHash;
    private String mineType;
    private Boolean isDir;
    private String parentId;
    private Long expiresTime = System.currentTimeMillis() + ConfigManager.getCacheExpiresIn() * 1000;

    public void setCanonicalPathHash(Integer hash) {
        canonicalPathHash = hash;
    }

    public void setCanonicalPathHash(String canonicalPath) {
        canonicalPathHash = canonicalPath.toLowerCase().hashCode();
    }

    public static final String MEDIA_TYPE_DIR = "directory";

    @Override
    public int hashCode() {
        return (id == null) ? super.hashCode() : id.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CacheCentralEntity other)) return false;
        if (other.getId() != null) return other.getId().equals(this.getId());
        return this.getId() == null;
    }

    public enum Sort {
        NAME,
        SIZE,
        CREATE_TIME,
        MODIFIED_TIME;

        @Override
        public String toString() {
            return super.name().toLowerCase();
        }
    }
}
