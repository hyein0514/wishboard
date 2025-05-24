package com.guesthouse.wishboard.controller;

import com.guesthouse.wishboard.dto.KeywordRequest;
import com.guesthouse.wishboard.dto.KeywordResponse;
import com.guesthouse.wishboard.jwt.CustomUserDetail;
import com.guesthouse.wishboard.service.KeywordService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/keywords")
@RequiredArgsConstructor
public class KeywordController {

    private final KeywordService keywordService;

    /* ★ 개발용 더미 사용자 → 실제 보안 적용 시 교체 */
    private Long currentUserId() { return 1L; }

    /* ───────── 등록 ───────── */
    @PostMapping
    public ResponseEntity<KeywordResponse> add(
            @RequestBody @Valid KeywordRequest req,
            @AuthenticationPrincipal CustomUserDetail user) {

        KeywordResponse res =
                keywordService.add(req, user.getUser().getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(res);
    }

    /* ───────── 삭제 ───────── */
    @DeleteMapping("/{keywordId}")
    public ResponseEntity<Void> remove(
            @PathVariable Long keywordId,
            @AuthenticationPrincipal CustomUserDetail user) {

        keywordService.delete(keywordId, user.getUser().getId());
        return ResponseEntity.noContent().build();
    }

    /* ───────── 조회 ───────── */
    @GetMapping
    public ResponseEntity<List<KeywordResponse>> list(
            @AuthenticationPrincipal CustomUserDetail user) {

        return ResponseEntity.ok(
                keywordService.list(user.getUser().getId()));
    }
}
