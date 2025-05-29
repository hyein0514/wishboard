package com.guesthouse.wishboard.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.guesthouse.wishboard.dto.PostDetailResponse;
import com.guesthouse.wishboard.dto.PostRequest;
import com.guesthouse.wishboard.dto.PostResponse;
import com.guesthouse.wishboard.dto.PostSummaryResponse;
import com.guesthouse.wishboard.jwt.CustomUserDetail;
import com.guesthouse.wishboard.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.MediaType;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.Principal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    // private Long currentUserId() { return 1L; }

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
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<PostResponse> create(
            @RequestPart("post") String postJson, // String으로 받기!
            @RequestPart(value = "images", required = false) List<MultipartFile> images,
            Authentication authentication) throws IOException {

        System.out.println("post json: " + postJson);

        ObjectMapper mapper = new ObjectMapper();
        PostRequest req = mapper.readValue(postJson, PostRequest.class);

        CustomUserDetail user = (CustomUserDetail) authentication.getPrincipal();

        PostResponse res = postService.create(req, images, user.getUser().getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(res);
    }



    /* ───────── 게시글 수정 ───────── */
    @PatchMapping("/{communityId}")
    public ResponseEntity<PostResponse> update(
            @PathVariable Long communityId,
            @RequestBody @Valid PostRequest req,
            @AuthenticationPrincipal CustomUserDetail user) {

        return ResponseEntity.ok(
                postService.update(communityId, req, user.getUser().getId()));
    }

    /* ───────── 게시글 삭제 ───────── */
    @DeleteMapping("/{communityId}")
    public ResponseEntity<Void> delete(
            @PathVariable Long communityId,
            @AuthenticationPrincipal CustomUserDetail user) {

        postService.delete(communityId, user.getUser().getId());
        return ResponseEntity.noContent().build();
    }

    /* ───────── 게시글 좋아요 ───────── */
    @PostMapping("/{communityId}/like")
    public ResponseEntity<Map<String, Long>> like(
            @PathVariable Long communityId,
            @AuthenticationPrincipal CustomUserDetail user) {

        long cnt = postService.like(communityId, user.getUser().getId());
        return ResponseEntity.ok(Map.of("postId", communityId, "likeCount", cnt));
    }
}
