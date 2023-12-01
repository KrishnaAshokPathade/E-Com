package com.backend.controller;

import com.backend.payload.*;
import com.backend.service.FileService;
import com.backend.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/products/")
public class ProductController {
    @Autowired
    private FileService fileService;
    @Autowired
    private ProductService productService;
    @Value("${product.image}")
    private String imagePath;

    @PostMapping("/createProduct")
    public ResponseEntity<ProductDto> createProduct(@Valid @RequestBody ProductDto productDto) {
        ProductDto product = this.productService.createProduct(productDto);
        return new ResponseEntity<ProductDto>(product, HttpStatus.CREATED);
    }

    @PutMapping("/updateProduct/{productId}")
    public ResponseEntity<ProductDto> updateProduct(@Valid @RequestBody ProductDto productDto, @PathVariable String productId) {
        ProductDto updateProduct = this.productService.updateProduct(productDto, productId);
        return new ResponseEntity<ProductDto>(updateProduct, HttpStatus.OK);
    }

    @GetMapping("/getAllProduct")
    public ResponseEntity<List<ProductDto>> getAllProduct() {
        List<ProductDto> allProduct = this.productService.getAllProduct();
        return new ResponseEntity<List<ProductDto>>(allProduct, HttpStatus.OK);
    }

    @GetMapping("/getSingleProduct/{productId}")
    public ResponseEntity<ProductDto> getSingleProduct(@PathVariable String productId) {
        ProductDto singleProduct = this.productService.getSingleProduct(productId);
        return new ResponseEntity<ProductDto>(singleProduct, HttpStatus.OK);
    }

    @DeleteMapping("/deleteProduct/{productId}")
    public ResponseEntity<ApiResponceMessage> deleteProduct(@PathVariable String productId) {
        this.productService.delete(productId);
        ApiResponceMessage apiResponceMessage = ApiResponceMessage.builder().message("Product Delete Succefully").success(true).build();
        return new ResponseEntity<>(apiResponceMessage, HttpStatus.OK);
    }

    @GetMapping("/searchByTitle/{subTitle}")
    public ResponseEntity<List<ProductDto>> searchByTitle(@PathVariable String subTitle) {
        List<ProductDto> productDtos = this.productService.searchByTitle(subTitle);
        return new ResponseEntity<List<ProductDto>>(productDtos, HttpStatus.OK);
    }

    @GetMapping("/getAllByPageble")
    public ResponseEntity<PagableResponce<ProductDto>> getAllByPageble(
            @RequestParam(value = "pageNumber", defaultValue = "0", required = false) int pageNumber,
            @RequestParam(value = "pageSize", defaultValue = "50", required = false) int pageSize,
            @RequestParam(value = "sortDir", defaultValue = "asc", required = false) String sortDir,
            @RequestParam(value = "sortBy", defaultValue = "title", required = false) String sortBy) {
        PagableResponce<ProductDto> all = this.productService.getAllByPageble(pageNumber, pageSize, sortBy, sortDir);
        return new ResponseEntity<PagableResponce<ProductDto>>(all, HttpStatus.OK);
    }

    @PostMapping("/uploadImage/{productId}")
    public ResponseEntity<ImageResponce> uploadImage(@RequestParam MultipartFile image, @PathVariable String productId) throws IOException {
        ProductDto singleProduct = this.productService.getSingleProduct(productId);
        String uploadImage = this.fileService.uploadFile(image, imagePath);
        singleProduct.setProductImageName(uploadImage);
        ProductDto productDto = this.productService.updateProduct(singleProduct, productId);
        ImageResponce imageResponce = ImageResponce.builder().imageName(uploadImage).status("Success").success(true).message("Image Upload Successfully").build();
        return new ResponseEntity<ImageResponce>(imageResponce, HttpStatus.CREATED);
    }

    @GetMapping("/image/{productId}")
    public ResponseEntity<?> serveProductImage(@PathVariable String productId, HttpServletResponse responce) throws Exception {
        ProductDto productDto = productService.getSingleProduct(productId);
        InputStream resource = fileService.getResource(imagePath, productDto.getProductImageName());
        responce.setContentType(MediaType.IMAGE_JPEG_VALUE);
        StreamUtils.copy(resource, responce.getOutputStream());
        return ResponseEntity.ok("Get Product Image Successfully");
    }
}
