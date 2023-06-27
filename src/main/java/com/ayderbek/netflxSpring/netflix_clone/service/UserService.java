package com.ayderbek.netflxSpring.netflix_clone.service;

import com.amazonaws.services.alexaforbusiness.model.NotFoundException;
import com.ayderbek.netflxSpring.netflix_clone.domain.User;
import com.ayderbek.netflxSpring.netflix_clone.repos.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository repository;

    public User getById(Long userId) {
        return repository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Video not found with id " + userId));
    }

    public List<User> getAllUsers() {
        return repository.findAll();
    }

    public List<User> searchUsers(String keyword, String email, int minPlaylists, int minGenres) {
        return repository.searchUsers(keyword, email, minPlaylists, minGenres);
    }

    public User createUser(OAuth2User principal) {
        User user = new User();

        user.setAuth0Id(principal.getName());
        user.setName(principal.getAttribute("name"));
        user.setEmail(principal.getAttribute("email"));
        user.setEmailVerified(principal.getAttribute("email_verified"));
        user.setPicture(principal.getAttribute("picture"));

        // password should be handled carefully, not all OAuth2 providers give password
        // you might consider setting a default password or asking user to set it separately
        user.setPassword("default");

        return repository.save(user);
    }

    public User findByAuthProviderUserId(String authProviderUserId) {
        return repository.findByAuth0Id(authProviderUserId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with Auth0 Id: " + authProviderUserId));
    }
}
