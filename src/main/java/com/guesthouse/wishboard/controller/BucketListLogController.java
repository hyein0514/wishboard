package com.guesthouse.wishboard.controller;

import com.guesthouse.wishboard.dto.BucketListLogResponseDto;
import com.guesthouse.wishboard.service.BucketListLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/bucketlist-log")
@RequiredArgsConstructor
public class BucketListLogController {

    private final BucketListLogService bucketListLogService;

    @GetMapping("/{logId}/prev")
    public ResponseEntity<BucketListLogResponseDto> getPrevLog(@PathVariable Long logId) {
        return ResponseEntity.ok(bucketListLogService.getPrevLog(logId));
    }

    @GetMapping("/{logId}/next")
    public ResponseEntity<BucketListLogResponseDto> getNextLog(@PathVariable Long logId) {
        return ResponseEntity.ok(bucketListLogService.getNextLog(logId));
    }
}


