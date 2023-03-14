package com.studentProject.services;

import com.studentProject.entities.Chapter;
import com.studentProject.repositories.ChapterRepository;
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
class ChapterServiceTest {
    private static final Long TEST_ID = 1L;

    @Mock
    KafkaProducerService kafkaProducerService;
    @Mock
    private ChapterRepository chapterRepository;
    @InjectMocks
    private ChapterService chapterService;

    @Test
    public void shouldReturnChapterById() {

        Chapter chapter = new Chapter("testName", "testTitle");
        chapter.setId(TEST_ID);

        Mockito.when(chapterRepository.findById(TEST_ID)).thenReturn(Optional.of(chapter));

        assertEquals(chapter, chapterService.findById(TEST_ID));
    }

    @Test
    public void shouldReturnAllChapters() {

        List<Chapter> listOfChapters = new ArrayList<>();
        Pageable pageable = PageRequest.of(0, 15);
        Page<Chapter> chapters = new PageImpl<>(listOfChapters, pageable, 1);

        Mockito.when(chapterRepository.findAll(Mockito.any(Pageable.class))).thenReturn(chapters);

        Page<Chapter> result = chapterService.findAll(pageable);
        assertEquals(chapters.getTotalElements(), result.getTotalElements());
    }

    @Test
    public void shouldSaveChapter() {
        Chapter chapter = new Chapter("testName", "testTitle");
        chapter.setId(TEST_ID);

        chapterService.saveChapter(chapter);

        verify(chapterRepository).save(any(Chapter.class));
    }

    @Test
    public void shouldDeleteChapter() {

        chapterService.deleteById(TEST_ID);

        verify(chapterRepository).deleteById(TEST_ID);
    }

    @Test
    public void shouldReturnTrueTotalPages() {

        int total = 115;
        int size = 15;
        int expected=Math.ceilDiv(total, size);
        List<Chapter> listOfChapters = new ArrayList<>();
        Pageable pageable = PageRequest.of(0, size);
        Page<Chapter> chapters = new PageImpl<>(listOfChapters, pageable, total);

        assertEquals(expected, chapterService.getPages(chapters).size());

    }

}