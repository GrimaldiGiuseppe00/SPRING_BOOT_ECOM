package com.ecommerce.Ecom.controller;

import com.ecommerce.Ecom.model.CartItem;
import com.ecommerce.Ecom.payload.CartDto;
import com.ecommerce.Ecom.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class CartController {
    @Autowired
    private CartService cartService;
    @PostMapping("/carts/products/{productId}/quantity/{quantity}")
    public ResponseEntity<CartDto> addProductToCart(@PathVariable Long productId,
                                                    @PathVariable Integer quantity){
        CartDto cartDto = cartService.addProductToCart(productId,quantity);
        return new ResponseEntity<>(cartDto,HttpStatus.CREATED);
    }
}
