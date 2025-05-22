package com.guesthouse.wishboard.dto;

import com.guesthouse.wishboard.entity.Community;

public record PostResponse(
        Long   postId,
        String type,
        String communityType,
        String diversity,
        String title,
        String content
) {
    public static PostResponse from(Community c) {
        return new PostResponse(
                c.getCommunityId(),
                c.getType(),
                c.getCommunityType(),
                c.getCommunityDiversity(),
                c.getTitle(),
                c.getContent()
        );
    }
}
