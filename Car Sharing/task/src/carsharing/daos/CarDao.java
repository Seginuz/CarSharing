package carsharing.daos;

import carsharing.models.Car;
import carsharing.models.Company;

import java.util.List;

public interface CarDao {
    List<Car> getCompanyCars(Company company);
    void createCar(Company company, String name);
    Car getCar(int id);
}
