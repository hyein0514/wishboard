package com.guesthouse.wishboard.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BucketListLogRequestDto {
    private String createdAt; // yyyy-MM-dd
    private String image;
    private String content;
    private Long bucketId;
}
