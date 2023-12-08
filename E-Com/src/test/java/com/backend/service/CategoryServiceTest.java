package com.backend.service;

import com.backend.model.Category;
import com.backend.model.User;
import com.backend.payload.CategoryDto;
import com.backend.payload.PagableResponce;
import com.backend.payload.UserDto;
import com.backend.repository.CategoryRepo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.modelmapper.internal.util.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;


import java.util.Arrays;
import java.util.List;
import java.util.Optional;


import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;


@SpringBootTest
public class CategoryServiceTest {
    @Autowired
    private CategorySevice categorySevice;
    @Autowired
    private ModelMapper modelMapper;
    @MockBean
    private CategoryRepo categoryRepo;

    Category category;

    @BeforeEach
    public void init() {
        category = Category.builder()
                .title("Java")
                .coverImage("Java.png")
                .description("Java Developer")
                .build();
    }

    @Test
    public void createCategoryTest() {

        Mockito.when(categoryRepo.save(Mockito.any())).thenReturn(category);
        CategoryDto categoryDto = categorySevice.createCategory(modelMapper.map(category, CategoryDto.class));
        Assert.notNull(categoryDto);
        Assertions.assertEquals("Java", categoryDto.getTitle(), "Category Not Found");
    }

    @Test
    public void updateCategoryTest() {

        String categoryId = "";
        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setTitle("Python");
        categoryDto.setCoverImage("python.png");
        categoryDto.setDescription("Python Developer");

        Mockito.when(categoryRepo.findById(Mockito.anyString())).thenReturn(Optional.of(category));
        Mockito.when(categoryRepo.save(Mockito.any())).thenReturn(category);
        CategoryDto updateCategory = categorySevice.updateCategory(categoryDto, categoryId);
        Assertions.assertNotNull(updateCategory);
        Assertions.assertEquals(categoryDto.getDescription(), updateCategory.getDescription(), "Category Not Found");
    }

    @Test
    public void deleteCategoryTest() {
        String categoryId = "Krishna123";
        Mockito.when(categoryRepo.findById(categoryId)).thenReturn(Optional.of(category));
        categorySevice.deleteCategory(categoryId);
        verify(categoryRepo, times(1)).delete(category);
    }

    @Test
    public void getAllCategoryTest() {
        Category category1 = Category.builder()
                .title("c#")
                .coverImage("c.png")
                .description("C# Developer")
                .build();
        Category category2 = Category.builder()
                .title("C++")
                .coverImage("C++.png")
                .description("C++ Developer")
                .build();
        Category category3 = Category.builder()
                .title("Ruby")
                .coverImage("Ruby.png")
                .description("Ruby Developer")
                .build();
        Category category4 = Category.builder()
                .title("Android")
                .coverImage("Android.png")
                .description("Android Developer")
                .build();


        List<Category> categoryList = Arrays.asList(category, category2, category1, category3, category4);

        Mockito.doReturn(categoryList).when(categoryRepo).findAll();
        List<CategoryDto> allCategory = categorySevice.getAllCategory();
        for (int i = 0; i < categoryList.size(); i++) {
            Category mockCategory = categoryList.get(i);
            Assertions.assertEquals(modelMapper.map(mockCategory, CategoryDto.class), allCategory.get(i));
        }
        Assertions.assertEquals(categoryList.size(), allCategory.size());

    }

    @Test
    public void getSingleCategoryTest() {
        String categoryId = "Krishna123";
        Mockito.when(categoryRepo.findById(categoryId)).thenReturn(Optional.of(category));

        CategoryDto categoryDto = categorySevice.getSingleCategory(categoryId);
        Assertions.assertNotNull(categoryDto);
        Assertions.assertEquals(category.getCoverImage(), categoryDto.getCoverImage(), "Category Not Found");

    }

    @Test
    public void getAllByPageble() {
        Category category1 = Category.builder()
                .title("c#")
                .coverImage("c.png")
                .description("C# Developer")
                .build();
        Category category2 = Category.builder()
                .title("C++")
                .coverImage("C++.png")
                .description("C++ Developer")
                .build();
        Category category3 = Category.builder()
                .title("Ruby")
                .coverImage("Ruby.png")
                .description("Ruby Developer")
                .build();
        Category category4 = Category.builder()
                .title("Android")
                .coverImage("Android.png")
                .description("Android Developer")
                .build();

        List<Category> categoryList = Arrays.asList(category, category2, category1, category3, category4);
        Page<Category> page = new PageImpl<>(categoryList);
        Mockito.when(categoryRepo.findAll((Pageable) Mockito.any())).thenReturn(page);

        PagableResponce<CategoryDto> pagableResponce = categorySevice.getAllByPageble(1,1,"title","asc");
        Assertions.assertEquals(5, pagableResponce.getContent().size());
    }

}




