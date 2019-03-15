package com.qrsynergy.repository;

import com.qrsynergy.model.User;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface QRRepository extends MongoRepository<User, ObjectId> {

}
