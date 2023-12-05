package com.backend.controller;

import com.backend.payload.CategoryDto;
import com.backend.payload.PagableResponce;
import com.backend.payload.ProductDto;
import com.backend.service.CategorySevice;
import com.backend.service.ProductService;
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
    @Autowired
    private ProductService productService;
    @Autowired
    private CategorySevice categorySevice;

    @PostMapping("/createCategory")
    public ResponseEntity<CategoryDto> createCategory(@Valid @RequestBody CategoryDto categoryDto) {
        CategoryDto category = this.categorySevice.createCategory(categoryDto);
        return new ResponseEntity<CategoryDto>(category, HttpStatus.CREATED);
    }

    @PutMapping("/updateCategory/{categoryId}")
    public ResponseEntity<CategoryDto> updateCategory(@Valid @RequestBody CategoryDto categoryDto, @PathVariable String categoryId) {
        CategoryDto updateCategory = this.categorySevice.updateCategory(categoryDto, categoryId);
        return new ResponseEntity<CategoryDto>(updateCategory, HttpStatus.CREATED);
    }

    @DeleteMapping("/deleteCategory/{categoryId}")
    public ResponseEntity<?> deleteCategory(@PathVariable String categoryId) {
        this.categorySevice.deleteCategory(categoryId);
        return ResponseEntity.ok("Delete Category Successfully");
    }

    @GetMapping("/getAllCategory")
    public ResponseEntity<List<CategoryDto>> getAllCategory() {
        List<CategoryDto> allCategory = this.categorySevice.getAllCategory();
        return new ResponseEntity<List<CategoryDto>>(allCategory, HttpStatus.CREATED);
    }

    @GetMapping("/getAllByPageble")
    public ResponseEntity<PagableResponce<CategoryDto>> getAllByPageble(
            @RequestParam(value = "pageNumber", defaultValue = PAGE_NUMBER, required = false) int pageNumber,
            @RequestParam(value = "pageSize", defaultValue = PAGE_SIZE, required = false) int pageSize,
            @RequestParam(value = "sortDir", defaultValue =SORT_DIR , required = false) String sortDir,
            @RequestParam(value = "sortBy", defaultValue = SORT_BY_TITLE, required = false) String sortBy) {
        PagableResponce<CategoryDto> all = this.categorySevice.getAllByPageble(pageNumber, pageSize, sortBy, sortDir);
        return new ResponseEntity<PagableResponce<CategoryDto>>(all, HttpStatus.OK);
    }

    @PostMapping("/{categoryId}/products")
    public ResponseEntity<ProductDto> createProductWithCategory(@PathVariable String categoryId, @RequestBody ProductDto productDto) {
        ProductDto productWithCategory = this.productService.createWithCategory(productDto, categoryId);

        return new ResponseEntity<ProductDto>(productWithCategory, HttpStatus.CREATED);
    }

    @PutMapping("/{categoryId}/products/{productId}")
    public ResponseEntity<ProductDto> updateCategoryOfProduct(@PathVariable String categoryId, @PathVariable String productId) {
        ProductDto productDto = this.productService.updateCategory(categoryId, productId);
        return new ResponseEntity<ProductDto>(productDto, HttpStatus.OK);
    }
}
