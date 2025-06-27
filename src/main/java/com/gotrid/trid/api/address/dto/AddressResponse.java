package com.gotrid.trid.api.address.dto;

import java.math.BigDecimal;

public record AddressResponse(
        Integer id,
        String address,
        AddressRequest.Coordinates coordinates,
        AddressRequest.Details details
) {
    public record Coordinates(BigDecimal lat, BigDecimal lng) {
    }

    public record Details(String phoneNumber, String landmark) {
    }
}