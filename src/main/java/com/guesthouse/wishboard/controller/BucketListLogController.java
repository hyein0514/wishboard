package com.guesthouse.wishboard.controller;

import com.guesthouse.wishboard.dto.BucketListLogRequestDto;
import com.guesthouse.wishboard.dto.BucketListLogResponseDto;
import com.guesthouse.wishboard.service.BucketListLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bucketlist/log")
@RequiredArgsConstructor
public class BucketListLogController {

    private final BucketListLogService bucketListLogService;

    @PostMapping
    public ResponseEntity<String> createLog(@RequestBody BucketListLogRequestDto dto) {
        bucketListLogService.createLog(dto);
        return ResponseEntity.ok("도전 기록이 등록되었습니다.");
    }

    @GetMapping("/{bucketId}")
    public ResponseEntity<List<BucketListLogResponseDto>> getLogs(@PathVariable Long bucketId) {
        return ResponseEntity.ok(bucketListLogService.getLogList(bucketId));
    }

    @GetMapping("/detail/{logId}")
    public ResponseEntity<BucketListLogResponseDto> getLogDetail(@PathVariable Long logId) {
        return ResponseEntity.ok(bucketListLogService.getLogDetail(logId));
    }

    @DeleteMapping("/{logId}")
    public ResponseEntity<String> deleteLog(@PathVariable Long logId) {
        bucketListLogService.deleteLog(logId);
        return ResponseEntity.ok("도전 기록이 삭제되었습니다.");
    }
}


