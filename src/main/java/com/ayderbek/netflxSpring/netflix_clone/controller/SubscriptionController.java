package com.ayderbek.netflxSpring.netflix_clone.controller;

import com.ayderbek.netflxSpring.netflix_clone.domain.Subscription;
import com.ayderbek.netflxSpring.netflix_clone.domain.SubscriptionType;
import com.ayderbek.netflxSpring.netflix_clone.domain.User;
import com.ayderbek.netflxSpring.netflix_clone.service.SubscriptionService;
import com.ayderbek.netflxSpring.netflix_clone.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/subscriptions")
@RequiredArgsConstructor
@CrossOrigin("*")
public class SubscriptionController {
    private final SubscriptionService subscriptionService;
    private final UserService userService;


    @PostMapping("/{userId}/{type}")
    public ResponseEntity<Subscription> subscribe(@PathVariable Long userId, @PathVariable SubscriptionType type) {
        Subscription subscription = subscriptionService.createSubscription(userId, type);
        if(subscription != null){
            return new ResponseEntity<>(subscription, HttpStatus.CREATED);
        }
        else{
            // Handle subscription not created situation
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/{userId}/{type}")
    public ResponseEntity<Subscription> changePlan(@PathVariable Long userId, @PathVariable SubscriptionType type) {
        User user = userService.getById(userId);
        Subscription subscription = subscriptionService.changePlan(user, type);
        if(subscription != null){
            return new ResponseEntity<>(subscription, HttpStatus.OK);
        }
        else{
            // Handle plan not changed situation
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }


    @PutMapping("/{userId}/reactivate/{type}")
    public ResponseEntity<Subscription> reactivateSubscription(@PathVariable Long userId, @PathVariable SubscriptionType type) {
        Subscription subscription = subscriptionService.reactivateSubscription(userId, type);
        if(subscription != null){
            return new ResponseEntity<>(subscription, HttpStatus.OK);
        }
        else{
            // Handle subscription not reactivated situation
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }


    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> cancelSubscription(@PathVariable Long userId) {
        User user = userService.getById(userId);
        subscriptionService.cancelSubscription(user);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
