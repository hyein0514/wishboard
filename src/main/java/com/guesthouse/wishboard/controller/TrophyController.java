package com.guesthouse.wishboard.controller;

import com.guesthouse.wishboard.dto.TrophyDetail1Dto;
import com.guesthouse.wishboard.dto.TrophyDetail2Dto;
import com.guesthouse.wishboard.dto.TrophyDto;
import com.guesthouse.wishboard.jwt.CustomUserDetail;
import com.guesthouse.wishboard.service.TrophyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/trophy")
public class TrophyController {
    private final TrophyService trophyService;

    // 사용자 ID로 완료된 버킷리스트 목록 조회
    @GetMapping("/trophies")
    public ResponseEntity<List<TrophyDto>> getTrophies(@AuthenticationPrincipal CustomUserDetail users) {
        List<TrophyDto> trophies = trophyService.getUserTrophies(users);
        return ResponseEntity.ok(trophies);
    }
    // 완료된 버킷리스트 기본 정보 (첫 화면)
    @GetMapping("/detail/{bucketId}")
    public ResponseEntity<TrophyDetail1Dto> getBucketDetail(@PathVariable Long bucketId, @AuthenticationPrincipal CustomUserDetail users) {
        TrophyDetail1Dto detail = trophyService.getBucketDetail(bucketId, users);
        return ResponseEntity.ok(detail);
    }

    // 완료된 버킷리스트 로그 목록 (두 번째 화면부터)
    @GetMapping("/logs/{bucketId}")
    public ResponseEntity<List<TrophyDetail2Dto>> getBucketLogs(@PathVariable Long bucketId, @AuthenticationPrincipal CustomUserDetail users) {
        List<TrophyDetail2Dto> logs = trophyService.getBucketLogsSorted(bucketId, users);
        return ResponseEntity.ok(logs);
    }
}