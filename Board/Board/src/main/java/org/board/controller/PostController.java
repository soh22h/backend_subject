package org.board.controller;

import org.board.dto.PostDTO;
import org.board.dto.PostResponseDTO;
import org.board.service.PostService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @PostMapping
    public ResponseEntity<PostDTO> createPost(@RequestBody PostDTO postDTO) {
        PostDTO createdPost = postService.createPost(postDTO);
        return ResponseEntity.ok(createdPost);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PostDTO> updatePost(@PathVariable Long id, @RequestBody PostDTO postDTO) {
        PostDTO updatedPost = postService.updatePost(id, postDTO);
        return ResponseEntity.ok(updatedPost);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable Long id) {
        postService.deletePost(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<PostResponseDTO>> listPosts() {
        List<PostResponseDTO> posts = postService.getPostList();
        return ResponseEntity.ok(posts);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostResponseDTO> getPost(@PathVariable Long id) {
        PostResponseDTO postResponseDTO = postService.getPostDetail(id);
        return ResponseEntity.ok(postResponseDTO);
    }
}