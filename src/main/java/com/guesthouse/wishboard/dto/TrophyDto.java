package com.guesthouse.wishboard.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
public class TrophyDto { //완료된 버킷리스트
    private Long bucketId;
    private String title;
    private String category;
    private Date achievedAt;
    private String status;
    private Date createdAt;
    private Long userId;
    private String trophy;
    
}
