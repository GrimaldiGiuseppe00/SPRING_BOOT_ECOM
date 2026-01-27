package com.ecommerce.Ecom.service;

import com.ecommerce.Ecom.model.Product;
import com.ecommerce.Ecom.payload.ProductDto;
import com.ecommerce.Ecom.payload.ProductResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Service
public interface ProductService {
    ProductDto addProduct(Long categoryId,Product product);
    ProductResponse getAllProducts();
    ProductResponse getAllProductsByCategory(Long categoryId);
    ProductResponse getAllProductsByKeyword(String keyword);
    ProductDto updateProduct(Long productId,Product product);
}
