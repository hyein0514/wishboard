package com.guesthouse.wishboard.repository;

import com.guesthouse.wishboard.entity.Like;
import com.guesthouse.wishboard.entity.Like_id;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CommunityLikeRepository
        extends JpaRepository<Like, Like_id> {

    /** 해당 사용자가 해당 게시글에 좋아요를 눌렀는지 */
    @Query("""
      SELECT CASE WHEN COUNT(l) > 0 THEN TRUE ELSE FALSE END
      FROM Like l
      WHERE l.user.Id = :userId
        AND l.community.communityId = :communityId
    """)
    boolean existsByUserAndCommunity(
            @Param("userId") Long userId,
            @Param("communityId") Long communityId
    );

    /** 게시글별 총 좋아요 수 */
    @Query("SELECT COUNT(l) FROM Like l WHERE l.community.communityId = :communityId")
    long countByCommunityId(@Param("communityId") Long communityId);
}
