package io.github.driveindex.common.exception;

import feign.Request;
import feign.codec.DecodeException;

/**
 * @author sgpublic
 * @Date 2022/8/15 11:24
 */
public class AzureDecodeException extends DecodeException {
    public static final String CODE_ITEM_NOT_FOUND = "itemNotFound";

    private final String code;

    public AzureDecodeException(int status, String code, String message, Request request) {
        super(status, message, request);
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}