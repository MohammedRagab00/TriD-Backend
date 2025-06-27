package com.gotrid.trid.api.address.services;


import com.gotrid.trid.api.address.dto.AddressRequest;
import com.gotrid.trid.api.address.dto.AddressResponse;
import com.gotrid.trid.common.exception.custom.UnAuthorizedException;
import com.gotrid.trid.core.address.mapper.AddressMapper;
import com.gotrid.trid.core.address.model.Address;
import com.gotrid.trid.core.address.repository.AddressRepository;
import com.gotrid.trid.core.user.model.Users;
import com.gotrid.trid.core.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Service
public class AddressService implements IAddressService {

    private final AddressRepository addressRepository;
    private final UserRepository userRepository;
    private final AddressMapper addressMapper;

    @Override
    public Integer addAddress(AddressRequest dto, Integer userId) {
        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Address address = addressMapper.toEntity(dto);
        address.setUser(user);

        return addressRepository.saveAndFlush(address).getId();
    }

    @Override
    public List<AddressResponse> getAddressForUser(Integer userId) {
        List<Address> address = addressRepository.findByUser_Id(userId);
        return address.stream().map(addressMapper::toResponse).toList();
    }

    @Override
    public void updateAddress(Integer id, AddressRequest dto, Integer userId) {
        Address address = addressRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Address not found"));

        isUserAuthorized(address, userId);

        addressMapper.updateExisting(address, dto);

        addressRepository.save(address);
    }

    @Override
    public void deleteAddress(Integer id, Integer userId) {
        Address address = addressRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Address not found"));

        isUserAuthorized(address, userId);

        addressRepository.deleteById(id);
    }

    private void isUserAuthorized(Address address, Integer userId) {
        if (!address.getUser().getId().equals(userId)) {
            throw new UnAuthorizedException("You are not authorized to update this address");
        }
    }
}
