package com.ayderbek.netflxSpring.netflix_clone.controller;

import com.ayderbek.netflxSpring.netflix_clone.service.WatchHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/watchHistories")
@RequiredArgsConstructor
@CrossOrigin("*")
public class WatchHistoryController {
    private final WatchHistoryService watchHistoryService;

    @PostMapping("/send-to-kafka")
    public ResponseEntity<String> sendWatchHistoriesToKafka() {
        watchHistoryService.sendAllWatchHistoriesToKafka();
        return ResponseEntity.ok("WatchHistories sent to Kafka");
    }
}
