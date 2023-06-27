package com.ayderbek.netflxSpring.netflix_clone.repos;

import com.ayderbek.netflxSpring.netflix_clone.domain.VideoView;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VideoViewRepository extends JpaRepository<VideoView,Long> {
}
