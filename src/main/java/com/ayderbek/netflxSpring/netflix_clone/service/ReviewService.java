package com.ayderbek.netflxSpring.netflix_clone.service;

import com.ayderbek.netflxSpring.netflix_clone.domain.*;
import com.ayderbek.netflxSpring.netflix_clone.repos.ReviewRepository;
import com.ayderbek.netflxSpring.netflix_clone.repos.UserRepository;
import com.ayderbek.netflxSpring.netflix_clone.request.ReviewRequest;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final UserService userService;
    private final VideoService videoService;

    private final NewTopic netflixTopic;

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public Review getReviewById(Long id) {
        return reviewRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Review with id " + id + " not found"));
    }

    public List<Review> getAllReviews() {
        return reviewRepository.findAll();
    }


    public Review createReview(ReviewRequest reviewRequest) {
        User user = userService.getById(reviewRequest.getUserId());
        Video video = videoService.getById(reviewRequest.getVideoId());
        Review review = new Review();
        review.setUser(user);
        review.setVideo(video);
        review.setRating(reviewRequest.getRating());
        review.setText(reviewRequest.getText());
        return reviewRepository.save(review);
    }

    public Review updateReview(Long id, ReviewRequest reviewRequest) {
        Review review = getReviewById(id);

        if (reviewRequest.getUserId() != null) {
            User user = userService.getById(reviewRequest.getUserId());
            review.setUser(user);
        }

        if (reviewRequest.getVideoId() != null) {
            Video video = videoService.getById(reviewRequest.getVideoId());
            review.setVideo(video);
        }

        if (reviewRequest.getRating() != null) {
            review.setRating(reviewRequest.getRating());
        }

        if (reviewRequest.getText() != null) {
            review.setText(reviewRequest.getText());
        }

        return reviewRepository.save(review);
    }

    public void deleteReview(Long id) {
        Optional<Review> reviewOptional = reviewRepository.findById(id);
        if (reviewOptional.isPresent()) {
            Review review = reviewOptional.get();
            reviewRepository.delete(review);
        } else {
            throw new RuntimeException("there is no id ");
        }
    }

    public void sendAllReviewsToKafka() {
        List<Review> reviews = reviewRepository.findAll();
        reviews.forEach(review -> kafkaTemplate.send(netflixTopic.name(), review));
    }
}
