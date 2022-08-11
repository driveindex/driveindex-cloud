package io.github.driveindex.common.dto.result;

import io.github.driveindex.common.util.GsonUtil;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * @author sgpublic
 * @Date 2022/8/3 13:00
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SuccessResult<T extends Serializable> extends SampleSuccessResult {
    public static final SampleSuccessResult SAMPLE = new SampleSuccessResult();

    private final T data;

    private SuccessResult(T data) {
        this.data = data;
    }

    public static <T extends Serializable> SuccessResult<T> of(T data) {
        return new SuccessResult<>(data);
    }

    @Override
    public String toString() {
        return GsonUtil.toJson(this);
    }
}

class SampleSuccessResult extends ResponseData {
    public SampleSuccessResult() {
        super(200, "success.");
    }
}
