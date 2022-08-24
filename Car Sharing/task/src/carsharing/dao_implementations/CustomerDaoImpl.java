package carsharing.dao_implementations;

import carsharing.daos.CustomerDao;
import carsharing.models.Customer;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CustomerDaoImpl implements CustomerDao {

    private Connection conn;

    public CustomerDaoImpl(Connection conn) {
        this.conn = conn;
    }

    @Override
    public List<Customer> getAllCustomers() {
        List<Customer> customers = new ArrayList<>();

        try {
            Statement stmt = conn.createStatement();
            String query = "SELECT * FROM CUSTOMER;";
            ResultSet result = stmt.executeQuery(query);

            while (result.next()) {
                int id = result.getInt("ID");
                String name = result.getString("NAME");
                Integer rented_car_id = result.getInt("RENTED_CAR_ID");
                if (result.wasNull()) {
                    rented_car_id = null;
                }
                customers.add(new Customer(id, name, rented_car_id));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return customers;
    }

    @Override
    public void createCustomer(String name) {
        try {
            String query = "INSERT INTO CUSTOMER (NAME) VALUES (?)";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, name);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateCustomer(Customer customer) {
        try {
            String query = "UPDATE CUSTOMER SET RENTED_CAR_ID = ? WHERE ID = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            if (customer.getRentedCarId() != null) {
                stmt.setInt(1, customer.getRentedCarId());
            } else {
                stmt.setNull(1, Types.INTEGER);
            }
            stmt.setInt(2, customer.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
