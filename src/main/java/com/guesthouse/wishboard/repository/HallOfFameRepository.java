package com.guesthouse.wishboard.repository;

import com.guesthouse.wishboard.dto.HallOfFameDto;
import com.guesthouse.wishboard.entity.Community;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface HallOfFameRepository extends Repository<Community, Long> {

    // 이번달에 생성된 커뮤니티 글 중 트로피게시판에 올라간 것중 좋아요 수가 많은 상위 10개의 게시글 조회
    @Query("SELECT new com.guesthouse.wishboard.dto.HallOfFameDto(c.communityId, u.Id, u.nickname, b.bucketId) " +
       "FROM Community c " +
       "JOIN c.bucketList b " +//user,bucketList, Community 중첩 join
       "JOIN b.user u " +
       "WHERE c.communityType = '트로피' " +
       "AND FUNCTION('YEAR', c.createdAt) = FUNCTION('YEAR', CURRENT_DATE) " +
       "AND FUNCTION('MONTH', c.createdAt) = FUNCTION('MONTH', CURRENT_DATE) " +
       "ORDER BY (SELECT COUNT(l) FROM Like l WHERE l.communityId = c.communityId) DESC")
    List<HallOfFameDto> findTop10Trophies(Pageable pageable);

}