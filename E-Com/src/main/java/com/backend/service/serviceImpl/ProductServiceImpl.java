package com.backend.service.serviceImpl;

import com.backend.exception.ResourceNotFoundException;
import com.backend.model.Product;
import com.backend.payload.PagableResponce;
import com.backend.payload.ProductDto;
import com.backend.repository.ProductRepo;
import com.backend.service.ProductService;
import com.backend.utitlity.Helper;
import org.modelmapper.ModelMapper;
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
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private ProductRepo productRepo;

    @Value("${product.image}")
    private String imagePath;

    @Override
    public ProductDto createProduct(ProductDto productDto) {
        String productId = UUID.randomUUID().toString();
        productDto.setProductId(productId);

        Product product = this.modelMapper.map(productDto, Product.class);
        Product createProduct = this.productRepo.save(product);
        return this.modelMapper.map(createProduct, ProductDto.class);
    }

    @Override
    public ProductDto updateProduct(ProductDto productDto, String productId) {
        Product product = this.productRepo.findById(productId).orElseThrow(() -> new ResourceNotFoundException("ProductId  Not Found For  Update"));
        product.setProductImageName(productDto.getProductImageName());
        product.setDescription(productDto.getDescription());
        product.setPrice(productDto.getPrice());
        product.setTitle(productDto.getTitle());
        product.setQuantity(productDto.getQuantity());
        product.setAddedDate(productDto.getAddedDate());
        product.setStock(productDto.isStock());
        Product updateProduct = this.productRepo.save(product);
        return this.modelMapper.map(updateProduct, ProductDto.class);
    }

    @Override
    public List<ProductDto> getAllProduct() {
        List<Product> product1 = this.productRepo.findAll();
        List<ProductDto> collect = product1.stream().map(product -> this.modelMapper.map(product, ProductDto.class)).collect(Collectors.toList());
        return collect;
    }

    @Override
    public ProductDto getSingleProduct(String productId) {
        Product product = this.productRepo.findById(productId).orElseThrow(() -> new ResourceNotFoundException("ProductId Not Found for get Single Product"));
        return this.modelMapper.map(product, ProductDto.class);
    }

    @Override
    public List<ProductDto> searchByTitle(String subTitle) {
        List<Product> byTitleContaining = this.productRepo.findByTitleContaining(subTitle);
        List<ProductDto> productDtos = byTitleContaining.stream().map(product -> this.modelMapper.map(product, ProductDto.class)).collect(Collectors.toList());
        return productDtos;
    }

    @Override
    public void delete(String productId) {
        Product product = this.productRepo.findById(productId).orElseThrow(() -> new ResourceNotFoundException("ProductId Not Found"));
        this.productRepo.delete(product);

    }

    @Override
    public PagableResponce<ProductDto> getAllByPageble(int pageNumber, int pageSize, String sortBy, String sortDir) {
        Sort sort = (sortDir.equalsIgnoreCase("desc")) ? (Sort.by(sortBy).descending()) : (Sort.by(sortBy).ascending());
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        Page<Product> products = this.productRepo.findAll(pageable);
        PagableResponce<ProductDto> pagebleResponce = Helper.getPagebleResponce(products, ProductDto.class);

        return pagebleResponce;
    }
}
