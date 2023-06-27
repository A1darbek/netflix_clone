package com.ayderbek.netflxSpring.netflix_clone.repos;

import com.ayderbek.netflxSpring.netflix_clone.domain.Genre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface GenreRepository extends JpaRepository<Genre,Long> {
    @Query("SELECT g FROM Genre g WHERE g.name LIKE %:keyword% " +
            "AND (SELECT COUNT(u) FROM User u JOIN u.favoriteGenres fg WHERE fg.id = g.id) >= :minUsers " +
            "AND (SELECT COUNT(v) FROM Video v JOIN v.genres vg WHERE vg.id = g.id) >= :minVideos")
    List<Genre> searchGenres(@Param("keyword") String keyword,
                             @Param("minUsers") int minUsers,
                             @Param("minVideos") int minVideos);

}
