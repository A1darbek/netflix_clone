package com.ayderbek.netflxSpring.netflix_clone.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReviewRequest {
    private Long userId;
    private Long videoId;
    private Integer rating;
    private String text;
}
