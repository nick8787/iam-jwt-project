package spring.security.jwt.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import spring.security.jwt.model.entities.User;

import java.util.LinkedList;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByUsername(String username);

    boolean existsByEmail(String email);

    Optional<User> findUserByUsername(String username);

    @Query("SELECT u FROM User u WHERE u.id = :userId")
    LinkedList<User> findAllUsers(@Param("userId") Integer userId);

}
