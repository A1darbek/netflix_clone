package com.ayderbek.netflxSpring.netflix_clone.service;

import com.ayderbek.netflxSpring.netflix_clone.domain.Subscription;
import com.ayderbek.netflxSpring.netflix_clone.domain.SubscriptionType;
import com.ayderbek.netflxSpring.netflix_clone.domain.User;
import com.ayderbek.netflxSpring.netflix_clone.exception.SubscriptionAlreadyExistsException;
import com.ayderbek.netflxSpring.netflix_clone.exception.SubscriptionNotFoundException;
import com.ayderbek.netflxSpring.netflix_clone.repos.SubscriptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class SubscriptionService {
    private final SubscriptionRepository subscriptionRepository;

    private final UserService userService;

    public Subscription getActiveSubscription(Long userId) {
        return subscriptionRepository.findActiveByUserId(userId);
    }

    public Subscription createSubscription(Long userId, SubscriptionType type) {
        User user = userService.getById(userId);

        Subscription existingSubscription = subscriptionRepository.findByUser(user);
        if (existingSubscription != null) {
            throw new SubscriptionAlreadyExistsException("User with id " + userId + " already has a subscription");
        }

        double price = type == SubscriptionType.PREMIUM ? 15.99 : (type == SubscriptionType.STANDARD ? 12.99 : 8.99);

        Subscription subscription = Subscription.builder()
                .user(user)
                .type(type)
                .startDate(LocalDate.now())
                .endDate(LocalDate.now().plusMonths(1))
                .isActive(true)
                .price(price)
                .build();

        return subscriptionRepository.save(subscription);
    }


    public Subscription changePlan(User user, SubscriptionType newType) {
        Subscription subscription = subscriptionRepository.findByUserAndIsActive(user, true);

        if (subscription == null) {
            throw new SubscriptionNotFoundException
                    ("Active subscription not found for user with id " + user.getId());
        }

        subscription.setType(newType);
        subscription.setPrice(determinePriceBasedOnType(newType));
        return subscriptionRepository.save(subscription);
    }

    public void cancelSubscription(User user) {
        Subscription subscription = subscriptionRepository.findByUser(user);
        subscription.setIsActive(false);
        subscriptionRepository.save(subscription);
    }

    private double determinePriceBasedOnType(SubscriptionType type) {
        // Replace these with your actual prices
        return switch (type) {
            case BASIC -> 8.99;
            case STANDARD -> 12.99;
            case PREMIUM -> 15.99;
            default -> throw new IllegalArgumentException("Unknown type: " + type);
        };
    }

    public Subscription reactivateSubscription(Long userId, SubscriptionType type) {
        User user = userService.getById(userId);
        Subscription subscription = subscriptionRepository.findByUserAndIsActive(user, false);

        if(subscription != null){
            subscription.setIsActive(true);
            subscription.setStartDate(LocalDate.now());
            subscription.setEndDate(LocalDate.now().plusMonths(1));
            subscription.setType(type);
            subscriptionRepository.save(subscription);
        } else {
            subscription = this.createSubscription(userId, type);
        }
        return subscription;
    }

}
