package com.backend.controller;

import com.backend.model.Product;
import com.backend.payload.ApiResponceMessage;
import com.backend.payload.PagableResponce;
import com.backend.payload.ProductDto;
import com.backend.service.FileService;
import com.backend.service.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@SpringBootTest
public class ProductControllerTest {
    @MockBean
    private FileService fileService;
    @MockBean
    private ProductService productService;
    @Autowired
    private ModelMapper modelMapper;

    private String path = "/images/product/";

    Product product;
    @InjectMocks
    private ProductController productController;

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    public void init() {

        product = Product.builder()
                .productImageName("table")
                .price(2000)
                .description("Wooden table")
                .quantity(30)
                .title("Furniture")
                .discountPrice(100)
                .build();
    }

    @Test
    public void createProductTest() throws Exception {

        ProductDto productDto = this.modelMapper.map(product, ProductDto.class);
        Mockito.when(productService.createProduct(Mockito.any())).thenReturn(productDto);
        mockMvc.perform(MockMvcRequestBuilders.post("/products/createProduct/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(convertObjectToJsonString(product))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());


    }

    private String convertObjectToJsonString(Product product) {
        try {
            return new ObjectMapper().writeValueAsString(product);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Test
    public void updateProductTest() throws Exception {

        String productId = "123";
        ProductDto productDto = this.modelMapper.map(product, ProductDto.class);

        Mockito.when(productService.updateProduct(Mockito.any(), Mockito.anyString())).thenReturn(productDto);

        mockMvc.perform(MockMvcRequestBuilders.put("/products/updateProduct/" + productId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(convertObjectToJsonString(product))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void getAllProductTest() throws Exception {


        Product product1 = Product.builder()
                .productImageName("Chair")
                .price(20002)
                .description("Plastic table")
                .quantity(30)
                .title("Furniture")
                .discountPrice(100)
                .build();

        Product product2 = Product.builder()
                .productImageName("table")
                .price(2000)
                .description("Wooden table")
                .quantity(30)
                .title("Furniture")
                .discountPrice(100)
                .build();

        List<Product> products = Arrays.asList(product2, product1, product);
        List<ProductDto> productDtos = products.stream().map(product -> this.modelMapper.map(products, ProductDto.class)).collect(Collectors.toList());

        Mockito.when(productService.getAllProduct()).thenReturn(productDtos);
        mockMvc.perform(MockMvcRequestBuilders.get("/products/getAllProduct/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void getSingleProductTest() throws Exception {

        String productId = "Krishna";

        ProductDto productDto = this.modelMapper.map(product, ProductDto.class);
        Mockito.when(productService.getSingleProduct(Mockito.anyString())).thenReturn(productDto);
        mockMvc.perform(MockMvcRequestBuilders.get("/products/getSingleProduct/" + productId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
        Assertions.assertEquals(product.getProductImageName(), productDto.getProductImageName());
    }

    @Test
    public void deleteProductTest() throws Exception {
        String productId = "123";
        ApiResponceMessage apiResponceMessage = ApiResponceMessage.builder().message("Product Delete Successfully").success(true).build();
        Mockito.doNothing().when(productService).deleteProduct(productId);
        mockMvc.perform(MockMvcRequestBuilders.delete("/products/deleteProduct/" + productId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Product Delete Successfully"))
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    public void searchByTitleTest() throws Exception {
        Product product1 = Product.builder()
                .productImageName("Chair")
                .price(20002)
                .description("Plastic table")
                .quantity(30)
                .title("Furniture")
                .discountPrice(100)
                .build();

        String searchTitle = "Furniture";
        List<Product> products = Arrays.asList(product1);

        List<ProductDto> productDtos = products.stream().map(product -> this.modelMapper.map(products, ProductDto.class)).collect(Collectors.toList());

        Mockito.when(productService.searchByTitle(Mockito.anyString())).thenReturn(productDtos);

        mockMvc.perform(MockMvcRequestBuilders.get("/products/searchByTitle/" + searchTitle)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void getAllByPagebleTest() throws Exception {
        Product product1 = Product.builder()
                .productImageName("Chair")
                .price(20002)
                .description("Plastic table")
                .quantity(30)
                .title("Furniture")
                .discountPrice(100)
                .build();
        Product product2 = Product.builder()
                .productImageName("Chair")
                .price(20002)
                .description("Plastic table")
                .quantity(30)
                .title("Furniture")
                .discountPrice(100)
                .build();

        List<Product> products = Arrays.asList(product2, product1, product);

        List<ProductDto> productDtos = products.stream().map(product -> this.modelMapper.map(products, ProductDto.class)).collect(Collectors.toList());
        PagableResponce<ProductDto> pagableResponce = new PagableResponce<>();
        Mockito.when(productService.getAllByPageble(Mockito.anyInt(), Mockito.anyInt(), Mockito.anyString(), Mockito.anyString())).thenReturn(pagableResponce);
        mockMvc.perform(MockMvcRequestBuilders.get("/products/getAllByPageble/")
                        .param("pageNumber", "1")
                        .param("pageSize", "10")
                        .param("sortDir", "asc")
                        .param("sortBy", "name")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

    }

    @Test
    public void uploadImageTest() throws Exception {
        String productId = "123";
        MockMultipartFile image = new MockMultipartFile("productImage", "test.jpg", MediaType.IMAGE_JPEG_VALUE, "image data".getBytes());
        String imageName = "test_image.jpg";

        Mockito.when(fileService.uploadFile(image, path)).thenReturn(imageName);

        ProductDto productDto = new ProductDto();
        Mockito.when(productService.getSingleProduct(Mockito.anyString())).thenReturn(productDto);
        Mockito.when(productService.updateProduct(Mockito.any(), Mockito.any())).thenReturn(productDto);

        mockMvc.perform(MockMvcRequestBuilders.multipart("/products/uploadImage/{productId}", productId)
                        .file(image)
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isCreated())
                // .andExpect(jsonPath("$.imageName").value(imageName))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.status").value("Success"))
                .andExpect(jsonPath("$.message").value("Image Upload Successfully"))
                .andReturn();
    }

    @Test
    public void getProductImageTest() throws Exception {

        String productId = "1234";
        String image = "test_image.jpg";

        ProductDto productDto = new ProductDto();
        productDto.setProductImageName(image);

        Mockito.when(productService.getSingleProduct(Mockito.anyString())).thenReturn(productDto);

        byte[] imageData = "Test image data".getBytes();
        InputStream inputStream = new ByteArrayInputStream(imageData);
        Mockito.when(fileService.getResource(path, image)).thenReturn(inputStream);


        mockMvc.perform(MockMvcRequestBuilders.get("/products/image/{productId}", productId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.IMAGE_JPEG_VALUE))
                .andExpect(content().bytes(imageData))
                .andReturn()
                .getResponse();

        assertEquals(MediaType.IMAGE_JPEG_VALUE, content().contentType(MediaType.IMAGE_JPEG));
    }

    @Test
    public void getAllOfCategoriesTest() throws Exception {

        String categoryId = "123";
        Product product1 = Product.builder()
                .productImageName("Chair")
                .price(20002)
                .description("Plastic table")
                .quantity(30)
                .title("Furniture")
                .discountPrice(100)
                .build();
        Product product2 = Product.builder()
                .productImageName("Chair")
                .price(20002)
                .description("Plastic table")
                .quantity(30)
                .title("Furniture")
                .discountPrice(100)
                .build();

        List<Product> products = Arrays.asList(product2, product1, product);

        List<ProductDto> productDtos = products.stream().map(product -> this.modelMapper.map(products, ProductDto.class)).collect(Collectors.toList());
        PagableResponce<ProductDto> pagableResponce = new PagableResponce<>();

        Mockito.when(productService.getAllOfCategories(Mockito.anyString(), Mockito.anyInt(), Mockito.anyInt(), Mockito.anyString(), Mockito.anyString())).thenReturn(pagableResponce);


        mockMvc.perform(MockMvcRequestBuilders.get("/products/getAllOfCategories/{categoryId}/products/",categoryId,categoryId)
                        .param("pageNumber", "1")
                        .param("pageSize", "10")
                        .param("sortDir", "asc")
                        .param("sortBy", "name")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

    }
}
