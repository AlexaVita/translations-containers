package com.studentProject.services;

import com.studentProject.entities.Chapter;
import com.studentProject.entities.Comment;
import com.studentProject.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaProducerService {
    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;


    public void sendChapter(Chapter chapter) {

        this.kafkaTemplate.send("chapters", chapter);
    }

    public void sendComment(Comment comment) {
        this.kafkaTemplate.send("comments", comment);
    }

    public void sendUser(User user) {
        this.kafkaTemplate.send("users", user);
    }
}