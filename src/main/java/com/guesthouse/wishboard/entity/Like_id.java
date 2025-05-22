package com.guesthouse.wishboard.entity;
import lombok.*;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Like_id implements Serializable {
    private Long userId;
    private Long communityId;
}
