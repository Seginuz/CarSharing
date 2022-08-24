package carsharing.daos;

import carsharing.models.Company;

import java.util.List;

public interface CompanyDao {
    List<Company> getAllCompanies();
    Company getCompany(int id);
    void createCompany(String name);
}
