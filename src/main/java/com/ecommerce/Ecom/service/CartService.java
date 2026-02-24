package com.ecommerce.Ecom.service;

import com.ecommerce.Ecom.payload.CartDto;

public interface CartService {
    public CartDto addProductToCart(Long productId, Integer quantity);
}
