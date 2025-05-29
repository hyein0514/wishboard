package com.guesthouse.wishboard.service;

import com.guesthouse.wishboard.dto.PostDetailResponse;
import com.guesthouse.wishboard.dto.PostRequest;
import com.guesthouse.wishboard.dto.PostResponse;
import com.guesthouse.wishboard.dto.PostSummaryResponse;
import com.guesthouse.wishboard.entity.*;
import com.guesthouse.wishboard.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
import javax.sql.DataSource;
import org.springframework.jdbc.core.JdbcTemplate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostService {

    private final CommunityRepository communityRepo;
    private final ImageRepository imageRepo;
    private final UserRepository userRepo;
    private final CommunityLikeRepository likeRepo;
    private final BucketListRepository bucketRepo;
    private final JdbcTemplate jdbcTemplate;
    private final ImageUploadService imageUploadService;

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
    public PostResponse create(PostRequest req, List<MultipartFile> images, Long userId)
    {

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
                .user(author)
                .bucketId(req.bucketId())
                .bucketList(bucket)
                .images(new ArrayList<>())
                .build();

        Community saved = communityRepo.save(entity);

        // 이미지 저장 (예: S3, 로컬 등)
        if (images != null && !images.isEmpty()) {
            for (MultipartFile file : images) {
                String imageUrl;
                try {
                    imageUrl = imageUploadService.upload(file);
                } catch (IOException e) {
                    throw new RuntimeException("이미지 저장 중 오류 발생", e);
                }
                Image image = new Image();
                image.setCommunity(saved);
                image.setImageUrl(imageUrl);
                imageRepo.save(image);
                saved.getImages().add(image);
            }
        }

        return PostResponse.from(saved);
    }


    /* 게시글 수정 */
    @Transactional
    public PostResponse update(Long communityId, PostRequest req, Long userId) {

        Community c = communityRepo.findById(communityId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "post"));
        User author = userRepo.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "user"));

        if (c.getUser() == null) {
            c.setUser(author);
        }

         /* 작성자 본인인지 확인  */
        if (!c.getUser().getId().equals(userId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "not author");
        }


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

        if (!c.getUser().getId().equals(userId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "not author");
        }

        communityRepo.delete(c);
    }

@Transactional
    public long like(Long communityId, Long userId) {
        // 1) 게시글 존재 확인
        if (!communityRepo.existsById(communityId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "post");
        }

        // 2) 이미 눌렀는지 체크 (count = 0 이면 아직 안 눌렀음)
        Integer existsCount = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM `like` WHERE user_id = ? AND community_id = ?",
                Integer.class, userId, communityId);

        if (existsCount != null && existsCount == 0) {
            jdbcTemplate.update(
                    "INSERT INTO `like` (user_id, community_id) VALUES (?, ?)",
                    userId, communityId);
        }

        // 3) 좋아요 총 개수 리턴
        Integer total = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM `like` WHERE community_id = ?",
                Integer.class, communityId);

        return total != null ? total : 0L;
    }

}
