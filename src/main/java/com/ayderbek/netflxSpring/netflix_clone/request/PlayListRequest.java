package com.ayderbek.netflxSpring.netflix_clone.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PlayListRequest {
    private String name;
    private Long userId;
    private List<Long> videoIds;
}
