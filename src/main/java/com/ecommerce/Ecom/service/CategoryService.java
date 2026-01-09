package com.ecommerce.Ecom.service;

import com.ecommerce.Ecom.model.Category;
import com.ecommerce.Ecom.payload.CategoryDto;
import com.ecommerce.Ecom.payload.CategoryResponse;
import org.springframework.stereotype.Service;

@Service
public interface CategoryService {
    CategoryResponse getAllCategories(Integer pageNumber, Integer pageSize,String sortBy,String sortOrder);
    CategoryDto createCategory(CategoryDto categoryDto);
    String deleteCategory(Long categoryId);
    CategoryDto updateCategory(CategoryDto categoryDto, Long categoryId);
}
