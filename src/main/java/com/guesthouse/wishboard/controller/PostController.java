package com.guesthouse.wishboard.controller;

import com.guesthouse.wishboard.dto.PostDetailResponse;
import com.guesthouse.wishboard.dto.PostRequest;
import com.guesthouse.wishboard.dto.PostResponse;
import com.guesthouse.wishboard.dto.PostSummaryResponse;
import com.guesthouse.wishboard.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    private Long currentUserId() { return 1L; }

    /** 게시글 목록 조회 **/
    @GetMapping
    public ResponseEntity<Page<PostSummaryResponse>> list(
            @RequestParam String communityType,
            @RequestParam String boardType,
            Pageable pageable
    ) {
        Page<PostSummaryResponse> page =
                postService.listPosts(communityType, boardType, pageable);
        return ResponseEntity.ok(page);
    }

    /** 게시글 상세 조회 **/
    @GetMapping("/{communityId}")
    public ResponseEntity<PostDetailResponse> detail(
            @PathVariable Long communityId
    ) {
        return ResponseEntity.ok(
                postService.getPost(communityId)
        );
    }

    /* ───────── 게시글 작성 ───────── */
    @PostMapping
    public ResponseEntity<PostResponse> create(@RequestBody @Valid PostRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(postService.create(req, currentUserId()));
    }

    /* ───────── 게시글 수정 ───────── */
    @PatchMapping("/{communityId}")
    public ResponseEntity<PostResponse> update(
            @PathVariable Long communityId,
            @RequestBody @Valid PostRequest req) {

        return ResponseEntity.ok(postService.update(communityId, req, currentUserId()));
    }

    /* ───────── 게시글 삭제 ───────── */
    @DeleteMapping("/{communityId}")
    public ResponseEntity<Void> delete(@PathVariable Long communityId) {
        postService.delete(communityId, currentUserId());
        return ResponseEntity.noContent().build();
    }

    /* ───────── 게시글 좋아요 ───────── */
    @PostMapping("/{communityId}/like")
    public ResponseEntity<Map<String, Long>> like(@PathVariable Long communityId) {
        long count = postService.like(communityId, currentUserId());
        return ResponseEntity.ok(Map.of("postId", communityId, "likeCount", count));
    }
}
