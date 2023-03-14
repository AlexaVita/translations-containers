package com.backupService.service;

import com.backupService.model.Chapter;
import com.backupService.model.Comment;
import com.backupService.model.User;
import com.backupService.repository.ChapterRepository;
import com.backupService.repository.CommentRepository;
import com.backupService.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.converter.StringJsonMessageConverter;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumerService
{
    @Autowired
    ChapterRepository chapterRepository;

    @Autowired
    CommentRepository commentRepository;

    @Autowired
    UserRepository userRepository;

    private final Logger logger =
            LoggerFactory.getLogger(KafkaConsumerService.class);

    @KafkaListener(topics = "chapters",
            groupId = "group-id")
    public void consumeChapters(Chapter chapter)
    {
        logger.info(String.format("Chapter consumed: %s", chapter.getTitle()));
        chapterRepository.save(chapter);
    }

    @KafkaListener(topics = "comments",
            groupId = "group-id")
    public void consumeComments(Comment comment)
    {
        logger.info(String.format("Comment consumed."));
        commentRepository.save(comment);
    }

    @KafkaListener(topics = "users",
            groupId = "group-id")
    public void consumeUsers(User user)
    {
        logger.info(String.format("User consumed: %s", user.getUsername()));
        userRepository.save(user);
    }

    @Bean
    public StringJsonMessageConverter jsonConverter() {
        return new StringJsonMessageConverter();
    }
}