package spring.security.jwt.model.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Size;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "end_points")
public class EndPoint {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Size(max = 8)
    @Column(name = "method", nullable = false)
    private String method;

    @Size(max = 256)
    @Column(name = "path", nullable = false)
    private String path;

}
