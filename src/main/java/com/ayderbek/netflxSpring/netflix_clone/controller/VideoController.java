package com.ayderbek.netflxSpring.netflix_clone.controller;

import com.ayderbek.netflxSpring.netflix_clone.domain.*;
import com.ayderbek.netflxSpring.netflix_clone.repos.VideoRepository;
import com.ayderbek.netflxSpring.netflix_clone.repos.VideoViewRepository;
import com.ayderbek.netflxSpring.netflix_clone.request.VideoViewDTO;
import com.ayderbek.netflxSpring.netflix_clone.service.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/videos")
@RequiredArgsConstructor
@Slf4j
public class VideoController {
    private final VideoStreamingService videoStreamingService;

    private final VideoService videoService;

    private final UserService userService;

    private final VideoViewRepository videoViewRepository;

    private final SubscriptionService subscriptionService;

    private final CategoryService categoryService;


    @GetMapping("/{videoId}")
    public ResponseEntity<String> getVideoStream(@PathVariable Long videoId,@AuthenticationPrincipal OAuth2User principal) {
        if (principal == null) {
            // handle the situation when the user is not authenticated
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not authenticated.");
        }
        String authProviderUserId = principal.getAttribute("sub");
        User user = userService.findByAuthProviderUserId(authProviderUserId);

        if (user == null) {
            // handle the situation when the user is not found in your database
            // this should ideally never happen if your OAuth2 login success handler is set up correctly
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not found.");
        }
        Subscription activeSubscription = subscriptionService.getActiveSubscription(user.getId());
        if (activeSubscription == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("No active subscription.");
        }

        try {
            String signedUrl = videoStreamingService.getSignedCloudFrontUrl(videoId);
            return ResponseEntity.ok(signedUrl);
        } catch (Exception e) {
            log.error("Error generating signed URL", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error generating signed URL.");
        }
    }


    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<Video>> getVideosByCategory(@PathVariable Long categoryId) {
        Category category = new Category();
        category.setId(categoryId);
        List<Video> videos = videoService.findVideosByCategory(category);
        return ResponseEntity.ok(videos);
    }

    @GetMapping
    public List<Video> getVideosReleasedAfterDate(
            @RequestParam("releaseDate")
            @DateTimeFormat(pattern = "yyyy-MM-dd")
            LocalDate releaseDate) {
        return videoService.findVideosReleasedAfterDate(releaseDate);
    }

    @GetMapping("/search")
    public CompletableFuture<List<Video>> searchVideosAsync(@RequestParam("query") String query) {
        return videoService.searchVideosAsync(query);
    }

    @PostMapping("/send-to-kafka")
    public ResponseEntity<String> sendVideosToKafka() {
        videoService.sendAllVideosToKafka();
        return ResponseEntity.ok("Videos sent to Kafka");
    }
    @PostMapping("/view")
    public ResponseEntity<String> incrementViewCount(@RequestBody VideoViewDTO videoViewDTO) {
        User user = userService.getById(videoViewDTO.getUserId());

        Video video = videoService.getById(videoViewDTO.getVideoId());

        VideoView videoView = new VideoView();
        videoView.setUser(user);
        videoView.setVideo(video);
        videoView.setViewTime(videoViewDTO.getViewTime());

        videoViewRepository.save(videoView);

        return ResponseEntity.ok("View count incremented");
    }

    @GetMapping("/trending/{categoryId}")
    public ResponseEntity<List<Video>> getTrendingVideos(@PathVariable Long categoryId) {
        Category category = categoryService.getById(categoryId);
        if (category != null) {
            List<Video> trendingVideos = videoService.getTrendingVideos(category);
            return new ResponseEntity<>(trendingVideos, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
