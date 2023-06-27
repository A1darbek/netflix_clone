package com.ayderbek.netflxSpring.netflix_clone.repos;

import com.ayderbek.netflxSpring.netflix_clone.domain.PlayList;
import com.ayderbek.netflxSpring.netflix_clone.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface PlayListRepository extends JpaRepository<PlayList,Long> {
    @Query("SELECT p FROM PlayList p WHERE p.user = :user")
    List<PlayList> findPlaylistsByUser(@Param("user") User user);

    @Query("SELECT pl FROM PlayList pl " +
            "JOIN pl.videos v " +
            "JOIN v.genres g " +
            "WHERE g.name = :genreName " +
            "AND pl.user = :user " +
            "ORDER BY pl.name ASC")
    List<PlayList> findPlaylistsByGenreAndUserOrderByPlaylistName(@Param("genreName") String genreName, @Param("user") User user);

    @Transactional
    @Modifying
    @Query("DELETE FROM PlayList p WHERE p.id = :playlistId AND p.user = :user")
    void deleteByUserAndId(@Param("user") User user, @Param("playlistId") Long playlistId);
}
