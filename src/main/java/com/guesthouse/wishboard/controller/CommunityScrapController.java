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

    // 실제 보안 로직 대신 하드코딩된 유저ID 반환
    // 나중에 currentUserId() 이 부분을 다 userId로 바꿔야함
    private Long currentUserPk() { return 1L; }
    // 인자에 꼭 ,Principal principal 넣기

    //String userId = principal.getName(); 이거도 각각 넣어야함

    @PostMapping
    public ResponseEntity<CommunityScrapResponse> add(
            @RequestBody @Valid CommunityScrapRequest req) {

        CommunityScrapResponse res =
                scrapService.addScrap(req.communityName(), currentUserPk());
        return ResponseEntity.status(HttpStatus.CREATED).body(res);
    }

    @DeleteMapping("/{communityName}")
    public ResponseEntity<Void> remove(@PathVariable String communityName) {

        scrapService.deleteScrap(communityName, currentUserPk());
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<Page<CommunityScrapResponse>> list(Pageable pageable) {

        Page<CommunityScrapResponse> res =
                scrapService.myScraps(currentUserPk(), pageable);
        return ResponseEntity.ok(res);
    }
}

