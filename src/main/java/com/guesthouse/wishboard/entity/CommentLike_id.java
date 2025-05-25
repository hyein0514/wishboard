package com.guesthouse.wishboard.entity;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
public class CommentLike_id implements Serializable {

    private String userId;

    private Long commentId;
}
