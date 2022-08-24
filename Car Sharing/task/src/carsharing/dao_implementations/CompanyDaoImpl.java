package carsharing.dao_implementations;

import carsharing.daos.CompanyDao;
import carsharing.models.Company;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CompanyDaoImpl implements CompanyDao {

    private final Connection conn;

    public CompanyDaoImpl(Connection conn) {
        this.conn = conn;
    }

    @Override
    public List<Company> getAllCompanies() {
        List<Company> companies = new ArrayList<>();

        try {
            Statement stmt = conn.createStatement();
            String query = "SELECT * FROM COMPANY;";
            ResultSet result = stmt.executeQuery(query);

            while (result.next()) {
                int id = result.getInt("ID");
                String name = result.getString("NAME");
                companies.add(new Company(id, name));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return companies;
    }

    @Override
    public Company getCompany(int id) {
        Company company = null;

        try {
            String query = "SELECT * FROM COMPANY WHERE ID = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, id);
            ResultSet result = stmt.executeQuery();

            if (result.next()) {
                String name = result.getString("NAME");
                company = new Company(id, name);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return company;
    }

    @Override
    public void createCompany(String name) {
        try {
            String query = "INSERT INTO COMPANY (NAME) VALUES (?)";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, name);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
