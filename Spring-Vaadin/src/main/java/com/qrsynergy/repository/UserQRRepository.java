package com.qrsynergy.repository;

import com.qrsynergy.model.UserQR;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserQRRepository extends MongoRepository<UserQR, String> {

    @Query("{'o_info': ?0}")
    public UserQR findByO_info(String o_info);
}
