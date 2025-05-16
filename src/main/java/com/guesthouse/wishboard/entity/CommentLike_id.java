package com.guesthouse.wishboard.entity;

import lombok.*;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentLike_id implements Serializable {
    private String userId;
    private Long commentId;
}
