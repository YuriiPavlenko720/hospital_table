package lemon.hospitaltable.table.repositories;

import lemon.hospitaltable.table.objects.Department;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class DepartmentRepository implements DepartmentsRepositoryInterface {
    private static final String URL = "jdbc:mysql://localhost:3306/hospital_01";
    private static final String LOGIN = "root";
    private static final String PASSWORD = "0000";

    private static Connection connection;

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        try {
            connection = DriverManager.getConnection(URL, LOGIN, PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void save(Department department) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement
                    ("INSERT INTO departments(name, capacity, taken, free) VALUES (?,?,?,?)");
            preparedStatement.setString(1, department.getName());
            preparedStatement.setInt(2, department.getCapacity());
            preparedStatement.setInt(3, department.getTaken());
            preparedStatement.setInt(4, department.getFree());

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteById(int id) {

    }

    @Override
    public Department findById(int id) {

        Department department = new Department();

        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM departments WHERE id = ?");
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            resultSet.next();
            department.setId(resultSet.getInt("id"));
            department.setName(resultSet.getString("name"));
            department.setCapacity(resultSet.getInt("capacity"));
            department.setTaken(resultSet.getInt("taken"));
            department.setFree(resultSet.getInt("free"));

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return department;
    }

    @Override
    public List<Department> findAll() {
        List<Department> allDepartments = new ArrayList<>();
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM departments");

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                int capacity = resultSet.getInt("capacity");
                int taken = resultSet.getInt("taken");
                int free = resultSet.getInt("free");

                Department department = new Department(id, name, capacity, taken, free);

                allDepartments.add(department);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return allDepartments;
    }

    @Override
    public void setDepartmentName(int id, String name) {

    }
}
