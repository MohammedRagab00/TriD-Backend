package com.gotrid.trid.api.address.dto;

import java.math.BigDecimal;

public record AddressRequest(
        String address,
        Coordinates coordinates,
        Details details
) {
    public record Coordinates(BigDecimal lat, BigDecimal lng) {
    }

    public record Details(String phoneNumber, String landmark) {
    }
}


