package com.qrsynergy.repository;

import com.qrsynergy.model.Company;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompanyRepository extends MongoRepository<Company, String> {

    /**
     *
     * @param name company name, it is in capital letters
     * @return Company
     */
    public Company findByName(String name);
}
