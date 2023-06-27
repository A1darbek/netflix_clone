package com.ayderbek.netflxSpring.netflix_clone.controller;

import com.ayderbek.netflxSpring.netflix_clone.domain.User;
import com.ayderbek.netflxSpring.netflix_clone.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping
    public List<User> getAllProperties() {
        return userService.getAllUsers();
    }

    @GetMapping("/search")
    public ResponseEntity<List<User>> searchUsers(
            @RequestParam String keyword,
            @RequestParam String email,
            @RequestParam int minPlaylists,
            @RequestParam int minGenres
    ) {
        List<User> users = userService.searchUsers(keyword, email, minPlaylists, minGenres);
        return ResponseEntity.ok(users);
    }
}
