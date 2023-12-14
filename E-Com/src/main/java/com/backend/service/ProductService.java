package com.backend.service;

import com.backend.payload.PagableResponce;
import com.backend.payload.ProductDto;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ProductService {

    ProductDto createProduct(ProductDto productDto);

    ProductDto updateProduct(ProductDto productDto, String productId);

    List<ProductDto> getAllProduct();

    ProductDto getSingleProduct(String productId);

    List<ProductDto> searchByTitle(String subTitle);

    ResponseEntity<?> deleteProduct(String productId);

    PagableResponce<ProductDto> getAllByPageble(int pageNumber, int pageSize, String sortBy, String sortDir);


    ProductDto createProductWithCategory(ProductDto productDto, String categoryId);

    ProductDto updateCategory(String productId, String categoryId);



    PagableResponce<ProductDto> getAllOfCategories(String categoryId, int pageSize, int pageNumber, String sortBy, String sortDir);
}
