package com.guesthouse.wishboard.repository;

import com.guesthouse.wishboard.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    /** 최상위 댓글(부모가 없는) 목록 조회 */
    List<Comment> findByCommunity_CommunityIdAndParentCommentIsNullOrderByCreatedAtAsc(Long communityId);

    /** 특정 댓글의 모든 대댓글(부모Comment가 이 commentId인 것) */
    List<Comment> findByParentComment_CommentIdOrderByCreatedAtAsc(Long parentId);
}

