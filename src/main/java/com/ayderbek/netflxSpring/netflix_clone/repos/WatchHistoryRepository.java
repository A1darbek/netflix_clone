package com.ayderbek.netflxSpring.netflix_clone.repos;

import com.ayderbek.netflxSpring.netflix_clone.domain.WatchHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WatchHistoryRepository extends JpaRepository<WatchHistory,Long> {
    List<WatchHistory> findByUserId(Long userId);
    List<WatchHistory> findByVideoId(Long videoId);
    List<WatchHistory> findByPlaylistId(Long playlistId);
}
