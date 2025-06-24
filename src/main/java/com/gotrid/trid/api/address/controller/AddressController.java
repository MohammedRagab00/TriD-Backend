package com.gotrid.trid.api.address.controller;

import com.gotrid.trid.api.address.dto.AddressRequest;
import com.gotrid.trid.api.address.dto.AddressResponse;
import com.gotrid.trid.api.address.services.AddressService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/addresses")
public class AddressController {

    @Autowired
    private AddressService addressService;

    @Operation(summary = "Create new address")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Address added successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @PostMapping
    public void addAddress(@RequestBody AddressRequest addressDto) {
        addressService.addAddress(addressDto);
    }

    @Operation(summary = "Get address by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Address retrieved"),
            @ApiResponse(responseCode = "404", description = "Address not found")
    })
    @GetMapping("/{id}")
    public AddressResponse getAddress(@PathVariable Integer id) {
        return addressService.getAddressById(id);
    }

    @Operation(summary = "Update address")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Address updated successfully"),
            @ApiResponse(responseCode = "404", description = "Address not found")
    })
    @PutMapping("/{id}")
    public void updateAddress(@PathVariable Integer id, @RequestBody AddressRequest request) {
        addressService.updateAddress(id, request);
    }

    @Operation(summary = "Delete address")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Address deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Address not found")
    })
    @DeleteMapping("/{id}")
    public void deleteAddress(@PathVariable Integer id) {
        addressService.deleteAddress(id);
    }
}
