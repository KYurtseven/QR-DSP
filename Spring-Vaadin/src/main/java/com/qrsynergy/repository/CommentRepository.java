package com.qrsynergy.repository;

import com.qrsynergy.model.Comment;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends MongoRepository<Comment, String> {

    public Comment findByUrl(String url);
}
