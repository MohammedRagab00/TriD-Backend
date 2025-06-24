package com.gotrid.trid.api.address.services;


import com.gotrid.trid.api.address.dto.AddressRequest;
import com.gotrid.trid.api.address.dto.AddressResponse;

public interface AddressService {
    void addAddress(AddressRequest addressDto);

    AddressResponse getAddressById(Integer id);

    void updateAddress(Integer id, AddressRequest dto);

    void deleteAddress(Integer id);
}

