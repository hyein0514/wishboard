package com.guesthouse.wishboard.controller;

import com.guesthouse.wishboard.dto.CommunityScrapRequest;
import com.guesthouse.wishboard.dto.CommunityScrapResponse;
import com.guesthouse.wishboard.jwt.CustomUserDetail;
import com.guesthouse.wishboard.service.CommunityScrapService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/community-scraps")
@RequiredArgsConstructor
public class CommunityScrapController {

    private final CommunityScrapService scrapService;


    private Long currentUserPk() { return 1L; }


    @PostMapping
    public ResponseEntity<CommunityScrapResponse> add(
            @RequestBody @Valid CommunityScrapRequest req,
            @AuthenticationPrincipal CustomUserDetail user) {

        CommunityScrapResponse res =
                scrapService.addScrap(req.communityName(), user.getUser().getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(res);
    }

    @DeleteMapping("/{communityName}")
    public ResponseEntity<Void> remove(
            @PathVariable String communityName,
            @AuthenticationPrincipal CustomUserDetail user) {

        scrapService.deleteScrap(communityName, user.getUser().getId());
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<Page<CommunityScrapResponse>> list(
            Pageable pageable,
            @AuthenticationPrincipal CustomUserDetail user) {

        Page<CommunityScrapResponse> res =
                scrapService.myScraps(user.getUser().getId(), pageable);
        return ResponseEntity.ok(res);
    }
}

