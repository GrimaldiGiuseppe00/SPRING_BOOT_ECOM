package com.ecommerce.Ecom.service;

import com.ecommerce.Ecom.payload.CartDto;

import java.util.List;

public interface CartService {
    CartDto addProductToCart(Long productId, Integer quantity);
    CartDto getCart();
    List<CartDto> getAllCarts();
}
