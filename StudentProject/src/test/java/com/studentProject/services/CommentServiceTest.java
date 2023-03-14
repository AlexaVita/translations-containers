package com.studentProject.services;

import com.studentProject.entities.Chapter;
import com.studentProject.entities.Comment;
import com.studentProject.entities.Role;
import com.studentProject.entities.User;
import com.studentProject.repositories.CommentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class CommentServiceTest {

    private static final Long TEST_ID = 1L;

    @Mock
    KafkaProducerService kafkaProducerService;
    @Mock
    CommentRepository commentRepository;
    @InjectMocks
    private CommentService commentService;

    private Comment comment;
    private Chapter chapter;
    private User user;

    @BeforeEach
    public void setup() {

        chapter = new Chapter("testName", "testTitle");
        chapter.setId(TEST_ID);
        user = new User("test", "test", true, Role.USER);
        user.setId(TEST_ID);
        comment = new Comment("test", chapter, user);
        comment.setId(TEST_ID);
    }

    @Test
    void shouldReturnCommentsByChapter() {

        List<Comment> listOfComments = new ArrayList<>();
        Pageable pageable = PageRequest.of(0, 115);
        Page<Comment> comments = new PageImpl<>(listOfComments, pageable, 8);

        Mockito.when(commentRepository.findByChapter(any(Pageable.class), any(Chapter.class))).thenReturn(comments);

        Page<Comment> result = commentService.findByChapter(pageable, chapter);
        assertEquals(comments.getTotalElements(), result.getTotalElements());

    }

    @Test
    public void shouldSaveComment() {

        commentService.set(comment, user, chapter);

        verify(commentRepository).save(any(Comment.class));
    }

    @Test
    public void shouldDeleteComment() {

        Mockito.when(commentRepository.findById(TEST_ID)).thenReturn(Optional.of(comment));

        commentService.deleteById(TEST_ID);
        verify(commentRepository).delete(any(Comment.class));
    }

    @Test
    public void shouldReturnTrueTotalPages() {

        int total = 115;
        int size = 15;
        int expected = Math.ceilDiv(total, size);
        List<Comment> listOfComments = new ArrayList<>();
        Pageable pageable = PageRequest.of(0, size);
        Page<Comment> comments = new PageImpl<>(listOfComments, pageable, total);

        assertEquals(expected, commentService.getPages(comments).size());
    }

}