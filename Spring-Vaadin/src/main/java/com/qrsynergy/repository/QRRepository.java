package com.qrsynergy.repository;

import com.qrsynergy.model.QR;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QRRepository extends MongoRepository<QR, String> {

    public QR findByUrl(String url);

    public List<QR> findByUrlIn(List<String> url);
}
