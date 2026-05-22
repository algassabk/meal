package com.ga.meal.controller;

import com.ga.meal.entity.Comment;
import com.ga.meal.service.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/recipe/{recipeId}")
    public Comment addComment(
            @PathVariable Long recipeId,
            @Valid @RequestBody Comment comment
    ) {
        return commentService.addComment(recipeId, comment);
    }

    @GetMapping("/recipe/{recipeId}")
    public List<Comment> getRecipeComments(@PathVariable Long recipeId) {
        return commentService.getRecipeComments(recipeId);
    }

    @PutMapping("/{commentId}")
    public Comment updateComment(
            @PathVariable Long commentId,
            @Valid @RequestBody Comment comment
    ) {
        return commentService.updateComment(commentId, comment);
    }

    @DeleteMapping("/{commentId}")
    public String deleteComment(@PathVariable Long commentId) {
        commentService.deleteComment(commentId);
        return "Comment deleted successfully";
    }
}
