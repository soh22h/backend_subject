package org.board.dto;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import jakarta.persistence.*;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Entity
@Builder
@Table(name="User")
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @org.hibernate.annotations.Comment("UUID로 generate")
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(columnDefinition = "CHAR(36)", nullable = false, unique = true)
    private String id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = true)
    private String email;

    @OneToMany(mappedBy = "writer", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Post> posts;

    @OneToMany(mappedBy = "writer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments;
}
