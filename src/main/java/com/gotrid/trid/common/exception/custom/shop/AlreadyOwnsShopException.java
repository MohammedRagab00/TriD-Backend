package com.gotrid.trid.common.exception.custom.shop;

public class AlreadyOwnsShopException extends RuntimeException {
    public AlreadyOwnsShopException(String message) {
        super(message);
    }
}
