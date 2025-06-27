package com.gotrid.trid.api.address.services;

import com.gotrid.trid.api.address.dto.AddressRequest;
import com.gotrid.trid.api.address.dto.AddressResponse;

import java.util.List;

public interface IAddressService {
    Integer addAddress(AddressRequest addressRequest, Integer userId);

    List<AddressResponse> getAddressForUser(Integer userId);

    void updateAddress(Integer id, AddressRequest dto, Integer userId);

    void deleteAddress(Integer id, Integer userId);
}
