package com.guesthouse.wishboard.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class BucketListResponseDto {
    private Long bucketId;
    private String title;
    private String category;
    private String targetDate;
    private String image;
    private Boolean pinToTop;
    private String dDay;

    @Builder
    public BucketListResponseDto(Long bucketId, String title, String category, String targetDate, String image, Boolean pinToTop, String dDay) {
        this.bucketId = bucketId;
        this.title = title;
        this.category = category;
        this.targetDate = targetDate;
        this.image = image;
        this.pinToTop = pinToTop;
        this.dDay = dDay;
    }
}

