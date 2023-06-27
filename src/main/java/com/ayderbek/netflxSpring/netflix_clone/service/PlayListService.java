package com.ayderbek.netflxSpring.netflix_clone.service;


import com.ayderbek.netflxSpring.netflix_clone.domain.PlayList;
import com.ayderbek.netflxSpring.netflix_clone.domain.User;
import com.ayderbek.netflxSpring.netflix_clone.domain.Video;
import com.ayderbek.netflxSpring.netflix_clone.repos.PlayListRepository;
import com.ayderbek.netflxSpring.netflix_clone.repos.UserRepository;
import com.ayderbek.netflxSpring.netflix_clone.repos.VideoRepository;
import com.ayderbek.netflxSpring.netflix_clone.request.PlayListRequest;
import jakarta.persistence.Cacheable;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class PlayListService {
    private final PlayListRepository playListRepository;

    private final UserService userService;

    private final VideoService videoService;

    private final VideoRepository videoRepository;

    @Async
    public CompletableFuture<PlayList> createPlayListAsync(PlayListRequest request) {
        User user = userService.getById(request.getUserId());
        PlayList playList = new PlayList();
        playList.setUser(user);

        List<Long> videoIds = request.getVideoIds();
        List<Video> videos = videoIds.stream()
                .map(videoService::getById)
                .collect(Collectors.toList());
        playList.setVideos(videos);
        playList.setName(request.getName());

        PlayList createdPlayList = playListRepository.save(playList);
        return CompletableFuture.completedFuture(createdPlayList);
    }

    public PlayList updatePlayList(Long id, PlayListRequest request) {
        Optional<PlayList> playListOptional = playListRepository.findById(id);
        if (playListOptional.isEmpty()) {
            throw new EntityNotFoundException("PlayList not found with id " + id);
        }
        PlayList playList = playListOptional.get();

        if (request.getName() != null) {
            playList.setName(request.getName());
        }
        return playListRepository.save(playList);
    }
    public PlayList patchPlayList(Long id, PlayListRequest request) {
        Optional<PlayList> playListOptional = playListRepository.findById(id);
        if (playListOptional.isEmpty()) {
            throw new EntityNotFoundException("PlayList not found with id " + id);
        }
        PlayList playList = playListOptional.get();

        if (request.getName() != null) {
            playList.setName(request.getName());
        }
        if (request.getUserId() != null) {
            User user = userService.getById(request.getUserId());
            playList.setUser(user);
        }
        if (request.getVideoIds() != null){
            List<Video> videos =  videoRepository.findAllById(request.getVideoIds());
            playList.setVideos(videos);
        }
        return playListRepository.save(playList);
    }


    public PlayList getPlayList(Long id) {
        return playListRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("PlayList with id " + id + " not found"));
    }

    public List<PlayList> getAllPlayLists() {
        return playListRepository.findAll();
    }

    public void deletePlayList(Long id) {
        Optional<PlayList> playListOptional = playListRepository.findById(id);
        if (playListOptional.isPresent()) {
            PlayList playList = playListOptional.get();
            playListRepository.delete(playList);
        } else {
            throw new RuntimeException("there is no id ");
        }
    }

    @CacheEvict(value = {"playlists", "playlistsByUser"}, allEntries = true)
    public void deletePlayList(PlayList playList) {
        playListRepository.delete(playList);
    }

    public CompletableFuture<List<PlayList>> findPlaylistsByUserAsync(User user) {
        return CompletableFuture.completedFuture(playListRepository.findPlaylistsByUser(user));
    }

    public List<PlayList> findPlaylistsByGenreAndUserOrderByPlaylistName(String genreName, User user) {
        return playListRepository.findPlaylistsByGenreAndUserOrderByPlaylistName(genreName, user);
    }


}
