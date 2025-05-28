package com.guesthouse.wishboard.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BucketListRequestDto {
    private String title;
    private String category;
    private String targetDate;  // "yyyy-MM-dd" 형식
    private String image;       // URL 형식
    private String reason;
    private String resolution;
}
