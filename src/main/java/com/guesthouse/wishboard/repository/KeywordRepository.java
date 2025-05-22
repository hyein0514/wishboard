package com.guesthouse.wishboard.repository;

import com.guesthouse.wishboard.entity.Keyword;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface KeywordRepository extends JpaRepository<Keyword, Long> {

    boolean existsByUserIdAndKeyword(Long userId, String keyword);

    List<Keyword> findByUserIdOrderByKeywordIdDesc(Long userId);
}

