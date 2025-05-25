package com.guesthouse.wishboard.service;

import com.guesthouse.wishboard.dto.CommunityScrapResponse;
import com.guesthouse.wishboard.entity.Community_scrap;
import com.guesthouse.wishboard.entity.User;
import com.guesthouse.wishboard.repository.Community_scrapRepository;
import com.guesthouse.wishboard.repository.UserRepository;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


import com.guesthouse.wishboard.dto.CommunityScrapResponse;
import com.guesthouse.wishboard.entity.Community_scrap;
import com.guesthouse.wishboard.entity.User;
import com.guesthouse.wishboard.repository.Community_scrapRepository;
import com.guesthouse.wishboard.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
@Transactional
public class CommunityScrapService {

    private final Community_scrapRepository scrapRepo;
    private final UserRepository userRepo;

    private User getUser(Long userPk) {
        return userRepo.findById(userPk)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "user"));
    }

    public CommunityScrapResponse addScrap(String communityName, Long userPk) {

        if (scrapRepo.exists(communityName, userPk)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "이미 존재");
        }
        Community_scrap saved =
                scrapRepo.save(new Community_scrap(communityName, getUser(userPk)));
        return CommunityScrapResponse.from(saved);
    }

    public void deleteScrap(String communityName, Long userPk) {
        Community_scrap scrap = scrapRepo.findOne(communityName, userPk)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "scrap not found"));
        scrapRepo.delete(scrap);
    }

    @Transactional(readOnly = true)
    public Page<CommunityScrapResponse> myScraps(Long userPk, Pageable pageable) {
        return scrapRepo.findPage(userPk, pageable)
                .map(CommunityScrapResponse::from);
    }
}





