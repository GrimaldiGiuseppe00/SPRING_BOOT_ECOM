package com.ecommerce.Ecom.service;

import com.ecommerce.Ecom.model.User;
import com.ecommerce.Ecom.payload.AddressDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface AddressService {
    AddressDto createAddress(AddressDto addressDto, User user);

    List<AddressDto> getAllAddresses();
}
