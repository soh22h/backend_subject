package org.board.controller;

import org.board.dto.CommentDTO;
import org.board.service.CommentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comments")
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping("/post/{postId}")
    public ResponseEntity<CommentDTO> createComment(@PathVariable Long postId, @RequestBody CommentDTO commentDTO) {
        CommentDTO createdComment = commentService.createComment(postId, commentDTO);
        return ResponseEntity.ok(createdComment);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CommentDTO> updateComment(@PathVariable Long id, @RequestBody CommentDTO commentDTO) {
        CommentDTO updatedComment = commentService.updateComment(id, commentDTO);
        return ResponseEntity.ok(updatedComment);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long id) {
        commentService.deleteComment(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/post/{postId}")
    public ResponseEntity<List<CommentDTO>> listCommentsByPost(@PathVariable Long postId) {
        List<CommentDTO> comments = commentService.getCommentList(postId);
        return ResponseEntity.ok(comments);
    }
}