package com.qrsynergy.repository;

import org.bson.types.ObjectId;
import com.qrsynergy.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import javax.validation.constraints.Email;

public interface UserRepository extends MongoRepository<User, Email> {

    public User findByEmail(String email);
}
