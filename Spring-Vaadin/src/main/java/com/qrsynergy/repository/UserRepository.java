package com.qrsynergy.repository;

import org.bson.types.ObjectId;
import com.qrsynergy.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import javax.validation.constraints.Email;

@Repository
public interface UserRepository extends MongoRepository<User, String> {

    /**
     *
     * @param email email of the user
     * @return User
     */
    public User findByEmail(String email);
}
