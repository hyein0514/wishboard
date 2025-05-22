package com.guesthouse.wishboard.service;

import com.guesthouse.wishboard.dto.CommentRequest;
import com.guesthouse.wishboard.dto.CommentResponse;
import com.guesthouse.wishboard.entity.Comment;
import com.guesthouse.wishboard.entity.Comment_like;
import com.guesthouse.wishboard.entity.Community;
import com.guesthouse.wishboard.entity.User;
import com.guesthouse.wishboard.repository.CommentLikeRepository;
import com.guesthouse.wishboard.repository.CommentRepository;
import com.guesthouse.wishboard.repository.CommunityRepository;
import com.guesthouse.wishboard.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentService {

    private final CommentRepository commentRepo;
    private final CommentLikeRepository likeRepo;
    private final CommunityRepository communityRepo;
    private final UserRepository userRepo;

    /* 댓글 작성 */
    @Transactional
    public CommentResponse addComment(Long communityId,
                                      CommentRequest req,
                                      Long userId) {

        Community post = communityRepo.findById(communityId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "post"));

        User author = userRepo.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "user"));

        Comment toSave = commentRepo.save(Comment.builder()
                .communityId(post.getCommunityId())
                .userId(author.getId())
                .community(post)
                .user(author)
                .comment(req.content())
                .build());
        Comment saved = commentRepo.save(toSave);

        return CommentResponse.from(saved, 0);
    }

    /* 대댓글 작성 */
    @Transactional
    public CommentResponse addReply(Long parentId,
                                    CommentRequest req,
                                    Long userId) {

        Comment parent = commentRepo.findById(parentId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "parent"));

        User author = userRepo.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "user"));

        Comment toReply = commentRepo.save(Comment.builder()
                .parentComment(parent)
                .communityId(parent.getCommunityId())
                .userId(author.getId())
                .community(parent.getCommunity())
                .parentCommentId(parent.getCommentId())
                .user(author)
                .comment(req.content())
                .build());
        Comment reply = commentRepo.save(toReply);

        return CommentResponse.from(reply, 0);
    }

    /* 수정 */
    @Transactional
    public CommentResponse edit(Long commentId,
                                CommentRequest req,
                                Long userId) {

        Comment c = commentRepo.findById(commentId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "comment"));

        if (!c.getUser().getId().equals(userId))
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "not author");

        c.setComment(req.content());
        long likes = likeRepo.countByCommentId(commentId);
        return CommentResponse.from(c, likes);
    }

    /* 삭제 (소프트 삭제가 필요 없다면 바로 delete) */
    @Transactional
    public void delete(Long commentId, Long userId) {
        Comment c = commentRepo.findById(commentId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "comment"));

        if (!c.getUser().getId().equals(userId))
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "not author");

        commentRepo.delete(c);
    }

    /* 좋아요 (중복 무시) */
    @Transactional
    public long like(Long commentId, Long userId) {

        if (!likeRepo.existsByUserIdAndCommentId(userId, commentId)) {
            likeRepo.save(new Comment_like(userId, commentId));
        }
        return likeRepo.countByCommentId(commentId);
    }

    /* 글에 달린 댓글 + 대댓글 트리 조회용 */
    public List<CommentResponse> listComments(Long communityId) {
        return commentRepo.findByCommunity_CommunityIdAndParentCommentIsNullOrderByCreatedAtAsc(communityId)
                .stream()
                .map(c -> CommentResponse.from(
                        c,
                        likeRepo.countByCommentId(c.getCommentId())
                ))
                .toList();
    }
}

