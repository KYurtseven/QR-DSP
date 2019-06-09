package com.qrsynergy.repository;

import com.qrsynergy.model.UserQR;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserQRRepository extends MongoRepository<UserQR, String> {

    /**
     * Finds user's UserQR document by user's email
     * @param o_info user's email
     * @return user's UserQR document
     */
    @Query("{'o_info': ?0}")
    public UserQR findByO_info(String o_info);
}
