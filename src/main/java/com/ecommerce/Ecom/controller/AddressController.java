package com.ecommerce.Ecom.controller;

import com.ecommerce.Ecom.model.User;
import com.ecommerce.Ecom.payload.AddressDto;
import com.ecommerce.Ecom.service.AddressService;
import com.ecommerce.Ecom.utils.AuthUtil;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class AddressController {
    @Autowired
    private AddressService addressService;
    @Autowired
    private AuthUtil authUtil;
@PostMapping("/addresses")
    public ResponseEntity<AddressDto> createAddress(@Valid @RequestBody AddressDto addressDto) {
        User loggedUser = authUtil.loggedInUser();
        AddressDto savedAddressDto= addressService.createAddress(addressDto,loggedUser);
        return new ResponseEntity<>(savedAddressDto, HttpStatus.CREATED);
    }
    @GetMapping("/addresses")
    public ResponseEntity<List<AddressDto>> getAddresses() {
    List<AddressDto> addressDtos= addressService.getAllAddresses();
    return new ResponseEntity<>(addressDtos, HttpStatus.OK);
    }

}
