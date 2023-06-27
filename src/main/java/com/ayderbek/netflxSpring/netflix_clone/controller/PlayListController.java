package com.ayderbek.netflxSpring.netflix_clone.controller;

import com.ayderbek.netflxSpring.netflix_clone.domain.PlayList;
import com.ayderbek.netflxSpring.netflix_clone.domain.User;
import com.ayderbek.netflxSpring.netflix_clone.request.PlayListRequest;
import com.ayderbek.netflxSpring.netflix_clone.service.PlayListService;
import com.ayderbek.netflxSpring.netflix_clone.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/playlists")
@RequiredArgsConstructor
@CrossOrigin("*")
public class PlayListController {
    private final PlayListService playListService;

    private final UserService userService;

    @GetMapping
    public List<PlayList> getAllPlayLists() {
        var playLists = playListService.getAllPlayLists();
        return playLists;
    }

    @PostMapping("/create")
    public CompletableFuture<PlayList> createPlayList(@RequestBody PlayListRequest request) {
        return playListService.createPlayListAsync(request);
    }

    @PutMapping("/{id}")
    public PlayList updatePlayList(@PathVariable Long id,@RequestBody PlayListRequest request) {
        return playListService.updatePlayList(id,request);
    }

    @PatchMapping("/{id}")
    public PlayList patchPlayList(@PathVariable Long id,@RequestBody PlayListRequest request) {
        return playListService.patchPlayList(id,request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePlayList(@PathVariable Long id) {
        playListService.deletePlayList(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/user/{userId}")
    public CompletableFuture<List<PlayList>> findPlaylistsByUser(@PathVariable Long userId) {
        User user = new User();
        user.setId(userId);
        return playListService.findPlaylistsByUserAsync(user);
    }

    @GetMapping("/users/{userId}")
    public ResponseEntity<List<PlayList>> getPlaylistsByGenreAndUser(
            @PathVariable Long userId,
            @RequestParam String genreName) {
        User user = userService.getById(userId);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        List<PlayList> playlists = playListService.findPlaylistsByGenreAndUserOrderByPlaylistName(genreName, user);
        return ResponseEntity.ok(playlists);
    }

}
