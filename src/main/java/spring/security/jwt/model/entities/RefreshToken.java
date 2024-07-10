package spring.security.jwt.model.entities;

import com.sun.istack.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Entity
@Table(name = "refresh_tokens")
@Getter
@Setter
@ToString
public class RefreshToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Size(max = 128)
    @Column(name = "token", nullable = false)
    private String token;
    @Column(name = "created", nullable = false)
    private LocalDateTime created;

    @NotNull
    @JoinColumn(name = "user_id")
    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private User user;

}
