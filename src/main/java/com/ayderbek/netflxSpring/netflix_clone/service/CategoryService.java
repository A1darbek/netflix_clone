package com.ayderbek.netflxSpring.netflix_clone.service;

import com.amazonaws.services.alexaforbusiness.model.NotFoundException;
import com.ayderbek.netflxSpring.netflix_clone.domain.Category;
import com.ayderbek.netflxSpring.netflix_clone.domain.Video;
import com.ayderbek.netflxSpring.netflix_clone.repos.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public List<Category> searchCategories(String keyword, String description, int minVideos) {
        return categoryRepository.searchCategories(keyword, description, minVideos);
    }
    public Category getById(Long categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new NotFoundException("Category not found with id " + categoryId));
    }
}
