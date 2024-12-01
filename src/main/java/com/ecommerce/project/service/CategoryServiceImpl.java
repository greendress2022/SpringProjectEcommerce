package com.ecommerce.project.service;

import com.ecommerce.project.exceptions.APIExceptions;
import com.ecommerce.project.model.Category;
import com.ecommerce.project.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CategoryServiceImpl implements CategoryService{
    @Autowired
    private CategoryRepository categoryRepository;
//    private List<Category> categories = new ArrayList<>(); // no NullPointerException when accessing categories
    private Long nextId = 1L; //use this to keep track of id

    @Override
    public List<Category> getAllCategories() {
        List<Category> all = categoryRepository.findAll();
        if(all.isEmpty()){
            throw new APIExceptions("no category found.");
        }
        return all;
    }

    @Override
    public void createCategory(Category category) {
//        category.setCategoryId(nextId++);
        // assign id automatically
        //add validation, jpa handles the query automatically by the findByCategoryName automatically
        Category savedCategory = categoryRepository.findByCategoryName(category.getCategoryName());
        //if it exists in the db
        if(savedCategory != null){
            throw new APIExceptions("category with name " + category.getCategoryName()+"already exists.");
        }
        categoryRepository.save(category);
    }

    @Override
    public String deleteCategory(Long categoryId) {
//        List<Category> categories = categoryRepository.findAll();
//        //add a check to prevent category not found error
//       Category category = categories.stream()
//               .filter(c -> c.getCategoryId().equals(categoryId))
//               .findFirst().orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "resource not found"));
        Category category = categoryRepository.findById(categoryId).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "not found"));
       categoryRepository.delete(category);
       return "category with id " + categoryId + " deleted";

    }

    @Override
    public Category updateCategory(Category category, Long categoryId) {
        List<Category> categories = categoryRepository.findAll();
        Optional<Category> optionalCategory = categories.stream().filter(c -> c.getCategoryId().equals(categoryId))
                .findFirst(); //.get() might throw an exception if not present
        if(optionalCategory.isPresent()){
            Category existingCategory = optionalCategory.get();
            existingCategory.setCategoryName(category.getCategoryName());
            Category savedCategory = categoryRepository.save(existingCategory);
            //System.out.println("updated category: " + savedCategory); //updated category: Category{categoryId=1, categoryName='xx'}
            return savedCategory;
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "category not found");
        }

    }


}
