package com.ecommerce.Ecom.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.NotBlank;

import java.util.List;


@Entity(name = "categories")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank
    @Size(min = 5, message = "Category name must contain at least 5 characters")
    private String categoryName;
    @OneToMany(mappedBy = "category",cascade = CascadeType.ALL)
    private List<Product> products;

}
