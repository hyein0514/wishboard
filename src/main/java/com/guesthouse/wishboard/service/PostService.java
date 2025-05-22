package com.guesthouse.wishboard.service;

import com.guesthouse.wishboard.dto.PostDetailResponse;
import com.guesthouse.wishboard.dto.PostRequest;
import com.guesthouse.wishboard.dto.PostResponse;
import com.guesthouse.wishboard.dto.PostSummaryResponse;
import com.guesthouse.wishboard.entity.BucketList;
import com.guesthouse.wishboard.entity.Community;
import com.guesthouse.wishboard.entity.User;
import com.guesthouse.wishboard.entity.Like;
import com.guesthouse.wishboard.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostService {

    private final CommunityRepository communityRepo;
    private final ImageRepository imageRepo;
    private final UserRepository userRepo;
    private final CommunityLikeRepository likeRepo;
    private final BucketListRepository bucketRepo;

    public Page<PostSummaryResponse> listPosts(
            String communityType,
            String boardType,
            Pageable pageable
    ) {
        return communityRepo
                .findByCommunityTypeAndType(communityType, boardType, pageable)
                .map(PostSummaryResponse::from);
    }

    public PostDetailResponse getPost(Long communityId) {
        Community c = communityRepo.findById(communityId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Post not found: " + communityId
                ));
        var images = imageRepo.findByCommunity_CommunityId(communityId)
                .stream()
                .map(i -> i.getImageUrl())
                .toList();
        return PostDetailResponse.from(c, images);
    }
    /* 게시글 작성 */
    @Transactional
    public PostResponse create(PostRequest req, Long userId) {

        User author = userRepo.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "User not found: " + userId
        ));

        BucketList bucket = bucketRepo.findById(req.bucketId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "bucket"));

        Community entity = Community.builder()
                .type(req.type())
                .communityType(req.communityType())
                .communityDiversity(req.diversity())
                .title(req.title())
                .content(req.content())
                .bucketId(req.bucketId())
                .build();

        Community saved = communityRepo.save(entity);
        return PostResponse.from(saved);
    }


    /* 게시글 수정 */
    @Transactional
    public PostResponse update(Long communityId, PostRequest req, Long userId) {

        Community c = communityRepo.findById(communityId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "post"));

        // 밑에 부분은 나중에 버킷 추가되면 복구하기
//        Bucket_list bucket = bucketRepo.findById(c.getBucketId())
//                .orElseThrow(() -> new ResponseStatusException(
//                        HttpStatus.NOT_FOUND, "bucket"));
//
//        if (!bucket.getUser().getUserId().equals(userId)) {
//            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "not author");
//        }

        if (!userId.equals("1111")) { /* 예시 */ }

        c.setType(req.type());
        c.setCommunityType(req.communityType());
        c.setCommunityDiversity(req.diversity());
        c.setTitle(req.title());
        c.setContent(req.content());
        return PostResponse.from(c);
    }

    /* 게시글 삭제 */
    @Transactional
    public void delete(Long communityId, Long userId) {
        Community c = communityRepo.findById(communityId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "post"));
//        if (!c.getBucket().getUser().getUserId().equals(userId))
//            throw new ResponseStatusException(
//                    HttpStatus.FORBIDDEN, "not author");
        communityRepo.delete(c);
    }

    @Transactional
    /* 게시글 좋아요 (토글 아님, 중복 시 무시) */
    public long like(Long communityId, Long userId) {

        if (!communityRepo.existsById(communityId))
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "post");

        // 이미 눌렀는지 검사
        if (!likeRepo.existsByUserIdAndCommunityId(userId, communityId)) {
            likeRepo.save(new Like(userId, communityId));  // ← 이 부분 수정
        }
        return likeRepo.countByCommunityId(communityId);
    }
}
