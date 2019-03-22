package com.qrsynergy.repository;

import com.qrsynergy.model.QR;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QRRepository extends MongoRepository<QR, String> {

    public QR findByUrl(String url);
}
