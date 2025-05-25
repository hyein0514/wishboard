package com.guesthouse.wishboard.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class BucketListDetailDto {
    private Long bucketId;
    private String title;
    private String category;
    private String targetDate;
    private String image;
    private String reason;
    private String resolution;
    private Boolean pinToTop;
    private String status;

    @Builder
    public BucketListDetailDto(Long bucketId, String title, String category, String targetDate,
                               String image, String reason, String resolution,
                               Boolean pinToTop, String status) {
        this.bucketId = bucketId;
        this.title = title;
        this.category = category;
        this.targetDate = targetDate;
        this.image = image;
        this.reason = reason;
        this.resolution = resolution;
        this.pinToTop = pinToTop;
        this.status = status;
    }
}

