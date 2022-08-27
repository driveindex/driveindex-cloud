package io.github.driveindex.azure.util;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.lang.Nullable;

/**
 * @author sgpublic
 * @Date 2022/8/16 21:11
 */
public class PageUtil {
    public static final Long DEFAULT_SIZE = 15L;

    public static <T> Page<T> createPage(
            @Nullable Long pageSize,
            @Nullable Long pageIndex
    ) {
        return new Page<T>()
                .setCurrent(pageIndex == null ? 0 : pageIndex)
                .setSize(pageSize == null ? DEFAULT_SIZE : pageSize);
    }
}
