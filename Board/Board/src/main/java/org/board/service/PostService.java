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
public class PostService {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CommentRepository commentRepository;

    // 게시글 쓰기
    @Transactional
    public PostDTO createPost(PostDTO postDTO) {
        User writer = userRepository.findByName(postDTO.getWriter().getName());

        Post post = Post.builder()
                .body(postDTO.getBody())
                .title(postDTO.getTitle())
                .writer(writer).build();
        post = postRepository.save(post);

        return toPostDTO(post);
    }

    // 게시글 수정
    @Transactional
    public PostDTO updatePost(Long postId, PostDTO postDTO) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다."));

        post.setTitle(postDTO.getTitle());
        post.setBody(postDTO.getBody());
        post = postRepository.save(post);

        return toPostDTO(post);
    }

    // 게시글 삭제
    @Transactional
    public void deletePost(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다."));
        postRepository.delete(post);
    }

    // 게시글 목록
    @Transactional(readOnly = true)
    public List<PostResponseDTO> getPostList() {
        return postRepository.findAllWithCommentCount()
                .stream()
                .map(post -> {
                    Post p = (Post) post[0];
                    Integer commentCount = (Integer) post[1];
                    // PostDTO로 변환
                    PostDTO postDTO = new PostDTO(p.getTitle(), p.getBody(), new UserDTO(p.getWriter().getName(), p.getWriter().getEmail()));

                    // PostResponseDTO 생성
                    PostResponseDTO postResponseDTO = new PostResponseDTO();
                    postResponseDTO.setTitle(postDTO.getTitle());
                    postResponseDTO.setBody(postDTO.getBody());
                    postResponseDTO.setWriter(postDTO.getWriter());
                    postResponseDTO.setComments(p.getComments().stream()
                            .map(comment -> new CommentDTO(comment.getBody(),
                                    new UserDTO(comment.getWriter().getName(), comment.getWriter().getEmail())))
                            .toList());
                    postResponseDTO.setCommentsCount(commentCount);
                    return postResponseDTO;
                })
                .toList();
    }


    // 게시글 상세
    @Transactional(readOnly = true)
    public PostResponseDTO getPostDetail(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다."));

        PostResponseDTO postResponseDTO = toPostResponseDTO(post);
        postResponseDTO.setComments(commentRepository.findByPostId(postId).stream()
                .map(this::toCommentDTO)
                .collect(Collectors.toList()));
        postResponseDTO.setCommentsCount(postResponseDTO.getComments().size()); // 댓글 수 설정

        return postResponseDTO;
    }

    // Entity to DTO 변환
    private PostResponseDTO toPostResponseDTO(Post post) {
        PostResponseDTO dto = new PostResponseDTO();
        dto.setTitle(post.getTitle());
        dto.setBody(post.getBody());
        dto.setWriter(toUserDTO(post.getWriter()));
        dto.setComments(post.getComments().stream()
                .map(this::toCommentDTO)
                .collect(Collectors.toList()));
        return dto;
    }

    private PostDTO toPostDTO(Post post) {
        PostDTO dto = new PostDTO();
        dto.setTitle(post.getTitle());
        dto.setBody(post.getBody());
        dto.setWriter(toUserDTO(post.getWriter()));
        return dto;
    }

    private CommentDTO toCommentDTO(Comment comment) {
        CommentDTO dto = new CommentDTO();
        dto.setBody(comment.getBody());
        dto.setWriter(new UserDTO(comment.getWriter().getName(), comment.getWriter().getEmail()));
        return dto;
    }

    private UserDTO toUserDTO(User user) {
        if (user == null) {
            return null; // Null 처리
        }
        UserDTO dto = new UserDTO();
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        return dto;
    }
}
