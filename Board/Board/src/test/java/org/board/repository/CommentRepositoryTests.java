package org.board.repository;

import jakarta.transaction.Transactional;
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
@Transactional
@Commit
public class CommentRepositoryTests {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    public void insert() {
        List<User> users = userRepository.findAll();
        List<Comment> comments = new ArrayList<>();

        Post post = Post.builder()
                .title("제목")
                .body("내용")
                .writer(users.get(0))
                .build();
        postRepository.save(post);

        IntStream.rangeClosed(1, 5).forEach(i -> {
            Comment comment = Comment.builder()
                    .body("댓글 " + i)
                    .post(post)
                    .writer(post.getWriter())
                    .build();
            commentRepository.save(comment);
            comments.add(comment);
        });
    }

    @Test
    public void testInsert(){
        List<User> users = userRepository.findAll();

        Post post = Post.builder()
                .title("테스트 제목")
                .body("테스트 내용")
                .writer(users.get(1))
                .build();
        postRepository.save(post);

        Comment comment = Comment.builder()
                .body("테스트 댓글")
                .post(post)
                .writer(post.getWriter())
                .build();
        commentRepository.save(comment);

        List<Comment> savedComments = commentRepository.findByPostId(post.getId());

        // Verify
        log.info("저장된 댓글: Post={}, Writer={}, Body={}, CreatedAt={}", comment.getPost().getTitle(), comment.getWriter().getName(), comment.getBody(), comment.getCreatedAt());
        Assertions.assertEquals(comment, savedComments.get(0));
    }

    @Test
    public void testFindAllByPost() {
        List<Comment> comments = commentRepository.findByPostId(1L);

        // Verify
        Assertions.assertEquals(comments.size(), 5);
        Assertions.assertEquals(comments.get(0).getBody(), "댓글 1");
        Assertions.assertEquals(comments.get(1).getBody(), "댓글 2");
        Assertions.assertEquals(comments.get(2).getBody(), "댓글 3");
        Assertions.assertEquals(comments.get(3).getBody(), "댓글 4");
        Assertions.assertEquals(comments.get(4).getBody(), "댓글 5");

        comments.forEach(comment -> {
            log.info("Comment={}, Body={}, Writer={}", comment, comment.getBody(), comment.getWriter());
        });
    }

    @Test
    public void testUpdate() {
        List<Comment> comments = commentRepository.findAll();
        Assertions.assertFalse(comments.isEmpty());

        Comment commentToUpdate = comments.get(0);
        String originalBody = commentToUpdate.getBody();

        commentToUpdate.setBody("수정한 댓글");
        Comment updatedComment = commentRepository.save(commentToUpdate);

        // Verify
        Optional<Comment> foundComment = commentRepository.findById(updatedComment.getId());
        Assertions.assertTrue(foundComment.isPresent());
        log.info("수정 전 댓글: Post={}, Body={}", commentToUpdate.getPost().getTitle(), originalBody);
        log.info("수정 후 댓글: Post={}, Body={}", foundComment.get().getPost().getTitle(), foundComment.get().getBody());
        Assertions.assertEquals("수정한 댓글", foundComment.get().getBody());
        Assertions.assertNotEquals(originalBody, foundComment.get().getBody());
    }

    @Test
    public void testDelete() {
        List<Comment> comments = commentRepository.findAll();
        Assertions.assertFalse(comments.isEmpty());

        Comment commentToDelete = comments.get(0);
        Long commentId = commentToDelete.getId();

        commentRepository.deleteById(commentId);

        // Verify
        log.info("삭제된 댓글: Body={}, Post={}", commentToDelete.getBody(), commentToDelete.getPost().getTitle());
        Optional<Comment> deletedComment = commentRepository.findById(commentId);
        Assertions.assertTrue(deletedComment.isEmpty());
    }
}
