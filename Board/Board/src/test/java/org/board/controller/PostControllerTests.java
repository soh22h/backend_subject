package org.board.controller;

import lombok.extern.log4j.Log4j2;
import org.board.dto.*;
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

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Commit
@Log4j2
public class PostControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostRepository postRepository;

    private UserDTO userDTO;

    @BeforeEach
    public void setUp() {
        // 초기 데이터 세팅
        List<User> users = userRepository.findAll();
        User user = users.get(0);
        userDTO = new UserDTO();
        userDTO.setName(user.getName());
        userDTO.setEmail(user.getEmail());

        // 기존에 있던 게시글들을 삭제
        postRepository.deleteAll();
    }

    @Test
    public void testCreatePost() throws Exception {
        // PostDTO 객체 생성
        PostDTO postDTO = new PostDTO();
        postDTO.setTitle("테스트 제목");
        postDTO.setBody("테스트 내용");
        postDTO.setWriter(userDTO);

        // mockMvc를 사용한 POST 요청
        mockMvc.perform(post("/api/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"테스트 제목\"," +
                                "\"body\":\"테스트 내용\"," +
                                "\"writer\":{\"name\":\"" + userDTO.getName() + "\"," +
                                "\"email\":\"" + userDTO.getEmail() + "\"}}"))
                .andExpect(status().isOk())  // HTTP 응답이 200 OK인지 확인
                .andExpect(jsonPath("$.title").value("테스트 제목"))  // 반환된 JSON에 title이 "테스트 제목"인지 확인
                .andExpect(jsonPath("$.body").value("테스트 내용"))   // 반환된 JSON에 body가 "테스트 내용"인지 확인
                .andExpect(jsonPath("$.writer.name").value(userDTO.getName()))  // writer의 name이 올바른지 확인
                .andExpect(jsonPath("$.writer.email").value(userDTO.getEmail()))  // writer의 email이 올바른지 확인
                .andDo(print());  // 결과를 콘솔에 출력
    }

    @Test
    public void testUpdatePost() throws Exception {
        List<User> users = userRepository.findAll();
        User user = users.get(0);
        // 초기 게시글 생성
        Post post = new Post();
        post.setTitle("원래 제목");
        post.setBody("원래 내용");
        post.setWriter(user);
        post = postRepository.save(post);
        log.info("수정 전 게시글: title={}, body={}", post.getTitle(), post.getBody());

        PostDTO postDTO = new PostDTO();
        postDTO.setTitle("수정 제목");
        postDTO.setBody("수정 내용");
        postDTO.setWriter(userDTO);

        // PUT 요청으로 게시글 수정
        mockMvc.perform(put("/api/posts/{id}", post.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"수정 제목\"," +
                                "\"body\":\"수정 내용\"," +
                                "\"writer\":{\"name\":\"" + userDTO.getName() + "\"," +
                                "\"email\":\"" + userDTO.getEmail() + "\"}}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("수정 제목"))
                .andExpect(jsonPath("$.body").value("수정 내용"))
                .andExpect(jsonPath("$.writer.name").value(userDTO.getName()))
                .andExpect(jsonPath("$.writer.email").value(userDTO.getEmail()))
                .andDo(print());
    }

    @Test
    public void testDeletePost() throws Exception {
        List<User> users = userRepository.findAll();
        User user = users.get(0);
        // 초기 게시글 생성
        Post post = new Post();
        post.setTitle("테스트 제목");
        post.setBody("테스트 내용");
        post.setWriter(user);
        post = postRepository.save(post);
        log.info("삭제 전 게시글: title={}, body={}", post.getTitle(), post.getBody());

        // DELETE 요청으로 게시글 삭제
        mockMvc.perform(delete("/api/posts/{id}", post.getId()))
                .andExpect(status().isNoContent());

        // 데이터베이스에서 실제 값 확인
        List<Post> posts = postRepository.findAll();
        Assertions.assertEquals(posts.size(),0);  // 게시글이 삭제되었는지 확인
    }

    @Test
    public void testListPosts() throws Exception {
        // 테스트용 사용자 데이터 준비
        List<User> users = userRepository.findAll();
        List<Post> posts = new ArrayList<>();

        // 초기 게시글 및 댓글 데이터 생성 및 저장
        IntStream.rangeClosed(1, 3).forEach(i -> {
            Post post = Post.builder()
                    .title("제목 " + i)
                    .body("내용 " + i)
                    .writer(users.get(5-i))
                    .build();

            Comment comment1 = Comment.builder()
                    .body("댓글 " + i)
                    .writer(users.get(4-i))
                    .post(post)
                    .build();

            Comment comment2 = Comment.builder()
                    .body("댓글 " + i)
                    .writer(users.get(3-i))
                    .post(post)
                    .build();

            post.setComments(List.of(comment1, comment2));

            postRepository.save(post);
            posts.add(post);
        });

        // GET 요청으로 게시글 목록 조회
        mockMvc.perform(get("/api/posts")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()) // HTTP 응답이 200 OK인지 확인
                .andExpect(jsonPath("$.length()").value(3)) // 응답 JSON 배열 길이가 3인지 확인
                .andExpect(jsonPath("$[0].title").value("제목 1"))
                .andExpect(jsonPath("$[0].body").value("내용 1"))
                .andExpect(jsonPath("$[0].writer.name").value(users.get(4).getName()))
                .andExpect(jsonPath("$[0].writer.email").value(users.get(4).getEmail()))
                .andExpect(jsonPath("$[0].comments.length()").value(2)) // 댓글 개수 확인
                .andExpect(jsonPath("$[0].comments[0].body").value("댓글 1"))
                .andExpect(jsonPath("$[0].comments[1].body").value("댓글 1"))
                .andExpect(jsonPath("$[1].title").value("제목 2"))
                .andExpect(jsonPath("$[1].body").value("내용 2"))
                .andExpect(jsonPath("$[1].commentsCount").value(2)) // 댓글 개수 확인
                .andExpect(jsonPath("$[2].title").value("제목 3"))
                .andExpect(jsonPath("$[2].body").value("내용 3"))
                .andExpect(jsonPath("$[2].comments.length()").value(2)) // 댓글 개수 확인
                .andDo(print());
    }

    @Test
    public void testGetPost() throws Exception {
        // 게시글 상세 DTO 준비
        List<User> users = userRepository.findAll();

        // 초기 게시글 및 댓글 데이터 생성 및 저장
        Post post = Post.builder()
                .title("테스트 제목")
                .body("테스트 내용")
                .writer(users.get(3))
                .build();

        Comment comment1 = Comment.builder()
                .body("테스트 댓글 1")
                .writer(users.get(1))
                .post(post)
                .build();

        Comment comment2 = Comment.builder()
                .body("테스트 댓글 2")
                .writer(users.get(4))
                .post(post)
                .build();

        post.setComments(List.of(comment1, comment2));

        postRepository.save(post);

        // GET 요청으로 게시글 상세 조회
        mockMvc.perform(get("/api/posts/{id}", post.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("테스트 제목"))
                .andExpect(jsonPath("$.body").value("테스트 내용"))
                .andExpect(jsonPath("$.writer.name").value(users.get(3).getName()))
                .andExpect(jsonPath("$.writer.email").value(users.get(3).getEmail()))
                .andExpect(jsonPath("$.comments.length()").value(2)) // 댓글 개수 확인
                .andExpect(jsonPath("$.comments[0].body").value("테스트 댓글 1"))
                .andExpect(jsonPath("$.comments[1].writer.name").value(comment2.getWriter().getName()))
                .andDo(print());
    }
}