package com.guesthouse.wishboard.dto;

import com.guesthouse.wishboard.entity.Community_scrap;

public record CommunityScrapResponse(Long scrapId, String communityName) {
    public static CommunityScrapResponse from(Community_scrap scrap) {
        return new CommunityScrapResponse(scrap.getCommunityScrapId(),
                scrap.getCommunityName());
    }
}
