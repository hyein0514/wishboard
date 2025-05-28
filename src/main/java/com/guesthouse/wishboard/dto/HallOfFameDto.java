package com.guesthouse.wishboard.dto;

import lombok.Data;

@Data
public class HallOfFameDto {
    private Long communityId;
    private Long userId;
    private String nickname;
    private Long bucketId;

    public HallOfFameDto(Long communityId, Long userId, String nickname, Long bucketId) {
        this.communityId = communityId;
        this.userId = userId;
        this.nickname = nickname;
        this.bucketId = bucketId;
    }
}
