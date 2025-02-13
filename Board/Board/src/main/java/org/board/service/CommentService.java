package org.board.service;

import org.board.dto.*;
import org.board.repository.CommentRepository;
import org.board.repository.PostRepository;
import org.board.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    // 댓글 목록
    @Transactional(readOnly = true)
    public List<CommentDTO> getCommentList(Long postId) {
        return commentRepository.findByPostId(postId).stream()
                .map(this::toCommentDTO)
                .collect(Collectors.toList());
    }

    // 댓글 쓰기
    @Transactional
    public CommentDTO createComment(Long postId, CommentDTO commentDTO) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다."));
        User writer = userRepository.findById(post.getWriter().getId())
                .orElseThrow(() -> new IllegalArgumentException("작성자 정보가 없습니다."));

        Comment comment = Comment.builder()
                .body(commentDTO.getBody())
                .writer(writer)
                .post(post).build();
        comment = commentRepository.save(comment);

        return toCommentDTO(comment);
    }

    // 댓글 수정
    @Transactional
    public CommentDTO updateComment(Long commentId, CommentDTO commentDTO) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("댓글을 찾을 수 없습니다."));

        comment.setBody(commentDTO.getBody());
        comment = commentRepository.save(comment);

        return toCommentDTO(comment);
    }

    // 댓글 삭제
    @Transactional
    public void deleteComment(Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("댓글을 찾을 수 없습니다."));
        commentRepository.delete(comment);
    }

    // Entity to DTO 변환
    private CommentDTO toCommentDTO(Comment comment) {
        CommentDTO dto = new CommentDTO();
        dto.setBody(comment.getBody());
        dto.setWriter(new UserDTO(comment.getWriter().getName(), comment.getWriter().getEmail()));
        return dto;
    }
}
