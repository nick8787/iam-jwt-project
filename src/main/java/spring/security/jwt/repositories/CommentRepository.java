package spring.security.jwt.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import spring.security.jwt.model.entities.Comment;

import java.util.LinkedList;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Integer> {

    Optional<Comment> findByIdAndDeletedFalse(Integer commentId);

    LinkedList<Comment> findAllByDeletedFalse();

    LinkedList<Comment> findAllByUserIdAndDeletedFalse(Integer userId);

}

