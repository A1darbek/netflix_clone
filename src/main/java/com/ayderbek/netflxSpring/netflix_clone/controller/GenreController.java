package com.ayderbek.netflxSpring.netflix_clone.controller;

import com.ayderbek.netflxSpring.netflix_clone.domain.Genre;
import com.ayderbek.netflxSpring.netflix_clone.service.GenreService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/genres")
@RequiredArgsConstructor
@CrossOrigin("*")
public class GenreController {
    private final GenreService genreService;


    @GetMapping("/search")
    public List<Genre> searchGenres(
            @RequestParam("keyword") String keyword,
            @RequestParam("minUsers") int minUsers,
            @RequestParam("minVideos") int minVideos) {
        return genreService.searchGenres(keyword, minUsers, minVideos);
    }
}
