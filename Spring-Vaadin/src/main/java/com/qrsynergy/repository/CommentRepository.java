package com.qrsynergy.repository;

import com.qrsynergy.model.Comment;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends MongoRepository<Comment, String> {

    /**
     * @param url uuid of the document
     * @return Comment document of the document
     */
    public Comment findByUrl(String url);
}
