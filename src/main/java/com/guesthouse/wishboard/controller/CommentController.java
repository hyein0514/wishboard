package com.guesthouse.wishboard.controller;

import com.guesthouse.wishboard.dto.CommentRequest;
import com.guesthouse.wishboard.dto.CommentResponse;
import com.guesthouse.wishboard.jwt.CustomUserDetail;
import com.guesthouse.wishboard.service.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    private Long currentUserId() { return 1L; }

    /* 댓글 작성 */
    @PostMapping("/posts/{communityId}/comments")
    public ResponseEntity<CommentResponse> addComment(
            @PathVariable Long communityId,
            @RequestBody @Valid CommentRequest req,
            @AuthenticationPrincipal CustomUserDetail user) {

        CommentResponse res =
                commentService.addComment(communityId, req, user.getUser().getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(res);
    }

    /* 대댓글 작성 */
    @PostMapping("/comments/{commentId}/replies")
    public ResponseEntity<CommentResponse> addReply(
            @PathVariable Long commentId,
            @RequestBody @Valid CommentRequest req,
            @AuthenticationPrincipal CustomUserDetail user) {

        CommentResponse res =
                commentService.addReply(commentId, req, user.getUser().getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(res);
    }

    /* 수정 */
    @PatchMapping("/comments/{commentId}")
    public ResponseEntity<CommentResponse> edit(
            @PathVariable Long commentId,
            @RequestBody @Valid CommentRequest req,
            @AuthenticationPrincipal CustomUserDetail user) {

        CommentResponse res =
                commentService.edit(commentId, req, user.getUser().getId());
        return ResponseEntity.ok(res);
    }

    /* 삭제 */
    @DeleteMapping("/comments/{commentId}")
    public ResponseEntity<Void> delete(
            @PathVariable Long commentId,
            @AuthenticationPrincipal CustomUserDetail user) {

        commentService.delete(commentId, user.getUser().getId());
        return ResponseEntity.noContent().build();
    }

    /* 좋아요 (댓글/대댓글 공통) */
    @PostMapping("/comments/{commentId}/like")
    public ResponseEntity<Map<String, Long>> like(
            @PathVariable Long commentId,
            @AuthenticationPrincipal CustomUserDetail user) {

        long cnt = commentService.likeComment(commentId, user.getUser().getId());
        return ResponseEntity.ok(Map.of(
                "commentId", commentId,
                "likeCount", cnt
        ));
    }
}

