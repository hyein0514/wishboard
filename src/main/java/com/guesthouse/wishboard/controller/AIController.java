
package com.guesthouse.wishboard.controller;

import com.guesthouse.wishboard.dto.AIResponseDto;
import com.guesthouse.wishboard.dto.TrophyDetail1Dto;
import com.guesthouse.wishboard.dto.TrophyDto;
import com.guesthouse.wishboard.jwt.CustomUserDetail;
import com.guesthouse.wishboard.service.AIService;
import com.guesthouse.wishboard.service.TrophyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/ai")
public class AIController {
    private final AIService aiService;
    private final TrophyService trophyService;

    @PostMapping("/recommend")
    public ResponseEntity<AIResponseDto> recommend(@AuthenticationPrincipal CustomUserDetail user) {
        List<TrophyDto> completed = trophyService.getUserTrophies(user);
        List<String> completedTitles = completed.stream()
                .map(TrophyDto::getTitle)
                .collect(Collectors.toList());

        AIResponseDto response = aiService.getFinalRecommendations(completedTitles);
        return ResponseEntity.ok(response);
    }
    
    // 선택한 추천 버킷리스트글 조회
    // 수정필요
    @GetMapping("/detail/{CommunityId}")
    public ResponseEntity<TrophyDetail1Dto> getBucketDetail(@PathVariable Long communityId) {
        return ResponseEntity.ok(trophyService.getBucketDetailH(communityId));
    }

}