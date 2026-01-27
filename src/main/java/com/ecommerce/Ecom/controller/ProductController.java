package com.ecommerce.Ecom.controller;

import com.ecommerce.Ecom.model.Product;
import com.ecommerce.Ecom.payload.ProductDto;
import com.ecommerce.Ecom.payload.ProductResponse;
import com.ecommerce.Ecom.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ProductController {
    @Autowired
    private ProductService productService;
    @PostMapping("/admin/categories/{categoryId}/product")
    public ResponseEntity<ProductDto> addProduct(@RequestBody Product product,
                                                 @PathVariable Long categoryId) {
    ProductDto productDto= productService.addProduct(categoryId,product);
    return new ResponseEntity<>(productDto, HttpStatus.CREATED);
    }

    @GetMapping("/public/products")
    public ResponseEntity<ProductResponse> getAllProducts() {
        ProductResponse productsResponse=productService.getAllProducts();
        return new ResponseEntity<>(productsResponse, HttpStatus.OK);
    }
    @GetMapping("/public/categories/{categoryId}/products")
    public ResponseEntity<ProductResponse> getAllProductsByCategory(@PathVariable Long categoryId) {
        ProductResponse productsResponse=productService.getAllProductsByCategory(categoryId);
        return new ResponseEntity<>(productsResponse, HttpStatus.OK);
    }
    @GetMapping("public/products/keyword/{keyword}")
    public ResponseEntity<ProductResponse> getAllProductsByKeyword(@PathVariable String keyword) {
        ProductResponse productsResponse= productService.getAllProductsByKeyword("%"+ keyword + "%");
        return new ResponseEntity<>(productsResponse,HttpStatus.FOUND);
    }

    @PutMapping("admin/products/{productId}")
    public ResponseEntity<ProductDto> updateProduct(@PathVariable Long productId,
                                                    @RequestBody Product product) {
        ProductDto productDto= productService.updateProduct(productId,product);
        return new ResponseEntity<>(productDto, HttpStatus.OK);
    }
}
