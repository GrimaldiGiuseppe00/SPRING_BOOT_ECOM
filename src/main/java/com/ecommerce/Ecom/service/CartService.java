package com.ecommerce.Ecom.service;

import com.ecommerce.Ecom.payload.CartDto;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public interface CartService {
    CartDto addProductToCart(Long productId, Integer quantity);

    CartDto getCart();

    List<CartDto> getAllCarts();

    @Transactional
    CartDto updateProductQuantityInCart(Long productId, Integer quantity);

    String deleteProductFromCart(Long cartId, Long productId);

    void updateProductInCarts(Long cartId, Long productId);
}
