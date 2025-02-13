package org.board.controller;

import lombok.extern.log4j.Log4j2;
import org.board.dto.*;
import org.board.repository.CommentRepository;
import org.board.repository.PostRepository;
import org.board.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Commit;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Commit
@Log4j2
public class CommentControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private CommentRepository commentRepository;

    private UserDTO userDTO;

    @BeforeEach
    public void setUp() {
        // 초기 데이터 세팅
        List<User> users = userRepository.findAll();
        User user = users.get(0);
        userDTO = new UserDTO();
        userDTO.setName(user.getName());
        userDTO.setEmail(user.getEmail());

        // 기존에 있던 댓글들을 삭제
        commentRepository.deleteAll();

        // 기존에 있던 게시글들을 삭제
        postRepository.deleteAll();
    }

    @Test
    public void testCreateComment() throws Exception {
        // 게시글 생성
        Post post = new Post();
        post.setTitle("테스트 제목");
        post.setBody("테스트 내용");
        post.setWriter(userRepository.findAll().get(0));
        post = postRepository.save(post);

        // 댓글 작성
        CommentDTO commentDTO = new CommentDTO();
        commentDTO.setBody("테스트 댓글");

        mockMvc.perform(post("/api/comments/post/{postId}", post.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"body\":\"테스트 댓글\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.body").value("테스트 댓글"))
                .andExpect(jsonPath("$.writer.name").value(userDTO.getName()))
                .andExpect(jsonPath("$.writer.email").value(userDTO.getEmail()))
                .andDo(print());
    }

    @Test
    public void testUpdateComment() throws Exception {
        // 게시글 생성
        Post post = new Post();
        post.setTitle("테스트 제목");
        post.setBody("테스트 내용");
        post.setWriter(userRepository.findAll().get(0));
        post = postRepository.save(post);

        // 댓글 생성
        Comment comment = new Comment();
        comment.setBody("원래 댓글");
        comment.setWriter(userRepository.findAll().get(1));
        comment.setPost(post);
        comment = commentRepository.save(comment);

        // 댓글 수정
        CommentDTO commentDTO = new CommentDTO();
        commentDTO.setBody("수정된 댓글");

        mockMvc.perform(put("/api/comments/{id}", comment.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"body\":\"수정된 댓글\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.body").value("수정된 댓글"))
                .andDo(print());
    }

    @Test
    public void testDeleteComment() throws Exception {
        // 게시글 생성
        Post post = new Post();
        post.setTitle("테스트 제목");
        post.setBody("테스트 내용");
        post.setWriter(userRepository.findAll().get(0));
        post = postRepository.save(post);

        // 댓글 생성
        Comment comment = new Comment();
        comment.setBody("삭제할 댓글");
        comment.setWriter(userRepository.findAll().get(1));
        comment.setPost(post);
        comment = commentRepository.save(comment);

        // 댓글 삭제
        mockMvc.perform(delete("/api/comments/{id}", comment.getId()))
                .andExpect(status().isNoContent());

        // 데이터베이스에서 실제 댓글 삭제 확인
        List<Comment> comments = commentRepository.findAll();
        Assertions.assertEquals(comments.size(), 0);
    }

    @Test
    public void testListCommentsByPost() throws Exception {
        // 게시글 생성
        Post post = new Post();
        post.setTitle("테스트 제목");
        post.setBody("테스트 내용");
        post.setWriter(userRepository.findAll().get(0));
        post = postRepository.save(post);

        // 댓글 생성
        Comment comment1 = new Comment();
        comment1.setBody("댓글 1");
        comment1.setWriter(userRepository.findAll().get(1));
        comment1.setPost(post);
        commentRepository.save(comment1);

        Comment comment2 = new Comment();
        comment2.setBody("댓글 2");
        comment2.setWriter(userRepository.findAll().get(2));
        comment2.setPost(post);
        commentRepository.save(comment2);

        // 댓글 목록 조회
        mockMvc.perform(get("/api/comments/post/{postId}", post.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].body").value("댓글 1"))
                .andExpect(jsonPath("$[1].body").value("댓글 2"))
                .andDo(print());
    }
}
