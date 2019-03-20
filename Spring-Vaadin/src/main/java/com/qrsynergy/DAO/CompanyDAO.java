package com.qrsynergy.DAO;

import com.qrsynergy.model.Company;
import com.qrsynergy.repository.CompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CompanyDAO {

    @Autowired
    CompanyRepository companyRepository;

    /**
     * Saves company to the database
     * Name is converted to the upper case before storing
     * @param company
     */
    public void saveCompany(Company company){
        company.setName(company.getName().toUpperCase());
        companyRepository.save(company);
    }

    /**
     * Finds company in the database
     * name is converted to the upper case
     * @param name
     * @returns company
     */
    public Company findByCompanyName(String name){
        return companyRepository.findByName(name.toUpperCase());
    }
}
