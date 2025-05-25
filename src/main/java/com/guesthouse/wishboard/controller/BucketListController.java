package com.guesthouse.wishboard.controller;

import com.guesthouse.wishboard.dto.BucketListRequestDto;
import com.guesthouse.wishboard.dto.BucketListResponseDto;
import com.guesthouse.wishboard.dto.BucketListDetailDto;
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

    @GetMapping("/{id}")
    public ResponseEntity<BucketListDetailDto> getDetail(@PathVariable Long id) {
        return ResponseEntity.ok(bucketListService.getBucketListDetail(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        bucketListService.deleteBucketList(id);
        return ResponseEntity.ok("버킷리스트가 삭제되었습니다.");
    }

    @PatchMapping("/{id}/pin")
    public ResponseEntity<String> togglePin(@PathVariable Long id) {
        bucketListService.togglePin(id);
        return ResponseEntity.ok("핀 상태가 변경되었습니다.");
    }

    @PatchMapping("/{id}/achieve")
    public ResponseEntity<String> achieve(@PathVariable Long id) {
        bucketListService.achieve(id);
        return ResponseEntity.ok("버킷리스트가 완료 처리되었습니다.");
    }
}
