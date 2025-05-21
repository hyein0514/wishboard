package com.guesthouse.wishboard.controller;

import com.guesthouse.wishboard.dto.CommunityScrapRequest;
import com.guesthouse.wishboard.dto.CommunityScrapResponse;
import com.guesthouse.wishboard.service.CommunityScrapService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/community-scraps")
@RequiredArgsConstructor
public class CommunityScrapController {

    private final CommunityScrapService scrapService;

    @PostMapping
    public ResponseEntity<CommunityScrapResponse> add(
            @RequestBody @Valid CommunityScrapRequest req,
            Principal principal) {                              // ← 여기에 Principal 추가

        String userId = principal.getName();                  // ← 여기서 userId 꺼내고
        CommunityScrapResponse res =
                scrapService.addScrap(req.communityName(), userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(res);
    }

    @DeleteMapping("/{communityName}")
    public ResponseEntity<Void> remove(
            @PathVariable String communityName,
            Principal principal) {                              // ← 여기에도

        String userId = principal.getName();
        scrapService.deleteScrap(communityName, userId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<Page<CommunityScrapResponse>> list(
            Pageable pageable,
            Principal principal) {                              // ← 그리고 여기

        String userId = principal.getName();
        Page<CommunityScrapResponse> res = scrapService.myScraps(userId, pageable);
        return ResponseEntity.ok(res);
    }
}

