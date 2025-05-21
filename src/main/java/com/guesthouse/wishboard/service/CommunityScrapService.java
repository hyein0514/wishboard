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

    // 로그인한 유저 조회, 없으면 404 에러 던짐
    private User getUserOrThrow(String loginUserId) {
        User user = userRepo.findAllByUserId(loginUserId);
        if (user == null) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "User not found: " + loginUserId
            );
        }
        return user;
    }

    public CommunityScrapResponse addScrap(String communityName, String loginUserId) {
        User user = getUserOrThrow(loginUserId);

        if (scrapRepo.existsByCommunityNameAndUser(communityName, user)) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "이미 즐겨찾기에 존재합니다."
            );
        }
        Community_scrap saved = scrapRepo.save(new Community_scrap(communityName, user));
        return CommunityScrapResponse.from(saved);
    }

    public void deleteScrap(String communityName, String loginUserId) {
        User user = getUserOrThrow(loginUserId);

        Community_scrap scrap = scrapRepo
                .findByCommunityNameAndUser(communityName, user)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Scrap not found: " + communityName
                ));

        scrapRepo.delete(scrap);
    }

    @Transactional(readOnly = true)
    public Page<CommunityScrapResponse> myScraps(String loginUserId, Pageable pageable) {
        User user = getUserOrThrow(loginUserId);
        return scrapRepo.findByUser(user, pageable)
                .map(CommunityScrapResponse::from);
    }
}



