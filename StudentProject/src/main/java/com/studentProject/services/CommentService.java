package com.studentProject.services;

import com.studentProject.entities.Chapter;
import com.studentProject.entities.Comment;
import com.studentProject.entities.User;
import com.studentProject.repositories.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class CommentService {
    @Autowired
    CommentRepository commentRepository;
    @Autowired
    KafkaProducerService kafkaProducerService;

    public Page<Comment> findByChapter(Pageable pageable, Chapter chapter) {
        Page<Comment> comments = commentRepository.findByChapter(pageable, chapter);
        return comments;
    }

    public List<Integer> getPages(Page<Comment> comments) {
        List<Integer> pages = IntStream.rangeClosed(1, comments.getTotalPages()).
                boxed().collect(Collectors.toList());
        return pages;
    }

    public void set(Comment comment, User user, Chapter chapter) {
        comment.setChapter(chapter);
        comment.setUser(user);
        kafkaProducerService.sendComment(comment);
        commentRepository.save(comment);
    }

    public void deleteById(Long commentId) {
        Comment comment = commentRepository.findById(commentId).orElseThrow();
        commentRepository.delete(comment);
    }

}
