package spring.security.jwt.model.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import spring.security.jwt.model.enums.RegistrationStatus;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Size(max = 64)
    @Column(name = "username")
    private String username;

    @Size(max = 64)
    @Column(name = "password")
    private String password;

    @Size(max = 128)
    @Column(name = "email")
    private String email;

    @Size(max = 16)
    @Column(name = "phone_number", nullable = false, unique = true)
    private String phoneNumber;

    @Column(name = "created", nullable = false)
    private LocalDateTime created;

    @Column(name = "updated", nullable = false)
    private LocalDateTime lastUpdate;

    @Column(name = "last_login")
    private LocalDateTime lastLogin;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "deleted", nullable = false)
    private Boolean deleted = false;

    @Enumerated(EnumType.STRING)
    @Column(name = "registration_status", nullable = false)
    private RegistrationStatus registrationStatus;

    @OneToOne(mappedBy = "user")
    private RefreshToken refreshToken;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Post> posts = new LinkedList<>(); // Связь с постами

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments = new LinkedList<>();

    @ManyToMany
    @JoinTable(
            name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Collection<Role> roles;
}