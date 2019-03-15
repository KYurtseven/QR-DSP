package com.qrsynergy.repository;

import com.qrsynergy.model.UserQR;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserQRRepository extends MongoRepository<UserQR, String> {
}
