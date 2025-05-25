package com.guesthouse.wishboard.repository;

import com.guesthouse.wishboard.entity.CommentLike_id;
import com.guesthouse.wishboard.entity.Comment_like;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentLikeRepository
        extends JpaRepository<Comment_like, CommentLike_id> {

    boolean existsByUserIdAndCommentId(Long userId, Long commentId);
    long countByCommentId(Long commentId);
}
