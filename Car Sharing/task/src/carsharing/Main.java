package carsharing;

import carsharing.models.Car;
import carsharing.models.Company;
import carsharing.models.Customer;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class Main {

    static final Scanner scanner = new Scanner(System.in);
    static Database db;

    public static void main(String[] args) {
        String filename = (args.length >= 2 && "-databaseFileName".equals(args[0])) ? args[1] : "database";
        String url = "jdbc:h2:./src/carsharing/db/" + filename;

        try (Connection conn = DriverManager.getConnection(url)) {
            conn.setAutoCommit(true);
            db = new Database(conn);
            createTables(conn);

            mainMenu();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void createTables(Connection conn) throws SQLException {
        Statement companyStmt = conn.createStatement();
        String query = "CREATE TABLE IF NOT EXISTS COMPANY (" +
                "ID INTEGER AUTO_INCREMENT PRIMARY KEY NOT NULL, " +
                "NAME VARCHAR UNIQUE NOT NULL " +
                ");";
        companyStmt.executeUpdate(query);
        companyStmt.close();

        Statement carStmt = conn.createStatement();
        query = "CREATE TABLE IF NOT EXISTS CAR (" +
                "ID INTEGER AUTO_INCREMENT PRIMARY KEY NOT NULL, " +
                "NAME VARCHAR UNIQUE NOT NULL, " +
                "COMPANY_ID INT NOT NULL, " +
                "CONSTRAINT fk_car_company FOREIGN KEY (COMPANY_ID) REFERENCES COMPANY(ID)" +
                ");";
        carStmt.executeUpdate(query);
        carStmt.close();

        Statement customerStmt = conn.createStatement();
        query = "CREATE TABLE IF NOT EXISTS CUSTOMER (" +
                "ID INTEGER AUTO_INCREMENT PRIMARY KEY NOT NULL, " +
                "NAME VARCHAR UNIQUE NOT NULL, " +
                "RENTED_CAR_ID INT, " +
                "CONSTRAINT fk_rented_car FOREIGN KEY (RENTED_CAR_ID) REFERENCES CAR(ID)" +
                ");";
        customerStmt.executeUpdate(query);
        customerStmt.close();
    }

    public static void mainMenu() {
        while (true) {
            System.out.println("1. Log in as a manager");
            System.out.println("2. Log in as a customer");
            System.out.println("3. Create a customer");
            System.out.println("0. Exit");

            switch (getCommand()) {
                case 1:
                    managerLogin();
                    break;
                case 2:
                    customerLogin();
                    break;
                case 3:
                    createCustomer();
                    break;
                case 0:
                    return;
            }
        }
    }

    public static void managerLogin() {
        while (true) {
            System.out.println("1. Company list");
            System.out.println("2. Create a company");
            System.out.println("0. Back");

            int command = getCommand();

            switch (command) {
                case 1:
                    listCompanies(null);
                    break;
                case 2:
                    createCompany();
                    break;
                case 0:
                    return;
            }
        }
    }

    public static void listCompanies(Customer customer) {
        var companies = db.getAllCompanies();

        if (!companies.isEmpty()) {
            System.out.println("Choose a company:");
            int i = 1;
            for (Company c : companies) {
                System.out.println(i + ". " + c.getName());
                i++;
            }
            System.out.println("0. Back");

            int command = getCommand();
            if (command != 0) {
                if (customer == null) {
                    companyMenu(companies.get(command - 1));
                } else {
                    listCars(companies.get(command - 1), customer);
                }
            }
        } else {
            System.out.println("The company list is empty!");
            System.out.println();
        }
    }

    public static void companyMenu(Company company) {
        while (true) {
            System.out.printf("'%s' company:%n", company.getName());
            System.out.println("1. Car list");
            System.out.println("2. Create a car");
            System.out.println("0. Back");

            switch (getCommand()) {
                case 1:
                    listCars(company, null);
                    break;
                case 2:
                    createCar(company);
                    break;
                case 0:
                    return;
            }
        }
    }

    public static void listCars(Company company, Customer customer) {
        var cars = db.getCompanyCars(company);

        if (!cars.isEmpty()) {
            System.out.println(
                    customer == null
                    ? "'" + company.getName() + "' cars:"
                    : "Choose a car:");

            int i = 1;
            for (Car c : cars) {
                System.out.println(i + ". " + c.getName());
                i++;
            }

            if (customer != null) {
                System.out.println("0. Back");

                int command = getCommand();
                if (command != 0) {
                    customer.setRentedCarId(cars.get(command - 1).getId());
                    db.updateCustomer(customer);
                    System.out.println("You rented '" + cars.get(command - 1).getName() + "'");
                }
            }
        } else {
            System.out.println(
                    customer == null
                    ? "The car list is empty!"
                    : "No available cars in the " + company.getName() + " company"
            );
        }
        System.out.println();
    }

    private static void customerLogin() {
        var customers = db.getAllCustomers();

        if (!customers.isEmpty()) {
            System.out.println("Customer list:");
            int i = 1;
            for (Customer c : customers) {
                System.out.println(i + ". " + c.getName());
                i++;
            }
            System.out.println("0. Back");

            int command = getCommand();
            if (command != 0) {
                customerMenu(customers.get(command - 1));
            }
        } else {
            System.out.println("The customer list is empty!");
            System.out.println();
        }
    }

    private static void customerMenu(Customer customer) {
        while(true) {
            System.out.println("1. Rent a car");
            System.out.println("2. Return a rented car");
            System.out.println("3. My rented car");
            System.out.println("0. Back");

            switch(getCommand()) {
                case 1:
                    if (customer.getRentedCarId() == null) {
                        listCompanies(customer);
                    } else {
                        System.out.println("You've already rented a car!");
                        System.out.println();
                    }
                    break;
                case 2:
                    if (customer.getRentedCarId() != null) {
                        customer.setRentedCarId(null);
                        db.updateCustomer(customer);
                        System.out.println("You've returned a rented car!");
                        System.out.println();
                    } else {
                        System.out.println("You didn't rent a car!");
                        System.out.println();
                    }
                    break;
                case 3:
                    if (customer.getRentedCarId() != null) {
                        Car car = db.getCar(customer.getRentedCarId());
                        Company company = db.getCompany(car.getCompanyId());
                        System.out.println("Your rented car:");
                        System.out.println(car.getName());
                        System.out.println("Company:");
                        System.out.println(company.getName());
                        System.out.println();
                    } else {
                        System.out.println("You didn't rent a car!");
                        System.out.println();
                    }
                    break;
                case 0:
                    return;
            }
        }
    }

    private static void createCustomer() {
        System.out.println("Enter the customer name:");
        String name = scanner.nextLine();
        db.createCustomer(name);
        System.out.println("The customer was created!");
        System.out.println();
    }

    public static void createCompany() {
        System.out.println("Enter the company name:");
        String name = scanner.nextLine();
        db.createCompany(name);
        System.out.println("The company was created!");
        System.out.println();
    }

    public static void createCar(Company company) {
        System.out.println("Enter the car name:");
        String name = scanner.nextLine();
        db.createCar(company, name);
        System.out.println("The car was added!");
        System.out.println();
    }

    public static int getCommand() {
        try {
            String input = scanner.nextLine();
            int command = Integer.parseInt(input);
            System.out.println();
            return command;
        } catch (NumberFormatException e) {
            System.out.println("Enter a number");
            System.out.println();
            return -1;
        }
    }
}