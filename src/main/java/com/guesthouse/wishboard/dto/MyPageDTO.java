package com.guesthouse.wishboard.dto;

import com.guesthouse.wishboard.entity.BucketList;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.*;

import java.util.Date;

@AllArgsConstructor
@Builder
@Getter
@Setter
@NoArgsConstructor
public class MyPageDTO {
    private Long communityId;
    private String communityType;
    private String type;
    private String title;
    private String content;
    private Date createdAt;
    private String communityDiversity;
    private String img;
    private String writerNickName;
    private int commentNum;
    private int likeNum;
}
