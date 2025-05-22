package com.guesthouse.wishboard.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
public class TrophyDetail1Dto { //트로피 상세페이지 첫화면
    private Long bucketId;
    private String title;
    private String category;
    private Date achievedAt;
    private Date createdAt;
    private String reason;
    private String resolution;
    private String image;
    private Long userId;
}
