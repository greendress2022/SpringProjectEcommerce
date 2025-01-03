package com.ecommerce.project.service;

import com.ecommerce.project.exceptions.APIExceptions;
import com.ecommerce.project.model.Category;
import com.ecommerce.project.payload.CategoryDTO;
import com.ecommerce.project.payload.CategoryResponse;
import com.ecommerce.project.repository.CategoryRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;
import java.util.Optional;

@Service
public class CategoryServiceImpl implements CategoryService{
    @Autowired
    private CategoryRepository categoryRepository;
//    private List<Category> categories = new ArrayList<>(); // no NullPointerException when accessing categories
 //   private Long nextId = 1L; //use this to keep track of id
    @Autowired
    private ModelMapper modelMapper;

    @Override
    public CategoryResponse getAllCategories() {
        List<Category> all = categoryRepository.findAll();
        //validate the response from the db
        if(all.isEmpty()){
            throw new APIExceptions("no category found.");
        }
        //convert to DTO
        List<CategoryDTO> categoryDTOS = all.stream().map(category -> modelMapper.map(category, CategoryDTO.class)).toList();
        CategoryResponse categoryResponse = new CategoryResponse();
        //set content part of the response
        categoryResponse.setContent(categoryDTOS);
        return categoryResponse ;
    }

    @Override
    public CategoryDTO createCategory(CategoryDTO categoryDTO) {
//        category.setCategoryId(nextId++);
        // assign id automatically
        Category category = modelMapper.map(categoryDTO,Category.class);
        //add validation, jpa handles the query automatically by the findByCategoryName automatically
        Category categoryFromDb = categoryRepository.findByCategoryName(category.getCategoryName());
        //if it exists in the db
        if(categoryFromDb != null){
            throw new APIExceptions("category with name " + category.getCategoryName()+"already exists.");
        }
        Category savedCategory = categoryRepository.save(category);
        CategoryDTO savedCategoryDTO = modelMapper.map(savedCategory,CategoryDTO.class);
        return savedCategoryDTO;
    }

    @Override
    public CategoryDTO deleteCategory(Long categoryId) {
//        List<Category> categories = categoryRepository.findAll();
//        //add a check to prevent category not found error
//       Category category = categories.stream()
//               .filter(c -> c.getCategoryId().equals(categoryId))
//               .findFirst().orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "resource not found"));
        Category category = categoryRepository.findById(categoryId).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "not found"));
        categoryRepository.delete(category);
//       return "category with id " + categoryId + " deleted";
        return modelMapper.map(category, CategoryDTO.class);

    }

    @Override
    public CategoryDTO updateCategory(CategoryDTO categoryDTO, Long categoryId) {
        Category category = modelMapper.map(categoryDTO, Category.class);
        List<Category> categories = categoryRepository.findAll();
        Optional<Category> optionalCategory = categories.stream().filter(c -> c.getCategoryId().equals(categoryId))
                .findFirst(); //.get() might throw an exception if not present
        if(optionalCategory.isPresent()){
            Category existingCategory = optionalCategory.get();
            existingCategory.setCategoryName(category.getCategoryName());
            Category savedCategory = categoryRepository.save(existingCategory);
            //System.out.println("updated category: " + savedCategory); //updated category: Category{categoryId=1, categoryName='xx'}
            return modelMapper.map(savedCategory, CategoryDTO.class);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "category not found");
        }

    }


}
