package com.ayderbek.netflxSpring.netflix_clone.controller;

import com.ayderbek.netflxSpring.netflix_clone.domain.Category;
import com.ayderbek.netflxSpring.netflix_clone.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
@CrossOrigin("*")
public class CategoryController {
    private final CategoryService categoryService;

    @GetMapping("/search")
    public ResponseEntity<List<Category>> searchCategories(@RequestParam String keyword,
                                                           @RequestParam String description,
                                                           @RequestParam int minVideos) {
        List<Category> categories = categoryService.searchCategories(keyword, description, minVideos);
        return ResponseEntity.ok(categories);
    }

}
