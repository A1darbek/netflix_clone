package com.ayderbek.netflxSpring.netflix_clone.repos;

import com.ayderbek.netflxSpring.netflix_clone.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {
    @Query("SELECT u FROM User u WHERE u.name LIKE %:keyword% " +
            "AND u.email LIKE %:email% " +
            "AND (SELECT COUNT(p) FROM PlayList p WHERE p.user.id = u.id) >= :minPlaylists " +
            "AND (SELECT COUNT(g) FROM Genre g JOIN g.users gu WHERE gu.id = u.id) >= :minGenres")
    List<User> searchUsers(@Param("keyword") String keyword,
                           @Param("email") String email,
                           @Param("minPlaylists") int minPlaylists,
                           @Param("minGenres") int minGenres);

    Optional<User> findByAuth0Id(String auth0Id);
}
