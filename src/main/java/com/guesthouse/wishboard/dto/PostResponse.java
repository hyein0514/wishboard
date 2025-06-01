package com.guesthouse.wishboard.dto;

import com.guesthouse.wishboard.entity.Community;

import java.time.LocalDateTime;
import java.util.List;

public record PostResponse(
        Long   postId,
        String type,
        String communityType,
        String diversity,
        String title,
        String content,
        String authorNickname,         // 추가!
        LocalDateTime createdAt,       // 추가!
        List<String> imageUrls,        // 추가!
        int likeCount,                 // 추가!
        int commentCount               // 추가!
) {
    public static PostResponse from(Community c) {
        List<String> imageUrls = (c.getImages() == null) ? List.of()
                : c.getImages().stream().map(img -> img.getImageUrl()).toList();

        return new PostResponse(
                c.getCommunityId(),
                c.getType(),
                c.getCommunityType(),
                c.getCommunityDiversity(),
                c.getTitle(),
                c.getContent(),
                c.getUser().getNickname(),                                   // 작성자 닉네임
                c.getCreatedAt(),                                            // 생성일
                c.getImages().stream().map(img -> img.getImageUrl()).toList(), // 이미지 URL
                c.getLikes() != null ? c.getLikes().size() : 0,              // 좋아요 수
                c.getComments() != null ? c.getComments().size() : 0
        );
    }
}
