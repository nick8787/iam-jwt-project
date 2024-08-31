package spring.security.jwt.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import spring.security.jwt.model.entities.User;

import java.util.LinkedList;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    boolean existsByUsernameAndIdNot(String username, Integer id);

    boolean existsByEmailAndIdNot(String email, Integer id);

    boolean existsByPhoneNumberAndIdNot(String phoneNumber, Integer id);

    Optional<User> findUserByUsernameAndDeletedFalse(String username);

    @Query("SELECT u FROM User u WHERE u.deleted = false")
    LinkedList<User> findAllActiveUsers(Integer userId);

    Optional<User> findByIdAndDeletedFalse(Integer userId);

    Optional<User> findByIdAndCreatedBy(Integer userId, String createdBy);
}

