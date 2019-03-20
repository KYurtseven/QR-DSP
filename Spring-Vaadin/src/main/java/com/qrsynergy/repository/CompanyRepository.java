package com.qrsynergy.repository;

import com.qrsynergy.model.Company;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CompanyRepository extends MongoRepository<Company, String> {

    public Company findByName(String name);
}
