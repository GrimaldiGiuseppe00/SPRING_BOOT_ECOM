package com.ecommerce.Ecom.service;

import com.ecommerce.Ecom.exceptions.APIException;
import com.ecommerce.Ecom.exceptions.ResourceNotFoundException;
import com.ecommerce.Ecom.model.Category;
import com.ecommerce.Ecom.payload.CategoryDto;
import com.ecommerce.Ecom.payload.CategoryResponse;
import com.ecommerce.Ecom.repositories.CategoryRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {
//    private List<Category> categories = new ArrayList<>();
//    private Long nextId = 1L;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Override
    public CategoryResponse getAllCategories(Integer pageNumber, Integer pageSize,String sortBy,String sortOrder) {
        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageDetails = PageRequest.of(pageNumber, pageSize,sortByAndOrder);
        Page<Category> categoryPage = categoryRepository.findAll(pageDetails);
        List<Category> categories = categoryPage.getContent();
        if (categories.isEmpty()) {
            throw new APIException("No category created till now");

        }
            List<CategoryDto> categoryDtos = categories.stream()
                    .map(category -> modelMapper.map(category, CategoryDto.class))
                    .collect(Collectors.toList());
        CategoryResponse categoryResponse = new CategoryResponse();
        categoryResponse.setContent(categoryDtos);
        categoryResponse.setPageNumber(categoryPage.getNumber());
        categoryResponse.setPageSize(categoryPage.getSize());
        categoryResponse.setTotalElements(categoryPage.getTotalElements());
        categoryResponse.setTotalPages(categoryPage.getTotalPages());
        categoryResponse.setLastPage(categoryPage.isLast());
        return categoryResponse;
    }

    @Override
    public CategoryDto createCategory(CategoryDto categoryDto) {
        Category category = modelMapper.map(categoryDto, Category.class);
        Category categoryFromDb=categoryRepository.findByCategoryName(category.getCategoryName());
        if (categoryFromDb != null ) {
            throw new APIException("Category with the name "+category.getCategoryName()+" already exists");
        }
        Category savedCategory = categoryRepository.save(category);
        CategoryDto savedCategoryDto = modelMapper.map(savedCategory, CategoryDto.class);
        return savedCategoryDto;
    }

    @Override
    public String deleteCategory(Long categoryId) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("Category", "categoryId", categoryId));
        categoryRepository.delete(category);
        return "Category with id"+ categoryId +"deleted successfully";
    }

    @Override
    public CategoryDto updateCategory(CategoryDto categoryDto, Long categoryId) {
        // Recupera entity esistente
        Category savedCategory = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "categoryId", categoryId));

        // Aggiorna solo i campi inviati dal client
        savedCategory.setCategoryName(categoryDto.getCategoryName());
        // se ci sono altri campi aggiornabili, aggiungi qui
        // savedCategory.setOtherField(categoryDto.getOtherField());

        // Salva entity aggiornata
        Category updatedCategory = categoryRepository.save(savedCategory);

        // Ritorna DTO
        return modelMapper.map(updatedCategory, CategoryDto.class);
    }

}
