package com.qrsynergy.repository;

import com.qrsynergy.model.Company;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompanyRepository extends MongoRepository<Company, String> {

    public Company findByName(String name);
}
