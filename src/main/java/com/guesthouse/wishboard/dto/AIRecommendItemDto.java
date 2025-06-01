package com.guesthouse.wishboard.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AIRecommendItemDto  {
    private String category;
    private String title;
    private String authorName;
    //private String image;
    private int likeCount;
    private int commentCount;
    private Long communityId;
    private Long bucketId;
    private String communityDiversity; 
}
