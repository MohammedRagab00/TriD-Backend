package com.gotrid.trid.api.address.services;


import com.gotrid.trid.api.address.dto.AddressRequest;
import com.gotrid.trid.api.address.dto.AddressResponse;
import com.gotrid.trid.core.address.model.Address;
import com.gotrid.trid.core.address.repository.AddressRepository;
import com.gotrid.trid.core.user.model.Users;
import com.gotrid.trid.core.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AddressServiceImpl implements AddressService {

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public void addAddress(AddressRequest dto) {
        Users user = userRepository.findById(dto.userId()).orElseThrow(() -> new RuntimeException("User not found"));

        Address address = new Address();
        address.setUser(user);
        address.setAddress(dto.address());
        address.setLongitude(dto.coordinates().lng());
        address.setLatitude(dto.coordinates().lat());
        address.setPhone_number(dto.details().phoneNumber());
        address.setLandmark(dto.details().landmark());

        addressRepository.save(address);
    }

    @Override
    public AddressResponse getAddressById(Integer id) {
        Address address = addressRepository.findById(id).orElseThrow(() -> new RuntimeException("Address not found"));

        return mapEntityToResponse(address);
    }

    @Override
    public void updateAddress(Integer id, AddressRequest dto) {
        Address address = addressRepository.findById(id).orElseThrow(() -> new RuntimeException("Address not found"));

        Users user = userRepository.findById(dto.userId()).orElseThrow(() -> new RuntimeException("User not found"));

        mapRequestToEntity(dto, address);
        address.setUser(user);
        addressRepository.save(address);
    }

    @Override
    public void deleteAddress(Integer id) {
        if (!addressRepository.existsById(id)) {
            throw new RuntimeException("Address not found");
        }
        addressRepository.deleteById(id);
    }

    private void mapRequestToEntity(AddressRequest dto, Address address) {
        address.setAddress(dto.address());
        address.setLatitude(dto.coordinates().lat());
        address.setLongitude(dto.coordinates().lng());
        address.setPhone_number(dto.details().phoneNumber());
        address.setLandmark(dto.details().landmark());
    }

    private AddressResponse mapEntityToResponse(Address address) {
        AddressResponse.Coordinates coords = new AddressResponse.Coordinates(
                address.getLatitude(),
                address.getLongitude()
        );
        AddressResponse.Details details = new AddressResponse.Details(
                address.getPhone_number(),
                address.getLandmark()
        );

        return new AddressResponse(
                address.getId(),
                address.getAddress(),
                coords, details
        );
    }
}
