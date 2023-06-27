package com.ayderbek.netflxSpring.netflix_clone.service;

import com.ayderbek.netflxSpring.netflix_clone.domain.Genre;
import com.ayderbek.netflxSpring.netflix_clone.repos.GenreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GenreService {
    private final GenreRepository genreRepository;


    public List<Genre> searchGenres(String keyword, int minUsers, int minVideos) {
        return genreRepository.searchGenres(keyword, minUsers, minVideos);
    }
}
