package com.guesthouse.wishboard.repository;

import com.guesthouse.wishboard.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ImageRepository extends JpaRepository<Image, Long> {
    List<Image> findByCommunity_CommunityId(Long communityId);
}
