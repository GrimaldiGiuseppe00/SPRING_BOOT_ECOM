package com.ecommerce.Ecom.service;

import com.ecommerce.Ecom.exceptions.ResourceNotFoundException;
import com.ecommerce.Ecom.model.Category;
import com.ecommerce.Ecom.model.Product;
import com.ecommerce.Ecom.payload.ProductDto;
import com.ecommerce.Ecom.payload.ProductResponse;
import com.ecommerce.Ecom.repositories.CategoryRepository;
import com.ecommerce.Ecom.repositories.ProductRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Override
    public ProductDto addProduct(Long categoryId, Product product) {
    Category category = categoryRepository.findById(categoryId).orElseThrow(
            ()-> new ResourceNotFoundException("Category","categoryId",categoryId)
    );
    product.setImage("default.png");
    product.setCategory(category);
    double specialPrice = product.getPrice()
            -((product.getDiscount() * 0.01) * product.getPrice());
    product.setSpecialPrice(specialPrice);
    Product savedProduct = productRepository.save(product);
    return modelMapper.map(savedProduct, ProductDto.class);

    }

    @Override
    public ProductResponse getAllProducts() {
       List<Product> products = productRepository.findAll();
       List<ProductDto> productDtos = products.stream()
               .map(product-> modelMapper.map(product,ProductDto.class))
               .collect(Collectors.toList());
       ProductResponse productResponse = new ProductResponse();
       productResponse.setContent(productDtos);
       return productResponse;
    }

    @Override
    public ProductResponse getAllProductsByCategory(Long categoryId) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(
                ()-> new ResourceNotFoundException("Category","categoryId",categoryId)
        );
        List<Product>products=productRepository.findByCategoryOrderByPriceAsc(category);
        List<ProductDto> productDtos = products.stream()
                .map(product-> modelMapper.map(product,ProductDto.class))
                .collect(Collectors.toList());
        ProductResponse productResponse = new ProductResponse();
        productResponse.setContent(productDtos);
        return productResponse;



    }

    @Override
    public ProductResponse getAllProductsByKeyword(String keyword) {
        List<Product> products = productRepository.findByProductNameLikeIgnoreCase(keyword);
        List<ProductDto> productDtos = products.stream()
                .map(product-> modelMapper.map(product,ProductDto.class))
                .collect(Collectors.toList());
        ProductResponse productResponse = new ProductResponse();
        productResponse.setContent(productDtos);
        return productResponse;
    }

    @Override
    public ProductDto updateProduct(Long productId, Product product) {

        Product productFromDb = productRepository.findById(productId).orElseThrow(
                ()-> new ResourceNotFoundException("Product","ProductId",productId)
        );
        productFromDb.setProductName(product.getProductName());
        productFromDb.setDescription(product.getDescription());
        productFromDb.setPrice(product.getPrice());
        double specialPrice = product.getPrice()
                -((product.getDiscount() * 0.01) * product.getPrice());
        productFromDb.setSpecialPrice(specialPrice);
        productFromDb.setCategory(product.getCategory());
        productFromDb.setQuantity(product.getQuantity());
        productFromDb.setDiscount(product.getDiscount());
        Product savedProduct=productRepository.save(productFromDb);
        ProductDto productDto = modelMapper.map(productFromDb,ProductDto.class);
        return productDto;


    }

}
