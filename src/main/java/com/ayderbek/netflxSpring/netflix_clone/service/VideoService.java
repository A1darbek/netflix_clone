package com.ayderbek.netflxSpring.netflix_clone.service;

import com.amazonaws.services.alexaforbusiness.model.NotFoundException;
import com.ayderbek.netflxSpring.netflix_clone.domain.Category;
import com.ayderbek.netflxSpring.netflix_clone.domain.Director;
import com.ayderbek.netflxSpring.netflix_clone.domain.Video;
import com.ayderbek.netflxSpring.netflix_clone.domain.VideoRowMapper;
import com.ayderbek.netflxSpring.netflix_clone.repos.CategoryRepository;
import com.ayderbek.netflxSpring.netflix_clone.repos.DirectorRepository;
import com.ayderbek.netflxSpring.netflix_clone.repos.VideoRepository;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class VideoService {

    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final NewTopic netflixTopic;
    private final VideoRepository videoRepository;
    private final DirectorRepository directorRepository;
    private final JdbcTemplate jdbcTemplate;
    private final CategoryRepository categoryRepository;


    public Video getById(Long videoId) {
        return videoRepository.findById(videoId)
                .orElseThrow(() -> new NotFoundException("Video not found with id " + videoId));
    }

    public List<Video> findVideosByCategory(Category category) {
        return videoRepository.findVideosByCategory(category);
    }

    public List<Video> findVideosReleasedAfterDate(LocalDate releaseDate) {
        return videoRepository.findVideosReleasedAfterDate(releaseDate);
    }

    public CompletableFuture<List<Video>> searchVideosAsync(String query) {
        return CompletableFuture.supplyAsync(() -> {
            String sql = "SELECT * FROM search_videos(?)";
            return jdbcTemplate.query(sql, ps -> ps.setString(1, query),
                    new VideoRowMapper(categoryRepository,directorRepository,jdbcTemplate));
        });
    }


    public Video save(Video video) {
        Video savedVideo = videoRepository.save(video);
        String message = String.format("New video added: %s", savedVideo.getTitle());
        kafkaTemplate.send(netflixTopic.name(), savedVideo);
        return savedVideo;
    }

    public void update(Video video) {
        Video updatedVideo = videoRepository.save(video);
        String message = String.format("Video updated: %s", updatedVideo.getTitle());
        kafkaTemplate.send(netflixTopic.name(), updatedVideo);
    }

    public void sendAllVideosToKafka() {
        List<Video> videos = videoRepository.findAll();
        videos.forEach(video -> kafkaTemplate.send(netflixTopic.name(), video));
    }

    public List<Video> getTrendingVideos(Category category) {
        return jdbcTemplate.query(
                "SELECT v.*, count(w.id) as watch_count " +
                        "FROM video v " +
                        "JOIN watch_history w ON v.id = w.video_id " +
                        "WHERE v.category_id = ? AND w.timestamp > (current_timestamp - INTERVAL '1 week') " +
                        "GROUP BY v.id " +
                        "ORDER BY watch_count DESC",
                new VideoRowMapper(categoryRepository,directorRepository,jdbcTemplate),
                category.getId()
        );
    }


}
