package ru.itmo.wp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.itmo.wp.domain.Comment;
import ru.itmo.wp.domain.Post;

import java.util.List;

public interface CommentRepository extends JpaRepository<Post, Long> {
    List<Comment> findAllByOrderByCreationTimeDesc();
}
