package carsharing.dao_implementations;

import carsharing.daos.CarDao;
import carsharing.models.Car;
import carsharing.models.Company;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CarDaoImpl implements CarDao {
    private final Connection conn;

    public CarDaoImpl(Connection conn) {
        this.conn = conn;
    }

    @Override
    public List<Car> getCompanyCars(Company company) {
        List<Car> cars = new ArrayList<>();

        try {
            String query = "SELECT * " +
                    "FROM CAR " +
                    "LEFT JOIN CUSTOMER ON CAR.ID = CUSTOMER.RENTED_CAR_ID " +
                    "WHERE CUSTOMER.RENTED_CAR_ID IS NULL " +
                    "AND COMPANY_ID = ? " +
                    "ORDER BY CAR.ID";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, company.getId());
            ResultSet result = stmt.executeQuery();

            while (result.next()) {
                int id = result.getInt("ID");
                String name = result.getString("NAME");
                int company_id = result.getInt("COMPANY_ID");
                cars.add(new Car(id, name, company_id));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return cars;
    }

    @Override
    public Car getCar(int id) {
        Car car = null;

        try {
            String query = "SELECT * FROM CAR WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, id);
            ResultSet result = stmt.executeQuery();

            if (result.next()) {
                String name = result.getString("NAME");
                int companyId = result.getInt("COMPANY_ID");
                car = new Car(id, name, companyId);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return car;
    }

    @Override
    public void createCar(Company company, String name) {
        try {
            String query = "INSERT INTO CAR (NAME, COMPANY_ID) VALUES (?, ?)";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, name);
            stmt.setInt(2, company.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
