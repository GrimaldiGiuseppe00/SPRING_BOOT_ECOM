package com.ecommerce.Ecom.controller;

import com.ecommerce.Ecom.model.Category;
import com.ecommerce.Ecom.payload.CategoryDto;
import com.ecommerce.Ecom.payload.CategoryResponse;
import com.ecommerce.Ecom.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;



    @GetMapping("/public/categories")
        public ResponseEntity<CategoryResponse> getAllCategories(){
            CategoryResponse categoryResponse = categoryService.getAllCategories();
            return new ResponseEntity<>(categoryResponse, HttpStatus.OK);
        }

        @PostMapping("/public/categories")
    public ResponseEntity<CategoryDto> createCategory(@Valid @RequestBody CategoryDto categoryDto){
            CategoryDto savedCategoryDto=categoryService.createCategory(categoryDto);
            return new ResponseEntity<>(savedCategoryDto,HttpStatus.CREATED);
        }
        @DeleteMapping("/admin/categories/{categoryId}")
        public ResponseEntity<String> deleteCategory(@PathVariable Long categoryId){
            String status = categoryService.deleteCategory(categoryId);
            return new ResponseEntity<>(status, HttpStatus.OK);


    }
    @PutMapping("/admin/categories/{categoryId}")
    public ResponseEntity<CategoryDto> updateCategory(@PathVariable @Valid Long categoryId, @RequestBody CategoryDto categoryDto){
            CategoryDto savedCategoryDto = categoryService.updateCategory(categoryDto, categoryId);
        return new ResponseEntity<>(savedCategoryDto, HttpStatus.OK);

    }


}
