package com.guesthouse.wishboard.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BucketListHomeDto {
    private Long bucketId;
    private String title;
    private String category;
    private String targetDate;
    private String dday;
}
