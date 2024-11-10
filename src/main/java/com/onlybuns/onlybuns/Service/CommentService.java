package com.onlybuns.onlybuns.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.onlybuns.onlybuns.Model.Comment;
import com.onlybuns.onlybuns.Repository.CommentRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class CommentService {
    @Autowired
    public CommentRepository commentRepository;

    public Comment createComment(Comment comment)
    {
        return commentRepository.save(comment);
    }
    public List<Comment> getAllCommentsByPost(Long postId)
    {
        List<Comment> allComments = commentRepository.findByPostId(postId);
        for(Comment comment : allComments)
        {
            if(comment.isDeleted())
            {
                comment.setComment("[Removed]");
            }
        }
        return allComments;
    }
    public List<Comment> getAllCommentsByUser(String username)
    {
        List<Comment> allComments = commentRepository.findByUsername(username);
        for(Comment comment : allComments)
        {
            if(comment.isDeleted())
            {
                comment.setComment("[Removed]");
            }
        }
        return allComments;
    }
    public void deleteComment(Long id)
    {
        Comment comment = commentRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Comment not found with Id " + id));
        comment.setDeleted(true);
        commentRepository.save(comment);
    }
}
