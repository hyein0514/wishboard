package com.guesthouse.wishboard.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
public class TrophyDetail2Dto {//트로피 상세페이지 첫화면 이후
    // BucketList 정보
    private Long bucketId;
    private String title;
    private String category;
    private Date achievedAt;
    private Date bucketCreatedAt;

    // BucketList_log 정보
    private Long logId;
    private Date logCreatedAt;
    private String image;
    private String content;
}
