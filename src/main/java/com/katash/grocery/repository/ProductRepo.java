package com.katash.grocery.repository;

import com.katash.grocery.model.Products;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepo extends JpaRepository<Products, Integer> {
}
