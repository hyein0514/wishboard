package com.guesthouse.wishboard.dto;

import java.time.LocalDateTime;

public record CommunityPostResponse(
        Long communityId,
        String boardType,           // 정보, Q&A, 트로피, 인원모집 등
        String title,
        String content,
        String diversity,           // 하위분류 예: 수상스키
        LocalDateTime createdAt
) {
    public static CommunityPostResponse from(com.guesthouse.wishboard.entity.Community c) {
        return new CommunityPostResponse(
                c.getCommunityId(),
                c.getType(),
                c.getTitle(),
                c.getContent(),
                c.getCommunityDiversity(),
                c.getCreatedAt()
        );
    }
}
