package com.qrsynergy.service;

import com.qrsynergy.model.Comment;
import com.qrsynergy.model.helper.CommentEntry;
import com.qrsynergy.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommentService {

    @Autowired
    CommentRepository commentRepository;

    /**
     * @param url uuid of the document
     * @return Comment document of the document
     */
    public Comment findComment(String url){
        return commentRepository.findByUrl(url);
    }

    /**
     * Saves new comment to database
     * @param comment comment entity of the QR
     * @param commentEntry new comment entry to be saved
     */
    public void addCommentEntry(Comment comment, CommentEntry commentEntry){
        comment.appendToCommentEntry(commentEntry);
        commentRepository.save(comment);
    }

    /**
     * Saves comment entity to the database
     * @param comment comment entity
     */
    public void saveComment(Comment comment){
        commentRepository.save(comment);
    }
}
