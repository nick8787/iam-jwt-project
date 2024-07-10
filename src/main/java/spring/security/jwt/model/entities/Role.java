package spring.security.jwt.model.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import spring.security.jwt.service.model.IamServiceUserRole;
import spring.security.jwt.utils.enum_converter.UserRoleTypeConverter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@Table(name = "roles")
@AllArgsConstructor
@NoArgsConstructor
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name = "user_system_role", nullable = false, updatable = false)
    @Convert(converter = UserRoleTypeConverter.class)
    private IamServiceUserRole userSystemRole;

    @Column(name = "active")
    private boolean active;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "roles", cascade = CascadeType.MERGE)
    private Set<User> users = new HashSet<>();

}