package spring.security.jwt.model.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

@Entity
@Table(name = "posts")
@Getter
@Setter
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, length = 5000)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @NotNull
    @Column(name = "created", nullable = false, updatable = false)
    private LocalDateTime created = LocalDateTime.now();

    @NotNull
    @Column(name = "updated", nullable = false)
    private LocalDateTime updated = LocalDateTime.now();

    @Column(name = "deleted", nullable = false)
    private Boolean deleted = false;

    @Column(nullable = false, columnDefinition = "integer default 0")
    private Integer likes = 0;

    @Column(length = 2048)
    private String image;

    @Column(name = "created_by")
    private String createdBy;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments = new LinkedList<>(); // Связь с комментариями

    @Column(name = "comments_count", nullable = false, columnDefinition = "integer default 0")
    private Integer commentsCount = 0;

    public void incrementCommentsCount() {
        this.commentsCount++;
    }

    public void decrementCommentsCount() {
        if (this.commentsCount > 0) {
            this.commentsCount--;
        }
    }
}