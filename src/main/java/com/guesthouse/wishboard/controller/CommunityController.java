// src/main/java/com/guesthouse/wishboard/controller/CommunityController.java
package com.guesthouse.wishboard.controller;

import com.guesthouse.wishboard.dto.CommunityPostResponse;
import com.guesthouse.wishboard.dto.CommunitySearchResponse;
import com.guesthouse.wishboard.service.CommunityService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CommunityController {

    private final CommunityService communityService;
    private static final int DEFAULT_PAGE_SIZE = 10;

    @GetMapping("/communities")
    public ResponseEntity<List<CommunitySearchResponse>> searchCommunities(
            @RequestParam String keyword) {

        List<CommunitySearchResponse> result = communityService.searchBySubKeyword(keyword);
        return ResponseEntity.ok(result);
    }

    /** 상위분류에 속한 게시글 전체 조회 **/
    @GetMapping("/communities/{communityType}")
    public Page<CommunityPostResponse> getByCommunityType(
            @PathVariable String communityType,
            Pageable pageable) {
        return communityService.findByType(communityType, pageable);
    }

}
