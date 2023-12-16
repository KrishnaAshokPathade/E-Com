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


    /**
     * Create the Category providing Category specific details
     * Generate the random categoryId.
     *
     * @param categoryDto
     * @return http status for save data
     * @apiNote This Api is used to create new category in databased
     */
    @Override
    public CategoryDto createCategory(CategoryDto categoryDto) {
        String categoryId = UUID.randomUUID().toString();
        logger.info("Generate the random categoryId: {}", categoryId);

        categoryDto.setCategoryId(categoryId);
        Category category = this.modelMapper.map(categoryDto, Category.class);
        Category saveCategory = categoryRepo.save(category);

        logger.info("New category created :{}", saveCategory);
        return this.modelMapper.map(saveCategory, CategoryDto.class);
    }


    /**
     * Update the Category by providing the category parameter and categoryId
     *
     * @param categoryDto
     * @param categoryId
     * @return categoryDto
     * @apiNote This Api is used to update Category data with id in  database
     */
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


    /**
     * Delete the Category by providing the categoryId
     * *@param categoryId  provide the unique categoryId for delete user.
     *
     * @apiNote This Api is used to delete Category data with categoryId in  database
     */
    @Override
    public void deleteCategory(String categoryId) {
        Category category = this.categoryRepo.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("CategoryId Not Found !!"));
        logger.info("Delete the category :{}", category.getCategoryId());
        categoryRepo.delete(category);
        logger.info("Delete the category:{}", category.getCategoryId());

    }


    /**
     * Retrive All Category.
     *
     * @return List<CategoryDto>
     * @apiNote To get all Category data from database
     */

    @Override
    public List<CategoryDto> getAllCategory() {
        List<Category> categorys = this.categoryRepo.findAll();
        logger.info("Fetching the all categories :{}", categorys.size());
        List<CategoryDto> categoryDtos = categorys.stream().map(category -> this.modelMapper.map(category, CategoryDto.class)).collect(Collectors.toList());
        logger.info("Fetching {} all the categories :{}", categoryDtos.size());
        return categoryDtos;
    }


    /**
     * Retrieve the Category by provide categoryId.
     *
     * @param categoryId
     * @return CategoryDto http status for get single data from database
     * @apiNote To get single Category data from database using categoryId
     */
    @Override
    public CategoryDto getSingleCategory(String categoryId) {
        Category category = this.categoryRepo.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("CategoryId npot Found !!"));
        logger.info("Fetching Single category with categoryId :{}", category.getCategoryId());
        CategoryDto categoryDto = this.modelMapper.map(category, CategoryDto.class);
        logger.info("Fetch the single category :{}", categoryDto);
        return categoryDto;
    }

    /**
     * Retrive the PagableResponce by providing the spicific parameter
     * Retrive All the data of Category.
     *
     * @param pageNumber
     * @param pageSize
     * @param sortBy
     * @param sortDir
     * @return http status for getting data
     * @apiNote To get all user data from database
     */
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
