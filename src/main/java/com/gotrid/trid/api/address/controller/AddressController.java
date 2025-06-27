package com.gotrid.trid.api.address.controller;

import com.gotrid.trid.api.address.dto.AddressRequest;
import com.gotrid.trid.api.address.dto.AddressResponse;
import com.gotrid.trid.api.address.services.IAddressService;
import com.gotrid.trid.config.security.userdetails.UserPrincipal;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@Tag(name = "Address", description = "Address management API")
@SecurityRequirement(name = "Bearer Authentication")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@RestController
@RequestMapping("/address")
public class AddressController {

    private final IAddressService addressService;

    @Operation(summary = "Create new address")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Address added successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @PostMapping
    public ResponseEntity<Void> addAddress(
            @RequestBody AddressRequest addressRequest,
            @Parameter(hidden = true) @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        Integer id = addressService.addAddress(addressRequest, userPrincipal.user().getId());
        return ResponseEntity.created(URI.create("api/v1/address/" + id)).build();
    }

    @Operation(summary = "Get address for user")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Address retrieved"),
            @ApiResponse(responseCode = "404", description = "Address not found")
    })
    @GetMapping
    public ResponseEntity<List<AddressResponse>> getAddress(@Parameter(hidden = true) @AuthenticationPrincipal UserPrincipal userPrincipal) {
        return ResponseEntity.ok(addressService.getAddressForUser(userPrincipal.user().getId()));
    }

    @Operation(summary = "Update address")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Address updated successfully"),
            @ApiResponse(responseCode = "404", description = "Address not found")
    })
    @PutMapping("/{id}")
    public ResponseEntity<Void> updateAddress(
            @PathVariable Integer id, @RequestBody AddressRequest request,
            @Parameter(hidden = true) @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        addressService.updateAddress(id, request, userPrincipal.user().getId());
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Delete address")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Address deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Address not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAddress(
            @PathVariable Integer id,
            @Parameter(hidden = true) @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        addressService.deleteAddress(id, userPrincipal.user().getId());
        return ResponseEntity.noContent().build();
    }
}
