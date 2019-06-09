package com.qrsynergy.repository;

import com.qrsynergy.model.QR;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QRRepository extends MongoRepository<QR, String> {

    /**
     *
     * @param url uuid of the document
     * @return QR
     */
    public QR findByUrl(String url);

    /**
     * Finds list of qr with url
     * @param url list of url
     * @return list of QR
     */
    public List<QR> findByUrlIn(List<String> url);

    /**
     * Removes qr
     * @param url uuid of document
     * @return
     */
    public Long removeByUrl(String url);
}
