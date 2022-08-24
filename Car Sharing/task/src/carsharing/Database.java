package carsharing;

import carsharing.dao_implementations.CarDaoImpl;
import carsharing.dao_implementations.CompanyDaoImpl;
import carsharing.dao_implementations.CustomerDaoImpl;
import carsharing.daos.CarDao;
import carsharing.daos.CompanyDao;
import carsharing.daos.CustomerDao;
import carsharing.models.Car;
import carsharing.models.Company;
import carsharing.models.Customer;

import java.sql.Connection;
import java.util.List;

public class Database {

    CustomerDao customerDao;
    CompanyDao companyDao;
    CarDao carDao;

    Database(Connection conn) {
        this.customerDao = new CustomerDaoImpl(conn);
        this.companyDao = new CompanyDaoImpl(conn);
        this.carDao = new CarDaoImpl(conn);
    }

    public List<Customer> getAllCustomers() {
        return customerDao.getAllCustomers();
    }

    public void createCustomer(String name) {
        customerDao.createCustomer(name);
    }

    public void updateCustomer(Customer customer) {
        customerDao.updateCustomer(customer);
    }

    public List<Company> getAllCompanies() {
        return companyDao.getAllCompanies();
    }

    public Company getCompany(int id) {
        return companyDao.getCompany(id);
    }

    public void createCompany(String name) {
        companyDao.createCompany(name);
    }

    public List<Car> getCompanyCars(Company company) {
        return carDao.getCompanyCars(company);
    }

    public void createCar(Company company, String name) {
        carDao.createCar(company, name);
    }

    public Car getCar(int id) {
        return carDao.getCar(id);
    }
}
