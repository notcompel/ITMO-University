package ru.itmo.wp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import ru.itmo.wp.domain.Comment;
import ru.itmo.wp.domain.Post;
import ru.itmo.wp.security.Guest;
import ru.itmo.wp.service.CommentService;
import ru.itmo.wp.service.PostService;
import ru.itmo.wp.service.UserService;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Controller
public class PostPage extends Page {
    private final PostService postService;

    public PostPage(PostService postService, CommentService commentService, UserService userService) {
        this.postService = postService;
    }

    @PostMapping({"/post/{id}", "/post"})
    public String post(@PathVariable(required = false) String id,
                       @Valid @ModelAttribute("comment") Comment comment,
                       BindingResult bindingResult,
                       HttpSession httpSession, Model model) {
        if (bindingResult.hasErrors()) {
            return "PostPage";
        }
        if (getUser(httpSession) == null) {
            return "redirect:/enter";
        }
        postService.writeComment(getUser(httpSession), postService.findById(Long.parseLong(id)), comment);
        model.addAttribute("post", postService.findById(Long.parseLong(id)));
        putMessage(httpSession, "You published new comment");

        return "redirect:/post/{id}";
    }

    @Guest
    @GetMapping({"/post/{id}", "/post"})
    public String post(@PathVariable(required = false) String id,
                       Model model) {
        try {
            model.addAttribute("post", postService.findById(Long.parseLong(id)));
            model.addAttribute("comment", new Comment());
        } catch (NumberFormatException e) {
            //no operations
        }
        return "PostPage";
    }


}
