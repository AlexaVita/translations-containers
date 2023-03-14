package com.backupService.repository;

import com.backupService.model.Chapter;
import com.backupService.model.Comment;
import com.backupService.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

public interface CommentRepository extends CrudRepository<Comment, Long> {
    Page<Comment> findByUser(Pageable pageable, User user);

    Page<Comment> findByChapter(Pageable pageable, Chapter chapter);
}
