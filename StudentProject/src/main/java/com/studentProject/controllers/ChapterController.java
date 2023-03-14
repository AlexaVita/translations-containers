package com.studentProject.controllers;

import com.studentProject.entities.Chapter;
import com.studentProject.entities.Comment;
import com.studentProject.entities.User;
import com.studentProject.services.ChapterService;
import com.studentProject.services.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

//Отвечает за реализацию отображения и взаимодействия с главами, комментариями.
@Controller
public class ChapterController {

    @Autowired
    ChapterService chapterService;
    @Autowired
    CommentService commentService;

    @GetMapping("/chapters")
    public String getChapters(Model model,
                              @AuthenticationPrincipal User user,
                              @PageableDefault(sort = {"id"}, value = 15, direction = Sort.Direction.ASC) Pageable pageable
    ) {

        Page<Chapter> chapters = chapterService.findAll(pageable);
        model.addAttribute("chapters", chapters);

        List<Integer> pages = chapterService.getPages(chapters);
        model.addAttribute("pages", pages);
        if (user != null) {
            model.addAttribute("role", user.getRole().getAuthority());
        }

        return "chapters";
    }


    @GetMapping("/makeChapter")
    public String makeChapter(Model model,
                              @AuthenticationPrincipal User user
    ) {

        if (user != null) {
            model.addAttribute("role", user.getRole().getAuthority());
        }
        model.addAttribute("chapter", new Chapter());

        return "makeChapter";
    }

    @PostMapping("/makeChapter")
    public String makeChapter(@ModelAttribute("chapter") @Valid Chapter chapter,
                              BindingResult bindingResult,
                              @AuthenticationPrincipal User user,
                              Model model) {

        if (user != null) {
            model.addAttribute("role", user.getRole().getAuthority());
        }

        if (bindingResult.hasErrors()) {
            return "makeChapter";
        }

        chapterService.saveChapter(chapter);

        return "redirect:/chapters";
    }

    @GetMapping("/chapter/{id}")
    public String getChapter(Model model,
                             @PathVariable Long id,
                             @AuthenticationPrincipal User user,
                             @PageableDefault(sort = {"id"}, direction = Sort.Direction.DESC) Pageable pageable
    ) {

        Chapter chapter = chapterService.findById(id);
        model.addAttribute("chapter", chapter);
        model.addAttribute("comment", new Comment());

        Page<Comment> comments = commentService.findByChapter(pageable, chapter);

        model.addAttribute("comments", comments);

        List<Integer> pages = commentService.getPages(comments);
        model.addAttribute("pages", pages);

        if (user != null) {
            model.addAttribute("role", user.getRole().getAuthority());
        }

        return "getChapter";
    }

    @GetMapping("/chapter/edit/{id}")
    public String editChapter(@PathVariable Long id,
                              Model model,
                              @AuthenticationPrincipal User user
    ) {

        Chapter chapter = chapterService.findById(id);
        model.addAttribute("chapter", chapter);

        if (user != null) {
            model.addAttribute("role", user.getRole().getAuthority());
        }

        return "editChapter";
    }

    @PostMapping("/chapter/edit/{id}")
    public String editChapter(@PathVariable Long id,
                              @ModelAttribute("chapter") @Valid Chapter chapter,
                              BindingResult bindingResult,
                              @AuthenticationPrincipal User user,
                              Model model
    ) {

        if (user != null) {
            model.addAttribute("role", user.getRole().getAuthority());
        }

        if (bindingResult.hasErrors()) {
            return "editChapter";
        }

        chapterService.saveChapter(chapter);

        return "redirect:/chapters";
    }

    @PostMapping("/chapter/delete/{id}")
    public String deleteChapter(@PathVariable Long id, @AuthenticationPrincipal User user) {

        chapterService.deleteById(id);

        return "redirect:/chapters";
    }

    /*вместо id в пути указано chapterId, так как иначе происходит подстановка значения id пути
     в id комментария, поскольку comment возвращается из формы без id*/
    @PostMapping("/chapter/{chapterId}/addComment")
    public String addComment(Model model,
                             @ModelAttribute("comment") @Valid Comment comment,
                             BindingResult bindingResult,
                             @AuthenticationPrincipal User user,
                             @PageableDefault(sort = {"id"}, direction = Sort.Direction.DESC) Pageable pageable,
                             @PathVariable("chapterId") Long id
    ) {

        if (user != null) {
            model.addAttribute("role", user.getRole().getAuthority());
        }

        Chapter chapter = chapterService.findById(id);
        model.addAttribute("chapter", chapter);

        Page<Comment> comments = commentService.findByChapter(pageable, chapter);
        model.addAttribute("comments", comments);

        List<Integer> pages = commentService.getPages(comments);
        model.addAttribute("pages", pages);

        if (bindingResult.hasErrors()) {
            return "getChapter";
        }

        commentService.set(comment, user, chapter);

        return "redirect:/chapter/{chapterId}";
    }

    @PostMapping("/chapter/{id}/deleteComment")
    public String deleteComment(Model model,
                                @RequestParam Long commentId
    ) {

        commentService.deleteById(commentId);

        return "redirect:/chapter/{id}";
    }
}
