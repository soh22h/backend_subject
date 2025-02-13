package org.board.repository;

import org.board.dto.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface PostRepository extends JpaRepository<Post, Long>{
    @Query("SELECT p, SIZE(p.comments) as commentCount FROM Post p")
    List<Object[]> findAllWithCommentCount();
}
