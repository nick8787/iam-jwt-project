package spring.security.jwt.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import spring.security.jwt.model.entities.Post;

import java.util.LinkedList;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Integer> {

    Optional<Post> findByIdAndDeletedFalse (Integer id);

    boolean existsByTitle(String title);

    LinkedList<Post> findAllByUserIdAndDeletedFalse(Integer userId);

    LinkedList<Post> findAllByDeletedFalse();

}
