package com.guesthouse.wishboard.entity;
import lombok.*;
import java.io.Serializable;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Like_id implements Serializable {
    private Long userId;
    private Long communityId;
}
