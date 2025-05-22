package com.guesthouse.wishboard.dto;

import com.guesthouse.wishboard.entity.Comment;

import java.time.LocalDateTime;
import java.time.ZoneId;

public record CommentResponse(
        Long commentId,
        Long parentCommentId,
        String authorNickname,
        String content,
        LocalDateTime createdAt,
        long likeCount
) {
    public static CommentResponse from(Comment c, long likeCount) {
        return new CommentResponse(
                c.getCommentId(),
                c.getParentCommentId(),
                c.getUser().getNickname(),
                c.getComment(),
                c.getCreatedAt(),
                likeCount
        );
    }
}

