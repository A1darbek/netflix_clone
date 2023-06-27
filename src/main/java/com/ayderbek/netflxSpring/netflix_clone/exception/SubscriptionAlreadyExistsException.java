package com.ayderbek.netflxSpring.netflix_clone.exception;

public class SubscriptionAlreadyExistsException extends RuntimeException {

    public SubscriptionAlreadyExistsException(String message) {
        super(message);
    }
}

