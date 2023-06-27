package com.ayderbek.netflxSpring.netflix_clone.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Subscription {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    private User user;

    @Enumerated(EnumType.STRING)
    private SubscriptionType type;

    private LocalDate startDate;

    private LocalDate endDate;

    private Boolean isActive;

    @Transient
    private Double price;

    public Double getPrice() {
        return switch (type) {
            case BASIC -> 8.99;
            case STANDARD -> 12.99;
            case PREMIUM -> 15.99;
            default -> throw new IllegalStateException("Unexpected value: " + type);
        };
    }
}
