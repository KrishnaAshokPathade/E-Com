package com.backend.service;

import com.backend.payload.CategoryDto;
import com.backend.payload.PagableResponce;

import java.util.List;

public interface CategorySevice {
    CategoryDto createCategory(CategoryDto categoryDto);
    CategoryDto updateCategory(CategoryDto categoryDto,String categoryId);
    void deleteCategory(String categoryId);
    List<CategoryDto> getAllCategory();
    CategoryDto getSingleCategory(String categoryId);

    PagableResponce<CategoryDto> getAllByPageble(int pageNumber, int pageSize, String sortBy, String sortDir);

}
