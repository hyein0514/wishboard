package com.guesthouse.wishboard.repository;

import com.guesthouse.wishboard.entity.Community_scrap;
import com.guesthouse.wishboard.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface Community_scrapRepository
        extends JpaRepository<Community_scrap, Long> {

    /* ① 중복 여부 */
    @Query("""
        select count(s) > 0
        from Community_scrap s
        where s.communityName = :communityName
          and s.user.id       = :userPk
    """)
    boolean exists(@Param("communityName") String communityName,
                   @Param("userPk")        Long   userPk);

    /* ② 삭제·단건 조회 */
    @Query("""
        select s
        from Community_scrap s
        where s.communityName = :communityName
          and s.user.id       = :userPk
    """)
    Optional<Community_scrap> findOne(@Param("communityName") String communityName,
                                      @Param("userPk")        Long   userPk);

    /* ③ 목록 조회 */
    @Query("""
        select s
        from Community_scrap s
        where s.user.id = :userPk
        order by s.communityScrapId desc
    """)
    Page<Community_scrap> findPage(@Param("userPk") Long userPk, Pageable pageable);
}



