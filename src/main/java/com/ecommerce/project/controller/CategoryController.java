package com.ecommerce.project.controller;

import com.ecommerce.project.model.Category;
import com.ecommerce.project.payload.CategoryResponse;
import com.ecommerce.project.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api")
public class CategoryController {
    @Autowired
     private CategoryService categoryService;

    // map to an endpoint
//    @GetMapping("/api/public/categories")
    @RequestMapping(value ="/public/categories" , method = RequestMethod.GET)
    public ResponseEntity<CategoryResponse> getAllCategories(){
        CategoryResponse categoryResponse = categoryService.getAllCategories();
        return new ResponseEntity<>(categoryResponse, HttpStatus.OK);
    }

    @RequestMapping(value ="/public/categories" , method = RequestMethod.POST)
    public ResponseEntity<String> createCategory(@Valid @RequestBody Category category){
        categoryService.createCategory(category);
        return new ResponseEntity<>("added successfully", HttpStatus.CREATED);
    }

    @RequestMapping(value ="/admin/categories/{categoryId}" , method = RequestMethod.DELETE)
    public ResponseEntity<String> deleteCategory(@PathVariable Long categoryId){
//        try{
//            String status = categoryService.deleteCategory(categoryId);
//            return new ResponseEntity<>(status, HttpStatus.OK);
//        } catch(ResponseStatusException e){
//            return new ResponseEntity<>(e.getReason(), e.getStatusCode());
//        }
        //no need to handle validation in controllers since validation is done by validator in the service
        String status = categoryService.deleteCategory(categoryId);
        return new ResponseEntity<>(status, HttpStatus.OK);
    }
    @RequestMapping(value="/admin/categories/{categoryId}", method=RequestMethod.PUT)
    public ResponseEntity<String> updateCategory(@RequestBody Category category, @PathVariable Long categoryId){
//        try{
//            Category updatedCategory = categoryService.updateCategory(category, categoryId);
//            return new ResponseEntity<>("updated successfully, the category id is: " + categoryId, HttpStatus.ACCEPTED);
//        }catch(ResponseStatusException e){
//            return new ResponseEntity<>(e.getReason(), e.getStatusCode());
//
//        }
        Category updatedCategory = categoryService.updateCategory(category, categoryId);
        return new ResponseEntity<>("updated Category with id: " + categoryId, HttpStatus.ACCEPTED);
    }
}
