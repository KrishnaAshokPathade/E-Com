package com.backend.repository;

import com.backend.model.Product;
import com.backend.payload.CategoryDto;
import com.backend.payload.PagableResponce;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepo extends JpaRepository<Product, String> {

    List<Product> findByTitleContaining(String subTitle);

}
