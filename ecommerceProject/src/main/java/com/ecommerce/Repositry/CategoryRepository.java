package com.ecommerce.Repositry;


import org.springframework.data.jpa.repository.JpaRepository;

import com.ecommerce.model.Category;

public interface CategoryRepository extends JpaRepository<Category, Integer>{

}
