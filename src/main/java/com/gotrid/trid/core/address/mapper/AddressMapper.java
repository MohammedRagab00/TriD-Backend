package com.gotrid.trid.core.address.mapper;

import com.gotrid.trid.api.address.dto.AddressRequest;
import com.gotrid.trid.api.address.dto.AddressResponse;
import com.gotrid.trid.core.address.model.Address;
import org.springframework.stereotype.Component;

@Component
public class AddressMapper {
    public Address toEntity(AddressRequest dto) {
        return Address.builder()
                .address(dto.address())
                .latitude(dto.coordinates().lat())
                .longitude(dto.coordinates().lng())
                .phone_number(dto.details().phoneNumber())
                .landmark(dto.details().landmark())
                .build();
    }

    public AddressResponse toResponse(Address entity) {
        return new AddressResponse(
                entity.getId(),
                entity.getAddress(),
                new AddressRequest.Coordinates(entity.getLatitude(), entity.getLongitude()),
                new AddressRequest.Details(entity.getPhone_number(), entity.getLandmark())
        );
    }

    public void updateExisting(Address address, AddressRequest dto) {
        address.setAddress(dto.address());
        address.setLatitude(dto.coordinates().lat());
        address.setLongitude(dto.coordinates().lng());
        address.setPhone_number(dto.details().phoneNumber());
        address.setLandmark(dto.details().landmark());
    }
}
