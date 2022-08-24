package carsharing.daos;

import carsharing.models.Customer;

import java.util.List;

public interface CustomerDao {
    List<Customer> getAllCustomers();
    void createCustomer(String name);
    void updateCustomer(Customer customer);
}
