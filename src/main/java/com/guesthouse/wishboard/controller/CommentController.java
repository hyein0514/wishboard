package com.guesthouse.wishboard.controller;

import com.guesthouse.wishboard.dto.CommentRequest;
import com.guesthouse.wishboard.dto.CommentResponse;
import com.guesthouse.wishboard.service.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
            @RequestBody @Valid CommentRequest req) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(commentService.addComment(communityId, req, currentUserId()));
    }

    /* 대댓글 작성 */
    @PostMapping("/comments/{commentId}/replies")
    public ResponseEntity<CommentResponse> addReply(
            @PathVariable Long commentId,
            @RequestBody @Valid CommentRequest req) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(commentService.addReply(commentId, req, currentUserId()));
    }

    /* 수정 */
    @PatchMapping("/comments/{commentId}")
    public ResponseEntity<CommentResponse> edit(
            @PathVariable Long commentId,
            @RequestBody @Valid CommentRequest req) {

        return ResponseEntity.ok(
                commentService.edit(commentId, req, currentUserId()));
    }

    /* 삭제 */
    @DeleteMapping("/comments/{commentId}")
    public ResponseEntity<Void> delete(@PathVariable Long commentId) {
        commentService.delete(commentId, currentUserId());
        return ResponseEntity.noContent().build();
    }

    /* 좋아요 (댓글/대댓글 공통) */
    @PostMapping("/comments/{commentId}/like")
    public ResponseEntity<Map<String, Long>> like(@PathVariable Long commentId) {
        long cnt = commentService.like(commentId, currentUserId());
        return ResponseEntity.ok(Map.of("commentId", commentId, "likeCount", cnt));
    }
}

