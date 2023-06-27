package com.ayderbek.netflxSpring.netflix_clone.request;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class VideoViewDTO {
    private Long videoId;
    private Long userId;
    private LocalDateTime viewTime;
}
