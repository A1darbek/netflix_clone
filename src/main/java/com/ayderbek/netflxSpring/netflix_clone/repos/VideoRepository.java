package com.ayderbek.netflxSpring.netflix_clone.repos;

import com.ayderbek.netflxSpring.netflix_clone.domain.Category;
import com.ayderbek.netflxSpring.netflix_clone.domain.Video;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface VideoRepository extends JpaRepository<Video,Long> {

    @Query("SELECT v FROM Video v WHERE v.category = :category")
    List<Video> findVideosByCategory(@Param("category") Category category);

    @Query("SELECT v FROM Video v WHERE v.releaseDate > :releaseDate")
    List<Video> findVideosReleasedAfterDate(@Param("releaseDate") LocalDate releaseDate);

    @Query("SELECT v FROM Video v JOIN v.genres g WHERE g.name = :genre")
    List<Video> findByGenre(@Param("genre") String genre);

    @Query("SELECT v FROM Video v WHERE v.releaseDate >= :from AND v.releaseDate <= :to")
    List<Video> findByReleaseDateBetween(@Param("from") LocalDate from, @Param("to") LocalDate to);
}
