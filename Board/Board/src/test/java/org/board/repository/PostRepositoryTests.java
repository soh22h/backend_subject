package org.board.repository;

import lombok.extern.log4j.Log4j2;
import org.board.dto.Comment;
import org.board.dto.Post;
import org.board.dto.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

@Log4j2
@SpringBootTest
@Commit
public class PostRepositoryTests {

    @Autowired
    PostRepository postRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    private CommentRepository commentRepository;

    @BeforeEach
    public void insert(){
        List<User> users = userRepository.findAll();
        List<Post> posts = new ArrayList<>();

        IntStream.rangeClosed(1, 5).forEach(i -> {
            User user = users.get(i-1);
            Post post = Post.builder()
                    .title("제목 " + i)
                    .body("내용 " + i)
                    .writer(user)
                    .build();
            postRepository.save(post);
            posts.add(post);
        });
    }

    @Test
    public void testInsert() {
        List<User> users = userRepository.findAll();

        User user = users.get(3);
        Post post = Post.builder()
                .title("제목")
                .body("내용")
                .writer(user)
                .build();
        postRepository.save(post);

        List<Post> savedPosts = postRepository.findAll();
        Post savedPost = savedPosts.get(savedPosts.size() - 1);

        // Verify
        log.info("저장된 게시글: Title={}, Body={}, UserName={}, UserEmail={}", post.getTitle(), post.getBody(), post.getWriter().getName(), post.getWriter().getEmail());
        Assertions.assertEquals(post.getTitle(), savedPost.getTitle());
        Assertions.assertEquals(post.getBody(), savedPost.getBody());
        Assertions.assertEquals(post.getWriter().getName(), savedPost.getWriter().getName());
        Assertions.assertEquals(post.getWriter().getEmail(), savedPost.getWriter().getEmail());
    }

    @Test
    public void testUpdate() {
        List<Post> posts = postRepository.findAll();

        Post postToUpdate = posts.get(2);
        String originalTitle = postToUpdate.getTitle();
        String originalBody = postToUpdate.getBody();

        postToUpdate.setTitle("수정 제목");
        postToUpdate.setBody("수정 내용");
        postRepository.save(postToUpdate);

        // Verify
        Optional<Post> updatedPostOptional = postRepository.findById(postToUpdate.getId());
        Assertions.assertTrue(updatedPostOptional.isPresent());
        Post updatedPost = updatedPostOptional.get();

        Assertions.assertEquals("수정 제목", updatedPost.getTitle());
        Assertions.assertEquals("수정 내용", updatedPost.getBody());
        Assertions.assertEquals(postToUpdate.getWriter().getId(), updatedPost.getWriter().getId());

        // Log 확인
        log.info("수정 전 게시글: Title={}, Body={}", originalTitle, originalBody);
        log.info("수정 후 게시글: Title={}, Body={}", updatedPost.getTitle(), updatedPost.getBody());
    }

    @Test
    public void testDelete() {
        List<Post> posts = postRepository.findAll();

        Post post = posts.get(3);
        postRepository.deleteById(post.getId());

        // Verify
        Optional<Post> deletedPost = postRepository.findById(post.getId());
        Assertions.assertEquals(deletedPost.isPresent(), false);
        log.info("삭제된 게시글: Index={}", 3);
    }

    @Test
    public void testFindAll() {
        List<Post> posts = postRepository.findAll();

        // Verify
        posts.forEach(post -> {
            log.info("게시글: Title={}, Body={}, UserName={}, UserEmail={}", post.getTitle(), post.getBody(), post.getWriter().getName(), post.getWriter().getEmail());
            Assertions.assertNotNull(post, "Post's user should not be null.");
        });
    }

    @Test
    public void testFindAllWithCommentCount() {
        List<Object[]> postsWithCommentCount = postRepository.findAllWithCommentCount();

        postsWithCommentCount.forEach(result -> {
            Post post = (Post) result[0];
            Integer commentCount = (Integer) result[1];

            log.info("게시글: Title={}, Body={}, UserName={}, UserEmail={}, 댓글 개수={}",
                    post.getTitle(),
                    post.getBody(),
                    post.getWriter().getName(),
                    post.getWriter().getEmail(),
                    commentCount);

            // Verify
            Assertions.assertNotNull(post);
            Assertions.assertNotNull(commentCount);
        });
    }

    @Test
    public void testFindById() {
        Optional<Post> foundPost = postRepository.findById(5L);
        Comment comment = Comment.builder()
                .body("테스트 댓글")
                .post(foundPost.get())
                .writer(foundPost.get().getWriter())
                .build();
        commentRepository.save(comment);

        // Verify
        Assertions.assertTrue(foundPost.isPresent());
        Assertions.assertEquals(foundPost.get().getTitle(), "제목 5");
        Assertions.assertEquals(foundPost.get().getBody(), "내용 5");
    }
}
