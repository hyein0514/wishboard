package com.guesthouse.wishboard.controller;

import com.guesthouse.wishboard.dto.BucketListRequestDto;
import com.guesthouse.wishboard.dto.BucketListResponseDto;
import com.guesthouse.wishboard.service.BucketListService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bucketlist")
@RequiredArgsConstructor
public class BucketListController {

    private final BucketListService bucketListService;

    @PostMapping
    public ResponseEntity<String> create(@RequestBody BucketListRequestDto dto) {
        bucketListService.createBucketList(dto);
        return ResponseEntity.ok("버킷리스트가 등록되었습니다.");
    }

    @GetMapping
    public ResponseEntity<List<BucketListResponseDto>> getMyBucketLists() {
        return ResponseEntity.ok(bucketListService.getMyBucketLists());
    }
}
