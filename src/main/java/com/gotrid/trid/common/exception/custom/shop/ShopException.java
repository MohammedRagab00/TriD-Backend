package com.gotrid.trid.common.exception.custom.shop;

import com.gotrid.trid.common.exception.handler.BusinessErrorCode;
import lombok.Getter;

@Getter
public class ShopException extends RuntimeException {
    private final BusinessErrorCode errorCode;

    public ShopException(BusinessErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }
}
