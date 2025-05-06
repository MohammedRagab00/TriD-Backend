package com.gotrid.trid.exception.custom.shop;

import com.gotrid.trid.exception.handler.BusinessErrorCode;
import lombok.Getter;

@Getter
public class ShopException extends RuntimeException {
    private final BusinessErrorCode errorCode;

    public ShopException(BusinessErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }
}
