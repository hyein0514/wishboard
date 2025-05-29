package com.guesthouse.wishboard.controller;

import com.guesthouse.wishboard.dto.HallOfFameDto;
import com.guesthouse.wishboard.dto.TrophyDetail1Dto;
import com.guesthouse.wishboard.dto.TrophyDetail2Dto;
import com.guesthouse.wishboard.service.HallOfFameService;
import com.guesthouse.wishboard.service.TrophyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/HallOfFame")
public class HallOfFameController {

    private final HallOfFameService hallOfFameService;
    private final TrophyService trophyService;

    
    // Top 10 가져오기
    @GetMapping("/TOP10")
    public ResponseEntity<List<HallOfFameDto>> getHallOfFame() {
        return ResponseEntity.ok(hallOfFameService.getHallOfFame());
    }

    // 선택한 트로피의 상세 정보
    @GetMapping("/detail/{bucketId}")
    public ResponseEntity<TrophyDetail1Dto> getBucketDetail(@PathVariable Long bucketId) {
        return ResponseEntity.ok(trophyService.getBucketDetailH(bucketId));
    }

    // 선택한 트로피의 로그 정보
    @GetMapping("/logs/{bucketId}")
    public ResponseEntity<List<TrophyDetail2Dto>> getBucketLogs(@PathVariable Long bucketId) {
        return ResponseEntity.ok(trophyService.getBucketLogsSortedH(bucketId));
    }
}
