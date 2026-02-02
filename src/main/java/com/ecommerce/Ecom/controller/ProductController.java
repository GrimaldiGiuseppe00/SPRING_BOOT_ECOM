package com.ecommerce.Ecom.controller;

import com.ecommerce.Ecom.config.AppConstants;
import com.ecommerce.Ecom.model.Product;
import com.ecommerce.Ecom.payload.ProductDto;
import com.ecommerce.Ecom.payload.ProductResponse;
import com.ecommerce.Ecom.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api")
public class ProductController {
    @Autowired
    private ProductService productService;
    @PostMapping("/admin/categories/{categoryId}/product")
    public ResponseEntity<ProductDto> addProduct(@Valid @RequestBody ProductDto productDto,
                                                 @PathVariable Long categoryId) {
    ProductDto savedproductDto= productService.addProduct(categoryId,productDto);
    return new ResponseEntity<>(savedproductDto, HttpStatus.CREATED);
    }

    @GetMapping("/public/products")
    public ResponseEntity<ProductResponse> getAllProducts(@RequestParam(name="pageNumber",defaultValue = AppConstants.PAGE_NUMBER,required = false)Integer pageNumber
            ,@RequestParam(name="pageSize",defaultValue = AppConstants.PAGE_SIZE,required = false)Integer pageSize
            ,@RequestParam(name = "sortBy",defaultValue = AppConstants.SORT_PRODUCTS_BY,required = false)String sortBy,@RequestParam(name = "sortOrder",defaultValue = AppConstants.SORT_DIRECTION,required = false)String sortOrder) {
        ProductResponse productsResponse=productService.getAllProducts(pageNumber,pageSize,sortBy,sortOrder);
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
    public ResponseEntity<ProductDto> updateProduct(@Valid @PathVariable Long productId,
                                                    @RequestBody ProductDto productDto) {
        ProductDto savedProductDto= productService.updateProduct(productId,productDto);
        return new ResponseEntity<>(savedProductDto, HttpStatus.OK);
    }
    @DeleteMapping("admin/products/{productId}")
    public ResponseEntity<ProductDto> deleteProduct(@PathVariable Long productId) {
        ProductDto deleteProductDto=productService.deleteProduct(productId);
        return new ResponseEntity<>(deleteProductDto, HttpStatus.OK);
    }
    @PutMapping("/products/{productId}/image")
    public ResponseEntity<ProductDto> updateProductImage(@PathVariable Long productId,
                                                         @RequestParam("image") MultipartFile image) throws IOException {
    ProductDto updatedProduct=productService.updateProductImage(productId,image);
    return new ResponseEntity<>(updatedProduct, HttpStatus.OK);
    }
}
