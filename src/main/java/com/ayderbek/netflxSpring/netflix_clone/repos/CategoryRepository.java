package com.ayderbek.netflxSpring.netflix_clone.repos;

import com.ayderbek.netflxSpring.netflix_clone.domain.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category,Long> {
    @Query("SELECT c FROM Category c WHERE LOWER(c.name) LIKE CONCAT('%', LOWER(:keyword), '%') " +
            "AND c.description LIKE CONCAT('%', :description, '%') " +
            "AND (SELECT COUNT(v) FROM Video v WHERE v.category.id = c.id) >= :minVideos")
    List<Category> searchCategories(@Param("keyword") String keyword,
                                    @Param("description") String description,
                                    @Param("minVideos") int minVideos);

}
