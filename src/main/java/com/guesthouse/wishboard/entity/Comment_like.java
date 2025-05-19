package com.guesthouse.wishboard.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "comment_like")
@IdClass(CommentLike_id.class)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Comment_like {

    @Id
    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Id
    @Column(name = "comment_id", nullable = false)
    private Long commentId;

    @ManyToOne
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "comment_id", insertable = false, updatable = false)
    private Comment comment;

}
