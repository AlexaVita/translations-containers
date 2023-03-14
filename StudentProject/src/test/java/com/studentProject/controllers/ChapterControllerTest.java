package com.studentProject.controllers;

import com.studentProject.entities.Chapter;
import com.studentProject.entities.Comment;
import com.studentProject.entities.Role;
import com.studentProject.entities.User;
import com.studentProject.services.ChapterService;
import com.studentProject.services.CommentService;
import com.studentProject.services.UserService;
import org.apache.catalina.security.SecurityConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@Import(SecurityConfig.class)
@WebMvcTest(ChapterController.class)
class ChapterControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    ChapterService chapterService;
    @MockBean
    UserService userService;
    @MockBean
    CommentService commentService;


    @Test
    public void shouldAllowAccessForAnonymousUserChapters() throws Exception {
        List<Chapter> listOfChapters = new ArrayList<>();
        Pageable pageable = PageRequest.of(0, 15);
        Page<Chapter> chapters = new PageImpl<>(listOfChapters, pageable, 1);

        when(chapterService.findAll(any(Pageable.class))).thenReturn(chapters);

        this.mockMvc.perform(get("/chapters"))
                .andExpect(status().isOk())
                .andExpect(view().name("chapters"))
                .andExpect(model().attributeExists("chapters"))
                .andExpect(model().attributeExists("pages"));
    }

    @Test
    public void shouldRedirectAnonymousUserToLoginMakeChapter() throws Exception {
        this.mockMvc.perform(get("/makeChapter"))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    @WithMockCustomUser(role = Role.ADMIN)
    public void shouldAccessForAuthenticatedUserWithAdminRoleMakeChapter() throws Exception {
        this.mockMvc.perform(get("/makeChapter"))
                .andExpect(status().isOk())
                .andExpect(view().name("makeChapter"));
    }

    @Test
    @WithMockCustomUser(role = Role.USER)
    public void shouldDenyForAuthenticatedUserWithUserRoleMakeChapter() throws Exception {
        this.mockMvc.perform(get("/makeChapter"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockCustomUser(role = Role.ADMIN)
    public void shouldAccessForAuthenticatedUserWithAdminRoleMakeChapterPost() throws Exception {

        Chapter chapter = new Chapter("test", "test");

        this.mockMvc.perform(post("/makeChapter")
                        .flashAttr("chapter", chapter)
                        .with(csrf()))
                .andExpect(status().is3xxRedirection());
        verify(chapterService).saveChapter(chapter);
    }

    @Test
    @WithMockCustomUser(role = Role.USER)
    public void shouldDenyForAuthenticatedUserWithUserRoleMakeChapterPost() throws Exception {

        Chapter chapter = new Chapter("test", "test");

        this.mockMvc.perform(post("/makeChapter")
                        .flashAttr("chapter", chapter)
                        .with(csrf()))
                .andExpect(status().isForbidden());
    }

    @Test
    public void shouldRedirectAnonymousUserToLoginMakeChapterPost() throws Exception {
        Chapter chapter = new Chapter("test", "test");

        this.mockMvc.perform(post("/makeChapter")
                        .flashAttr("chapter", chapter)
                        .with(csrf()))
                .andExpect(status().is3xxRedirection());
        verify(chapterService, never()).saveChapter(chapter);
    }

    @Test
    @WithMockCustomUser(role = Role.ADMIN)
    public void shouldAccessForAuthenticatedUserWithAdminRoleAddComment() throws Exception {

        List<Comment> listOfComments = new ArrayList<>();
        Pageable pageable = PageRequest.of(0, 115);
        Page<Comment> comments = new PageImpl<>(listOfComments, pageable, 8);
        List<Integer> pages = IntStream.rangeClosed(1, comments.getTotalPages()).
                boxed().collect(Collectors.toList());
        Comment comment = new Comment("test");
        Chapter chapter = new Chapter("test", "test");
        Long id = 1L;

        when(chapterService.findById(id)).thenReturn(chapter);
        when(commentService.findByChapter(any(Pageable.class), any(Chapter.class))).thenReturn(comments);
        when(commentService.getPages(any(Page.class))).thenReturn(pages);

        this.mockMvc.perform(post("/chapter/{chapterId}/addComment", id)
                        .flashAttr("comment", comment)
                        .with(csrf()))
                .andExpect(status().is3xxRedirection());
        verify(commentService).set(any(Comment.class), any(User.class), any(Chapter.class));

    }

    @Test
    @WithMockCustomUser(role = Role.USER)
    public void shouldAccessForAuthenticatedUserWithUserRoleAddComment() throws Exception {

        List<Comment> listOfComments = new ArrayList<>();
        Pageable pageable = PageRequest.of(0, 115);
        Page<Comment> comments = new PageImpl<>(listOfComments, pageable, 8);
        List<Integer> pages = IntStream.rangeClosed(1, comments.getTotalPages()).
                boxed().collect(Collectors.toList());
        Comment comment = new Comment("test");
        Chapter chapter = new Chapter("test", "test");
        Long id = 1L;

        when(chapterService.findById(id)).thenReturn(chapter);
        when(commentService.findByChapter(any(Pageable.class), any(Chapter.class))).thenReturn(comments);
        when(commentService.getPages(any(Page.class))).thenReturn(pages);

        this.mockMvc.perform(post("/chapter/{chapterId}/addComment", id)
                        .flashAttr("comment", comment)
                        .with(csrf()))
                .andExpect(status().is3xxRedirection());
        verify(commentService).set(any(Comment.class), any(User.class), any(Chapter.class));

    }

    @Test
    public void shouldDenyForAnonymousUserAddComment() throws Exception {

        List<Comment> listOfComments = new ArrayList<>();
        Pageable pageable = PageRequest.of(0, 115);
        Page<Comment> comments = new PageImpl<>(listOfComments, pageable, 8);
        List<Integer> pages = IntStream.rangeClosed(1, comments.getTotalPages()).
                boxed().collect(Collectors.toList());
        Comment comment = new Comment("test");
        Chapter chapter = new Chapter("test", "test");
        Long id = 1L;

        when(chapterService.findById(id)).thenReturn(chapter);
        when(commentService.findByChapter(any(Pageable.class), any(Chapter.class))).thenReturn(comments);
        when(commentService.getPages(any(Page.class))).thenReturn(pages);

        this.mockMvc.perform(post("/chapter/{chapterId}/addComment", id)
                        .flashAttr("comment", comment)
                        .with(csrf()))
                .andExpect(status().is3xxRedirection());
        verify(commentService, never()).set(any(Comment.class), any(User.class), any(Chapter.class));
    }


    @Test
    @WithMockCustomUser(role = Role.ADMIN)
    public void shouldAccessForAuthenticatedUserWithAdminRolePostEdit() throws Exception {

        Chapter chapter = new Chapter("test", "test");
        Long id = 1L;

        this.mockMvc.perform(post("/chapter/edit/{id}", id)
                        .flashAttr("chapter", chapter)
                        .with(csrf()))
                .andExpect(status().is3xxRedirection());
        verify(chapterService).saveChapter(chapter);
    }

    @Test
    @WithMockCustomUser(role = Role.USER)
    public void shouldDenyForAuthenticatedUserWithUserRolePostEdit() throws Exception {

        Chapter chapter = new Chapter("test", "test");
        Long id = 1L;

        this.mockMvc.perform(post("/chapter/edit/{id}", id)
                        .flashAttr("chapter", chapter)
                        .with(csrf()))
                .andExpect(status().isForbidden());
        verify(chapterService, never()).saveChapter(any(Chapter.class));
    }

    @Test
    public void shouldRedirectAnonymousUserPostEdit() throws Exception {

        Chapter chapter = new Chapter("test", "test");
        Long id = 1L;


        this.mockMvc.perform(post("/chapter/edit/{id}", id)
                        .flashAttr("chapter", chapter)
                        .with(csrf()))
                .andExpect(status().is3xxRedirection());
        verify(chapterService, never()).saveChapter(any(Chapter.class));
    }

    @Test
    public void shouldRedirectAnonymousUserPostDelChapter() throws Exception {

        Long id = 1L;

        this.mockMvc.perform(post("/chapter/delete/{id}", id)
                        .with(csrf()))
                .andExpect(status().is3xxRedirection());
        verify(chapterService, never()).deleteById(id);
    }

    @Test
    @WithMockCustomUser(role = Role.USER)
    public void shouldDenyForAuthenticatedUserWithUserRolePostDelChapter() throws Exception {

        Long id = 1L;

        this.mockMvc.perform(post("/chapter/delete/{id}", id)
                        .with(csrf()))
                .andExpect(status().isForbidden());
        verify(chapterService, never()).deleteById(id);
    }

    @Test
    @WithMockCustomUser(role = Role.ADMIN)
    public void shouldAccessForAuthenticatedUserWithAdminRolePostDelChapter() throws Exception {

        Long id = 1L;

        this.mockMvc.perform(post("/chapter/delete/{id}", id)
                        .with(csrf()))
                .andExpect(status().is3xxRedirection());
        verify(chapterService).deleteById(id);
    }

    @Test
    public void shouldRedirectAnonymousUserPostDelComment() throws Exception {

        Long id = 1L;

        this.mockMvc.perform(post("/chapter/{id}/deleteComment", id)
                        .param("commentId", id.toString())
                        .with(csrf()))
                .andExpect(status().is3xxRedirection());
        verify(commentService, never()).deleteById(id);
    }

    @Test
    @WithMockCustomUser(role = Role.USER)
    public void shouldDenyForAuthenticatedUserWithUserRolePostDelComment() throws Exception {

        Long id = 1L;

        this.mockMvc.perform(post("/chapter/{id}/deleteComment", id)
                        .param("commentId", id.toString())
                        .with(csrf()))
                .andExpect(status().isForbidden());
        verify(commentService, never()).deleteById(id);
    }

    @Test
    @WithMockCustomUser(role = Role.ADMIN)
    public void shouldAccessForAuthenticatedUserWithAdminRolePostDelComment() throws Exception {

        Long id = 1L;

        this.mockMvc.perform(post("/chapter/{id}/deleteComment", id)
                        .param("commentId", id.toString())
                        .with(csrf()))
                .andExpect(status().is3xxRedirection());
        verify(commentService).deleteById(id);
    }

    @Test
    public void shouldRedirectAnonymousUserToLoginEditChapter() throws Exception {

        Long id = 1L;

        this.mockMvc.perform(get("/chapter/edit/{id}", id))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    @WithMockCustomUser(role = Role.ADMIN)
    public void shouldAccessForAuthenticatedUserWithAdminRoleEditChapter() throws Exception {

        Long id = 1L;
        Chapter chapter = new Chapter("test", "test");
        chapter.setId(id);

        when(chapterService.findById(id)).thenReturn(chapter);

        this.mockMvc.perform(get("/chapter/edit/{id}", id))
                .andExpect(status().isOk())
                .andExpect(view().name("editChapter"));
    }

    @Test
    @WithMockCustomUser(role = Role.USER)
    public void shouldDenyForAuthenticatedUserWithUSERRoleEditChapter() throws Exception {

        Long id = 1L;
        Chapter chapter = new Chapter("test", "test");
        chapter.setId(id);

        when(chapterService.findById(id)).thenReturn(chapter);

        this.mockMvc.perform(get("/chapter/edit/{id}", id))
                .andExpect(status().isForbidden());
    }

    @Test
    public void shouldAllowAccessForAnonymousUserGetChapter() throws Exception {

        List<Comment> listOfComments = new ArrayList<>();
        Pageable pageable = PageRequest.of(0, 115);
        Page<Comment> comments = new PageImpl<>(listOfComments, pageable, 8);
        List<Integer> pages = IntStream.rangeClosed(1, comments.getTotalPages()).
                boxed().collect(Collectors.toList());
        Comment comment = new Comment("test");
        Chapter chapter = new Chapter("test", "test");
        Long id = 1L;

        when(chapterService.findById(id)).thenReturn(chapter);
        when(commentService.findByChapter(any(Pageable.class), any(Chapter.class))).thenReturn(comments);
        when(commentService.getPages(any(Page.class))).thenReturn(pages);


        this.mockMvc.perform(get("/chapter/{id}", id))
                .andExpect(status().isOk())
                .andExpect(view().name("getChapter"));
    }

}