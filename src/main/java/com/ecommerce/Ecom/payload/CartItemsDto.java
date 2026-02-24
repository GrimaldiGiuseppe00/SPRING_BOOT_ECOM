package com.ecommerce.Ecom.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartItemsDto {
    private Long CartItemId;
    private CartDto cart;
    private ProductDto productDto;
    private Integer quantity;
    private Double discount;
    private Double productPrice;
}
