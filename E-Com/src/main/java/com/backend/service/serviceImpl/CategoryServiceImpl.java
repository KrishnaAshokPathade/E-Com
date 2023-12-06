package com.backend.service.serviceImpl;

import com.backend.exception.ResourceNotFoundException;
import com.backend.model.Category;
import com.backend.payload.CategoryDto;
import com.backend.payload.PagableResponce;

import com.backend.repository.CategoryRepo;
import com.backend.service.CategorySevice;
import com.backend.utitlity.Helper;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategorySevice {
    private Logger logger = LoggerFactory.getLogger(CategoryServiceImpl.class);

    @Autowired
    private CategoryRepo categoryRepo;
    @Autowired
    private ModelMapper modelMapper;

    @Override
    public CategoryDto createCategory(CategoryDto categoryDto) {
        String categoryId = UUID.randomUUID().toString();
        logger.info("Generate the random categoryId: {}", categoryId);

        categoryDto.setCategoryId(categoryId);
        Category category = this.modelMapper.map(categoryDto, Category.class);
        Category saveCategory = categoryRepo.save(category);

        logger.info("Successfully category created :{}", saveCategory);
        return this.modelMapper.map(saveCategory, CategoryDto.class);
    }

    @Override
    public CategoryDto updateCategory(CategoryDto categoryDto, String categoryId) {
        Category category = this.categoryRepo.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("Category Not Found !!"));
        logger.info("Updating the category :{}", category.getCategoryId());
        category.setTitle(categoryDto.getTitle());
        category.setDescription(categoryDto.getDescription());
        category.setCoverImage(categoryDto.getCoverImage());

        Category updateCategory = this.categoryRepo.save(category);
        logger.info("Update the category :{}", updateCategory);
        return this.modelMapper.map(updateCategory, CategoryDto.class);
    }

    @Override
    public void deleteCategory(String categoryId) {
        Category category = this.categoryRepo.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("CategoryId Not Found !!"));
        logger.info("Delete the category :{}", category.getCategoryId());
        categoryRepo.delete(category);
        logger.info("Delete the category:{}", category.getCategoryId());

    }

    @Override
    public List<CategoryDto> getAllCategory() {
        List<Category> categorys = this.categoryRepo.findAll();
        logger.info("Fetching the all categories :{}", categorys.size());
        List<CategoryDto> categoryDtos = categorys.stream().map(category -> this.modelMapper.map(category, CategoryDto.class)).collect(Collectors.toList());
        logger.info("Fetching {} all the categories :{}", categoryDtos.size());
        return categoryDtos;
    }

    @Override
    public CategoryDto getSingleCategory(String categoryId) {
        Category category = this.categoryRepo.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("CategoryId npot Found !!"));
        logger.info("Fetching Single category with categoryId :{}", category.getCategoryId());
        CategoryDto categoryDto = this.modelMapper.map(category, CategoryDto.class);
        logger.info("Fetch the single category :{}", categoryDto);
        return categoryDto;
    }

    @Override
    public PagableResponce<CategoryDto> getAllByPageble(int pageNumber, int pageSize, String sortBy, String sortDir) {
        Sort sort = (sortDir.equalsIgnoreCase("desc")) ? (Sort.by(sortBy).descending()) : (Sort.by(sortBy).ascending());
        //pageNumber default start from 0
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);

        Page<Category> page = this.categoryRepo.findAll(pageable);
        logger.info("Fetching the  All Page :{} ", page.getTotalPages());
        PagableResponce<CategoryDto> responce = Helper.getPagebleResponce(page, CategoryDto.class);
        return responce;
    }

}
