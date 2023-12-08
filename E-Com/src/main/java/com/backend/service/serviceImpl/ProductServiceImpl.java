package com.backend.service.serviceImpl;

import com.backend.exception.ResourceNotFoundException;
import com.backend.model.Category;
import com.backend.model.Product;
import com.backend.payload.PagableResponce;
import com.backend.payload.ProductDto;
import com.backend.repository.CategoryRepo;
import com.backend.repository.ProductRepo;
import com.backend.service.ProductService;
import com.backend.utitlity.Helper;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {
    private Logger logger = LoggerFactory.getLogger(ProductServiceImpl.class);
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private CategoryRepo categoryRepo;
    @Autowired
    private ProductRepo productRepo;

    @Value("${product.image}")
    private String imagePath;

    @Override
    public ProductDto createProduct(ProductDto productDto) {
        String productId = UUID.randomUUID().toString();
        logger.info("Generate the random productId:{}", productId);
        productDto.setProductId(productId);
        Product product = this.modelMapper.map(productDto, Product.class);
        Product createProduct = this.productRepo.save(product);
        logger.info("New Product Created: {}", createProduct);
        return this.modelMapper.map(createProduct, ProductDto.class);
    }

    @Override
    public ProductDto updateProduct(ProductDto productDto, String productId) {
        Product product = this.productRepo.findById(productId).orElseThrow(() -> new ResourceNotFoundException("ProductId  Not Found For  Update"));
        logger.info("Updating the Product :{}", product.getProductId());
        product.setProductImageName(productDto.getProductImageName());
        product.setDescription(productDto.getDescription());
        product.setPrice(productDto.getPrice());
        product.setTitle(productDto.getTitle());
        product.setQuantity(productDto.getQuantity());
        product.setAddedDate(productDto.getAddedDate());
        product.setStock(productDto.isStock());
        Product updateProduct = this.productRepo.save(product);
        logger.info("Update Product:{}", updateProduct);
        return this.modelMapper.map(updateProduct, ProductDto.class);
    }

    @Override
    public List<ProductDto> getAllProduct() {
        List<Product> product1 = this.productRepo.findAll();
        logger.info("Fetching All Product :{}", product1.size());
        List<ProductDto> collect = product1.stream().map(product -> this.modelMapper.map(product, ProductDto.class)).collect(Collectors.toList());
        logger.info("Fetching All Product :{}", collect.size());
        return collect;
    }

    @Override
    public ProductDto getSingleProduct(String productId) {
        Product product = this.productRepo.findById(productId).orElseThrow(() -> new ResourceNotFoundException("ProductId Not Found for get Single Product"));
        logger.info("Fetching the Single Product:{}", product.getProductId());
        return this.modelMapper.map(product, ProductDto.class);
    }

    @Override
    public List<ProductDto> searchByTitle(String subTitle) {
        List<Product> list = this.productRepo.findByTitleContaining(subTitle);
        List<ProductDto> productDtos = list.stream().map(product -> this.modelMapper.map(product, ProductDto.class)).collect(Collectors.toList());
        logger.info("Fetching All Product :{}", productDtos);
        return productDtos;
    }

    @Override
    public void delete(String productId) {
        Product product = this.productRepo.findById(productId).orElseThrow(() -> new ResourceNotFoundException("ProductId Not Found"));
        logger.info("Delete the Product:{}", product.getProductId());
        this.productRepo.delete(product);

    }

    @Override
    public PagableResponce<ProductDto> getAllByPageble(int pageNumber, int pageSize, String sortBy, String sortDir) {
        Sort sort = (sortDir.equalsIgnoreCase("desc")) ? (Sort.by(sortBy).descending()) : (Sort.by(sortBy).ascending());
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        Page<Product> products = this.productRepo.findAll(pageable);
        logger.info("Fetching the  All Page :{} ", products.getTotalPages());
        PagableResponce<ProductDto> pagebleResponce = Helper.getPagebleResponce(products, ProductDto.class);
        logger.info("Fetching  PagableResponce<ProductDto> :{}", pagebleResponce);
        return pagebleResponce;
    }

    @Override
    public ProductDto createWithCategory(ProductDto productDto, String categoryId) {
        Category category = this.categoryRepo.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("CategoryId Not Found"));
        logger.info("Fetching the category :{}", category);
        Product product = modelMapper.map(productDto, Product.class);
        String productId = UUID.randomUUID().toString();
        logger.info("Generate random ProductId :{}", productId);
        product.setProductId(productId);
        product.setCategory(category);
        Product saveUpdate = productRepo.save(product);
        ProductDto dto = this.modelMapper.map(saveUpdate, ProductDto.class);
        logger.info("Create Product with Category ,{}", dto);
        return dto;
    }

    @Override
    public ProductDto updateCategory(String productId, String categoryId) {
        Product product = this.productRepo.findById(productId).orElseThrow(() -> new ResourceNotFoundException("ProductId not Found for updateCategory"));
        logger.info("Update the Product with category :{}", product.getProductId());
        Category category = this.categoryRepo.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("CategoryId not Found for updateCategory !!"));
        logger.info("Update the Product with category :{}", category.getCategoryId());
        product.setCategory(category);
        Product saveProduct = this.productRepo.save(product);
        logger.info("Update the product :{}", saveProduct);
        return modelMapper.map(product, ProductDto.class);
    }
}
