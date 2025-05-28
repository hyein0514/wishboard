package com.guesthouse.wishboard.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BucketListLogResponseDto {
    private Long logId;
    private String createdAt;
    private String image;
    private String content;
}



