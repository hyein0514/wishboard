package com.guesthouse.wishboard.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "`like`") // like는 MySQL 예약어이기 때문에 `like`로 감싸는 것이 안전
@IdClass(Like_id.class)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Like {

    @Id
    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Id
    @Column(name = "user_id", nullable = false)
    private Long communityId;

    @ManyToOne
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "community_id", insertable = false, updatable = false)
    private Community community;
}
