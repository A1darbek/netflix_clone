package com.ayderbek.netflxSpring.netflix_clone.service;


import com.ayderbek.netflxSpring.netflix_clone.domain.Video;
import com.ayderbek.netflxSpring.netflix_clone.domain.WatchHistory;
import com.ayderbek.netflxSpring.netflix_clone.repos.WatchHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WatchHistoryService {

    private final NewTopic netflixTopic;

    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final WatchHistoryRepository watchHistoryRepository;

    public List<WatchHistory> findByUserId(Long userId) {
        return watchHistoryRepository.findByUserId(userId);
    }

    public List<WatchHistory> findByVideoId(Long videoId) {
        return watchHistoryRepository.findByVideoId(videoId);
    }

    public List<WatchHistory> findByPlaylistId(Long playlistId) {
        return watchHistoryRepository.findByPlaylistId(playlistId);
    }

    public WatchHistory save(WatchHistory watchHistory) {
        return watchHistoryRepository.save(watchHistory);
    }

    public void deleteById(Long id) {
        watchHistoryRepository.deleteById(id);
    }

    public void sendAllWatchHistoriesToKafka() {
        List<WatchHistory> watchHistories = watchHistoryRepository.findAll();
        watchHistories.forEach(watchHistory -> kafkaTemplate.send(netflixTopic.name(), watchHistory));
    }
}
