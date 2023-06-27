package com.ayderbek.netflxSpring.netflix_clone.controller;

import com.ayderbek.netflxSpring.netflix_clone.domain.PlayList;
import com.ayderbek.netflxSpring.netflix_clone.domain.Review;
import com.ayderbek.netflxSpring.netflix_clone.request.PlayListRequest;
import com.ayderbek.netflxSpring.netflix_clone.request.ReviewRequest;
import com.ayderbek.netflxSpring.netflix_clone.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/reviews")
@RequiredArgsConstructor
@CrossOrigin("*")
public class ReviewController {
    private final ReviewService reviewService;

    @GetMapping
    public List<Review> getAllReviews() {
        var reviews = reviewService.getAllReviews();
        return reviews;
    }

    @PostMapping("/create")
    public ResponseEntity<Review> createReview(@RequestBody ReviewRequest request) {
        var review =  reviewService.createReview(request);
       return ResponseEntity.ok(review);
    }

    @PutMapping("/{id}")
    public Review updatePlayList(@PathVariable Long id,@RequestBody ReviewRequest request) {
        return reviewService.updateReview(id,request);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProperty(@PathVariable Long id) {
        reviewService.deleteReview(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/send-to-kafka")
    public ResponseEntity<String> sendReviewsToKafka() {
        reviewService.sendAllReviewsToKafka();
        return ResponseEntity.ok("Reviews sent to Kafka");
    }
}
