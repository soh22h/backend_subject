package org.board.dto;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@Builder
@Table(name="Comment")
@NoArgsConstructor
@AllArgsConstructor
public class Comment extends BaseEntity {

    @Column(nullable = false, columnDefinition = "TEXT")
    private String body;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "post", nullable = false)
    private Post post;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "writer", nullable = false)
    private User writer;
}
