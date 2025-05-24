package com.guesthouse.wishboard.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.guesthouse.wishboard.entity.Community;

public interface  CommunityRepository extends JpaRepository<Community, Long> {
    List<Community> findByTitleContainingIgnoreCase(String keyword);
}
