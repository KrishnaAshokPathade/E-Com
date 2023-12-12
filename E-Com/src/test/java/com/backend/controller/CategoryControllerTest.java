package com.backend.controller;

import com.backend.model.Category;
import com.backend.payload.CategoryDto;
import com.backend.payload.PagableResponce;
import com.backend.payload.ProductDto;
import com.backend.service.CategorySevice;
import com.backend.service.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@SpringBootTest
@AutoConfigureMockMvc
public class CategoryControllerTest {
    @MockBean
    private ProductService productService;

    @MockBean
    private CategorySevice categorySevice;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private MockMvc mockMvc;

    Category category;

    @BeforeEach
    public void init() {

        category = Category.builder()
                .title("Java Developer")
                .description("Java Developer with 2 years of Exprience")
                .coverImage("java.jpg")
                .build();
    }

    @Test
    public void createCategoryTest() throws Exception {

        CategoryDto categoryDto = modelMapper.map(category, CategoryDto.class);
//
        Mockito.when(categorySevice.createCategory(any())).thenReturn(categoryDto);

        mockMvc.perform(MockMvcRequestBuilders.post("/category/createCategory/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(convertObjectToJsonString(category))
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("Java Developer"));


    }

    private String convertObjectToJsonString(Object category) {
        try {
            return new ObjectMapper().writeValueAsString(category);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Test
    public void updateCategory() throws Exception {

        Category category1 = Category.builder()
                .description("Python")
                .coverImage("python.jpg")
                .title("python")
                .build();

        String categoryId = "Krishna";

        CategoryDto categoryDto = modelMapper.map(category1, CategoryDto.class);
        Mockito.when(categorySevice.updateCategory(Mockito.any(), Mockito.any())).thenReturn(categoryDto);

        mockMvc.perform(MockMvcRequestBuilders.put("/category/updateCategory/" + categoryId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(convertObjectToJsonString(category))
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").exists());
    }

    @Test
    public void deleteCategoryTest() throws Exception {
        Category category1 = Category.builder()
                .description("Python")
                .coverImage("python.jpg")
                .title("python")
                .build();
        String categoryId = "Krishna";
        Mockito.doNothing().when(categorySevice).deleteCategory(categoryId);
        mockMvc.perform(MockMvcRequestBuilders.delete("/category/deleteCategory/" + categoryId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.TEXT_PLAIN_VALUE + ";charset=UTF-8"))
                .andExpect(content().string("Delete Category Successfully"));

    }

    @Test
    public void getAllCategoryTest() throws Exception {
        Category category1 = Category.builder()
                .description("Python")
                .coverImage("python.jpg")
                .title("python")
                .build();

        Category category2 = Category.builder()
                .description("Python")
                .coverImage("python.jpg")
                .title("python")
                .build();


        List<Category> categories = Arrays.asList(category1, category2);
        List<CategoryDto> categoryDtos = categories.stream().map(category -> this.modelMapper.map(categories, CategoryDto.class)).collect(Collectors.toList());

        Mockito.when(categorySevice.getAllCategory()).thenReturn(categoryDtos);

        mockMvc.perform(MockMvcRequestBuilders.get("/category/getAllCategory")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andDo(print());
    }

    @Test
    public void getAllByPagebleTest() throws Exception {
        Category category1 = Category.builder()
                .description("Python")
                .coverImage("python.jpg")
                .title("python")
                .build();

        Category category2 = Category.builder()
                .description("Python")
                .coverImage("python.jpg")
                .title("python")
                .build();

        List<Category> categories = Arrays.asList(category1, category2);
        List<CategoryDto> categoryDtos = categories.stream().map(category -> this.modelMapper.map(categories, CategoryDto.class)).collect(Collectors.toList());

        PagableResponce<CategoryDto> pagableResponce = new PagableResponce();
        Mockito.when(categorySevice.getAllByPageble(anyInt(), anyInt(), anyString(), anyString())).thenReturn(pagableResponce);

        // Perform the GET request
        mockMvc.perform(MockMvcRequestBuilders.get("/category/getAllByPageble/")
                        .param("pageNumber", "1")
                        .param("pageSize", "10")
                        .param("sortDir", "asc")
                        .param("sortBy", "name")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void createProductWithCategoryTest() throws Exception {

        ProductDto productDto = new ProductDto();
        //   productDto.setProductId("1112");
        productDto.setProductImageName("abc.jpg");
        productDto.setPrice(111);
        productDto.setDescription("Coding Round");
        productDto.setTitle("Java Developer");
        productDto.setDiscountPrice(200);

        String categoryId = "Krishna";

        Mockito.when(productService.createWithCategory(any(ProductDto.class), eq(categoryId))).thenReturn(productDto);
        mockMvc.perform(MockMvcRequestBuilders.put("/category/{categoryId}/products/" + categoryId, categoryId)
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isOk());

    }

    @Test
    public void updateCategoryOfProductTest() throws Exception {


        ProductDto productDto = new ProductDto();
        //   productDto.setProductId("1112");
        productDto.setProductImageName("abc.jpg");
        productDto.setPrice(111);
        productDto.setDescription("Coding Round");
        productDto.setTitle("Java Developer");
        productDto.setDiscountPrice(200);

        String categoryId = "Krishna";

        String productId = "123";

        Mockito.when(productService.updateCategory(Mockito.anyString(), Mockito.anyString())).thenReturn(productDto);

        mockMvc.perform(MockMvcRequestBuilders.put("/category/{categoryId}/products/"+productId,categoryId)
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isOk());


    }

}
