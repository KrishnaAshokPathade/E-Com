package com.backend.service;

import com.backend.payload.PagableResponce;
import com.backend.payload.ProductDto;

import java.util.List;

public interface ProductService {

    ProductDto createProduct(ProductDto productDto);

    ProductDto updateProduct(ProductDto productDto, String productId);

    List<ProductDto> getAllProduct();

    ProductDto getSingleProduct(String productId);

    List<ProductDto> searchByTitle(String subTitle);

    void delete(String productId);

    PagableResponce<ProductDto> getAllByPageble(int pageNumber, int pageSize, String sortBy, String sortDir);

}
