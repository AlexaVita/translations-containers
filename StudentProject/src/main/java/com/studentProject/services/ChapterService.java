package com.studentProject.services;

import com.studentProject.entities.Chapter;
import com.studentProject.repositories.ChapterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class ChapterService {
    @Autowired
    ChapterRepository chapterRepository;
    @Autowired
    KafkaProducerService kafkaProducerService;

    public Page<Chapter> findAll(Pageable pageable) {
        Page<Chapter> chapters = chapterRepository.findAll(pageable);
        return chapters;
    }

    public List<Integer> getPages(Page<Chapter> chapters) {
        List<Integer> pages = IntStream.rangeClosed(1, chapters.getTotalPages()).
                boxed().collect(Collectors.toList());
        return pages;
    }

    public void saveChapter(Chapter chapter) {
        kafkaProducerService.sendChapter(chapter);
        chapterRepository.save(chapter);
    }

    public Chapter findById(Long id) {
        Optional<Chapter> chapterOp = chapterRepository.findById(id);
        Chapter chapter = chapterOp.get();
        return chapter;
    }

    public void deleteById(Long id) {
        chapterRepository.deleteById(id);
    }
}
