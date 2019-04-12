package com.qrsynergy.service;

import com.qrsynergy.model.Comment;
import com.qrsynergy.model.CommentEntry;
import com.qrsynergy.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommentService {

    @Autowired
    CommentRepository commentRepository;

    public Comment findComment(String url){
        return commentRepository.findByUrl(url);
    }

    public void addCommentEntry(Comment comment, CommentEntry commentEntry){
        comment.appendToCommentEntry(commentEntry);
        commentRepository.save(comment);
    }
}
