package spring.security.jwt.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import spring.security.jwt.model.entities.Role;

import java.util.LinkedList;
import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {

    @Query("SELECT DISTINCT r "
            + "FROM Role r "
            + "JOIN r.users u "
            + "WHERE r.active = true and u.id = ?1 ")
    LinkedList<Role> findActiveRolesByUserIdAndActiveIsTrue(Integer userId);

    Optional<Role> findByName(String name);

}
