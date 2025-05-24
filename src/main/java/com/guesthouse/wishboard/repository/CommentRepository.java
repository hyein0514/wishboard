package com.guesthouse.wishboard.repository;

import com.guesthouse.wishboard.entity.Comment;
import com.guesthouse.wishboard.entity.Community;
import com.guesthouse.wishboard.entity.Like;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface CommentRepository extends JpaRepository<Comment,Long> {

    /* 댓글 트리 용 */
    List<Comment> findByCommunity_CommunityIdAndParentCommentIsNullOrderByCreatedAtAsc(Long communityId);
    List<Comment> findByParentComment_CommentIdOrderByCreatedAtAsc(Long parentId);


    /* 마이페이지 용 */
    @Query( "SELECT DISTINCT cm " +
            "FROM Comment cm " +
            "JOIN cm.community c " +
            "LEFT JOIN c.images i " +
            "WHERE cm.userId = :userId "+
            "ORDER BY cm.communityId "
    )
    List<Comment> findByuserIdAboutComment(
            @Param("userId") Long userId
    );

    @Query("""
        SELECT DISTINCT c
        FROM Community c
        LEFT JOIN FETCH c.images
        WHERE c.communityId IN (
            SELECT l.community.communityId
            FROM Like l
            WHERE l.userId = :userId
        )
    """)
    List<Community> findByuserIdAboutLike(
            @Param("userId") Long userId
    );

        @Query("""
        SELECT DISTINCT c
        FROM Community c
        LEFT JOIN FETCH c.images
        WHERE c.user.Id = :userId
    """)
    List<Community> findByUserIdAboutPost(
            @Param("userId") Long userId
    );


}
