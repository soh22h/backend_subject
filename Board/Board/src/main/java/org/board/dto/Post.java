package org.board.dto;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Entity
@Builder
@Table(name="Post")
@NoArgsConstructor
@AllArgsConstructor
public class Post extends BaseEntity {
    @Column(nullable = false)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String body;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "writer", nullable = false)
    private User writer;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments;
}
