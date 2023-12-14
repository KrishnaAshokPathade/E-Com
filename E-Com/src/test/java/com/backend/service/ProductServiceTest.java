package com.backend.service;

import com.backend.model.Category;
import com.backend.model.Product;
import com.backend.payload.CategoryDto;
import com.backend.payload.PagableResponce;
import com.backend.payload.ProductDto;
import com.backend.repository.CategoryRepo;
import com.backend.repository.ProductRepo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockingDetails;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.hamcrest.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest
public class ProductServiceTest {
    @Autowired
    private ModelMapper modelMapper;
    @MockBean
    private CategoryRepo categoryRepo;
    @MockBean
    private ProductRepo productRepo;

    @Autowired
    private ProductService productService;

    private String imagePath = "/images/product/";


    Product product;

    @BeforeEach
    public void init() {

        product = Product.builder()
                .productImageName("xyz.jpg")
                .title("Image")
                .discountPrice(12)
                .price(50)
                .quantity(1)
                .build();
    }

    @Test
    public void createProductTest() {

        ProductDto productDto = this.modelMapper.map(product, ProductDto.class);
        Mockito.when(productRepo.save(Mockito.any())).thenReturn(productDto);

        Assertions.assertEquals(productDto.getProductImageName(), product.getProductImageName(), "Image Not Found");
    }

    @Test
    public void updateProductTest() {

        ProductDto productDto = new ProductDto();
        productDto.setProductImageName("xyz.jpg");
        productDto.setPrice(23);
        productDto.setTitle("image");
        productDto.setDiscountPrice(2);
        productDto.setQuantity(23);
        productDto.setPrice(300);

        String productId = "123";

        Mockito.when(productRepo.findById(Mockito.anyString())).thenReturn(Optional.of(product));
        Mockito.when(productRepo.save(Mockito.any())).thenReturn(product);
        ProductDto updateProduct = this.productService.updateProduct(productDto, productId);
        Assertions.assertEquals(updateProduct.getProductImageName(), productDto.getProductImageName(), "Product Not Found");

    }

    @Test
    public void getAllProductTest() {

        Product product1 = Product.builder()
                .productImageName("abc.jpg")
                .title("Image")
                .discountPrice(12)
                .price(50)
                .quantity(1)
                .build();
        Product product2 = Product.builder()
                .productImageName("abc.jpg")
                .title("Image")
                .discountPrice(12)
                .price(50)
                .quantity(1)
                .build();


        List<Product> products = Arrays.asList(product2, product1, product);
        Mockito.doReturn(products).when(productRepo).findAll();

        List<ProductDto> allProduct = productService.getAllProduct();
        for (int i = 0; i < products.size(); i++) {
            Product mockProduct = products.get(i);
            Assertions.assertEquals(this.modelMapper.map(mockProduct, ProductDto.class), allProduct.get(i));
        }
        Assertions.assertEquals(products.size(), allProduct.size());

    }

    @Test
    public void getSingleProduct() {

        String productId = "123";
        Mockito.when(productRepo.findById(Mockito.any())).thenReturn(Optional.of(product));
        ProductDto singleProduct = productService.getSingleProduct(productId);
        Assertions.assertEquals(singleProduct.getProductImageName(), product.getProductImageName(), "Product Not Found");
    }

    @Test
    public void searchByTitleTest() {

        Product product1 = Product.builder()
                .productImageName("abc.jpg")
                .title("Image")
                .discountPrice(12)
                .price(50)
                .quantity(1)
                .build();
        Product product2 = Product.builder()
                .productImageName("abc.jpg")
                .title("Image")
                .discountPrice(12)
                .price(50)
                .quantity(1)
                .build();

        String subTitle = "";

        Mockito.when(productRepo.findByTitleContaining(Mockito.any())).thenReturn(Arrays.asList(product1, product2));
        List<ProductDto> productDtos = productService.searchByTitle(subTitle);
        Assertions.assertEquals(2, productDtos.size(), "Product Not Found");

    }

    @Test
    public void deleteProductTest() {
        String productId = "123";

        Mockito.when(productRepo.findById(Mockito.anyString())).thenReturn(Optional.of(product));

        productService.deleteProduct(productId);

        verify(productRepo, times(1)).delete(product);

    }

    @Test
    public void getAllByPagebleTest() {
        Product product1 = Product.builder()
                .productImageName("abc.jpg")
                .title("Image")
                .discountPrice(12)
                .price(50)
                .quantity(1)
                .build();
        Product product2 = Product.builder()
                .productImageName("abc.jpg")
                .title("Image")
                .discountPrice(12)
                .price(50)
                .quantity(1)
                .build();


        List<Product> productList = Arrays.asList(product1, product2);

        Page page = new PageImpl(productList);

        Mockito.when(productRepo.findAll((Pageable) Mockito.any())).thenReturn(page);

        PagableResponce<ProductDto> pagableResponce = productService.getAllByPageble(1, 1, "title", "asc");

        Assertions.assertEquals(2, pagableResponce.getContent().size());

    }

    @Test
    public void createProductWithCategoryTest() {


        ProductDto productDto = new ProductDto();
        productDto.setProductImageName("Android");
        productDto.setPrice(23);
        productDto.setTitle("image");
        productDto.setDiscountPrice(2);
        productDto.setQuantity(23);
        productDto.setPrice(300);

        Category category = Category.builder()
                .title("Android")
                .coverImage("Android.png")
                .description("Android Developer")
                .build();


        String categoryId = "123";

        Mockito.when(categoryRepo.findById(Mockito.anyString())).thenReturn(Optional.of(category));

        Product product = new Product();
        Mockito.when(productRepo.save(Mockito.any())).thenReturn(product);

        ProductDto productDto1 = productService.createProductWithCategory(productDto, categoryId);
        Assertions.assertEquals(product.getProductId(), productDto1.getProductId());
    }

    @Test
    public void updateCategory() {


        ProductDto productDto = new ProductDto();
        productDto.setProductImageName("Android");
        productDto.setPrice(23);
        productDto.setTitle("image");
        productDto.setDiscountPrice(2);
        productDto.setQuantity(23);
        productDto.setPrice(300);

        Category category = Category.builder()
                .title("Android")
                .coverImage("Android.png")
                .description("Android Developer")
                .build();


        String productId = "1234";
        String categoryId = "123";


        Mockito.when(productRepo.findById(Mockito.anyString())).thenReturn(Optional.of(product));

        Mockito.when(categoryRepo.findById(Mockito.anyString())).thenReturn(Optional.of(category));
        Mockito.when(productRepo.save(Mockito.any())).thenReturn(product);

        Product product1 = new Product();
        ProductDto productDto1 = productService.updateCategory(productId, categoryId);

        Assertions.assertEquals(productDto1.getProductId(), product.getProductId());

    }

    @Test
    public void getAllOfCategoriesTest() {

        ProductDto productDto = new ProductDto();
        productDto.setProductImageName("Mobile");
        productDto.setPrice(23);
        productDto.setTitle("Samsung");
        productDto.setDiscountPrice(2);
        productDto.setQuantity(23);
        productDto.setPrice(300);


        ProductDto productDto1 = new ProductDto();
        productDto.setProductImageName("Laptop");
        productDto.setPrice(23);
        productDto.setTitle("Electronic");
        productDto.setDiscountPrice(2);
        productDto.setQuantity(23);
        productDto.setPrice(300);

        Category category = Category.builder()
                .title("Android")
                .coverImage("Android.png")
                .description("Android Developer")
                .build();

        String categoryId = "123";

        List<ProductDto> productDtosList = Arrays.asList(productDto, productDto1);
        Mockito.when(categoryRepo.findById(Mockito.anyString())).thenReturn(Optional.of(category));

        Page page = new PageImpl(productDtosList);
        Mockito.when(productRepo.findByCategory(Mockito.any(), Mockito.any())).thenReturn(page);


        PagableResponce<ProductDto> pagableResponce = productService.getAllOfCategories("123", 1, 1, "title", "asc");

        Assertions.assertEquals(2, pagableResponce.getContent().size());

    }
}

