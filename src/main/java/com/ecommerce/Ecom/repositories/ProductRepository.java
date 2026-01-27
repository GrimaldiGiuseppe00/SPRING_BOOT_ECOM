package com.ecommerce.Ecom.repositories;

import com.ecommerce.Ecom.model.Category;
import com.ecommerce.Ecom.model.Product;
import com.ecommerce.Ecom.payload.ProductResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product>findByCategoryOrderByPriceAsc(Category category);
    List<Product> findByProductNameLikeIgnoreCase(String keyword);
}
