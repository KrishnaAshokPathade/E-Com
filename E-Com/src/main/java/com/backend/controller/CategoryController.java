package com.backend.controller;

import com.backend.payload.CategoryDto;
import com.backend.payload.PagableResponce;
import com.backend.payload.ProductDto;
import com.backend.service.CategorySevice;
import com.backend.service.ProductService;
import com.backend.service.serviceImpl.FileServiceImpl;
import org.slf4j.Logger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.PrimitiveIterator;

import static com.backend.constants.AppConstant.*;

@RestController
@RequestMapping("/category/")
public class CategoryController {
    private Logger logger = LoggerFactory.getLogger(CategoryController.class);
    @Autowired
    private ProductService productService;
    @Autowired
    private CategorySevice categorySevice;

    @PostMapping("/createCategory")
    public ResponseEntity<CategoryDto> createCategory(@Valid @RequestBody CategoryDto categoryDto) {
        logger.info("Entering the categoryDto details for create Category: {}", categoryDto);
        CategoryDto category = this.categorySevice.createCategory(categoryDto);
        logger.info("Successfully create the Category by passing parameter as categoryDto: {}", category);
        return new ResponseEntity<CategoryDto>(category, HttpStatus.CREATED);
    }

    @PutMapping("/updateCategory/{categoryId}")
    public ResponseEntity<CategoryDto> updateCategory(@Valid @RequestBody CategoryDto categoryDto, @PathVariable String categoryId) {
        logger.info("Update the category with as categoryDto {} and categoryId {}", categoryDto, categoryId);
        CategoryDto updateCategory = this.categorySevice.updateCategory(categoryDto, categoryId);
        logger.info("Successfully update the category: {}", updateCategory);
        return new ResponseEntity<CategoryDto>(updateCategory, HttpStatus.CREATED);
    }

    @DeleteMapping("/deleteCategory/{categoryId}")
    public ResponseEntity<?> deleteCategory(@PathVariable String categoryId) {
        logger.info("Delete the category with   categoryId :{}", categoryId);
        this.categorySevice.deleteCategory(categoryId);
        logger.info("Category with categoryId: {} delete successfully ", categoryId);
        return ResponseEntity.ok("Delete Category Successfully");
    }

    @GetMapping("/getAllCategory")
    public ResponseEntity<List<CategoryDto>> getAllCategory() {
        List<CategoryDto> allCategory = this.categorySevice.getAllCategory();
        logger.info("Successfully Fetching {}  All Categories ", allCategory);
        return new ResponseEntity<List<CategoryDto>>(allCategory, HttpStatus.CREATED);
    }

    @GetMapping("/getAllByPageble")
    public ResponseEntity<PagableResponce<CategoryDto>> getAllByPageble(
            @RequestParam(value = "pageNumber", defaultValue = PAGE_NUMBER, required = false) int pageNumber,
            @RequestParam(value = "pageSize", defaultValue = PAGE_SIZE, required = false) int pageSize,
            @RequestParam(value = "sortDir", defaultValue = SORT_DIR, required = false) String sortDir,
            @RequestParam(value = "sortBy", defaultValue = SORT_BY_TITLE, required = false) String sortBy) {
        logger.info("Fetching {} categories  by pageable parameters");
        logger.info("Page Number: {}", pageNumber);
        logger.info("Page Size: {}", pageSize);
        logger.info("Sort Direction: {}", sortDir);
        logger.info("Sort By: {}", sortBy);
        PagableResponce<CategoryDto> all = this.categorySevice.getAllByPageble(pageNumber, pageSize, sortBy, sortDir);
        logger.info("Fetching {} categories successfully ", all);
        return new ResponseEntity<PagableResponce<CategoryDto>>(all, HttpStatus.OK);
    }

    @PostMapping("/{categoryId}/products")
    public ResponseEntity<ProductDto> createProductWithCategory(@PathVariable String categoryId, @RequestBody ProductDto productDto) {
        logger.info("Create Product with Category: categoryId {},productDto {}", categoryId, productDto);
        ProductDto productWithCategory = this.productService.createWithCategory(productDto, categoryId);
        logger.info("Successfully create the new product with category: {}", productWithCategory);
        return new ResponseEntity<ProductDto>(productWithCategory, HttpStatus.CREATED);
    }

    @PutMapping("/{categoryId}/products/{productId}")
    public ResponseEntity<ProductDto> updateCategoryOfProduct(@PathVariable String categoryId, @PathVariable String productId) {
        logger.info("Update the Category of Product: categoryId and productId :{}", productId, categoryId);
        ProductDto productDto = this.productService.updateCategory(categoryId, productId);
        logger.info("Successfully update the Category of Product {}:", productDto);
        return new ResponseEntity<ProductDto>(productDto, HttpStatus.OK);
    }
}
