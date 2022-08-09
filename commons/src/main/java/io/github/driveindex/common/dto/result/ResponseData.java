package io.github.driveindex.common.dto.result;

import io.github.driveindex.common.util.GsonUtil;

import java.io.Serializable;

/**
 * @author sgpublic
 * @Date 2022/8/3 13:04
 */
public abstract class ResponseData implements Serializable {
    public final Integer code;
    public final String message;

    public ResponseData(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public String toString() {
        return GsonUtil.toJson(this);
    }
}
