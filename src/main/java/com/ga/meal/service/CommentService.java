package com.ga.meal.service;

import com.ga.meal.entity.Comment;
import com.ga.meal.entity.Recipe;
import com.ga.meal.entity.User;
import com.ga.meal.repository.CommentRepository;
import com.ga.meal.repository.RecipeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final RecipeRepository recipeRepository;
    private final UserService userService;

    public Comment addComment(Long recipeId, Comment comment) {
        User currentUser = userService.getCurrentUser();

        Recipe recipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new RuntimeException("Recipe not found"));

        comment.setUser(currentUser);
        comment.setRecipe(recipe);

        return commentRepository.save(comment);
    }

    public List<Comment> getRecipeComments(Long recipeId) {
        return commentRepository.findByRecipeId(recipeId);
    }

    public Comment updateComment(Long commentId, Comment updatedComment) {
        User currentUser = userService.getCurrentUser();

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("Comment not found"));

        if (!comment.getUser().getId().equals(currentUser.getId())) {
            throw new RuntimeException("You can only update your own comment");
        }

        comment.setCommentText(updatedComment.getCommentText());

        return commentRepository.save(comment);
    }

    public void deleteComment(Long commentId) {
        User currentUser = userService.getCurrentUser();

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("Comment not found"));

        if (!comment.getUser().getId().equals(currentUser.getId())) {
            throw new RuntimeException("You can only delete your own comment");
        }

        commentRepository.delete(comment);
    }
}