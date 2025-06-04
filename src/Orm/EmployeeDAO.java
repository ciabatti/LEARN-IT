package Orm;


import DomainModel.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class EmployeeDAO {
    public Company getCompany(String idcode) throws SQLException, ClassNotFoundException {
        Connection con = ConnectionManager.getConnection();
        String sql = "SELECT companyid FROM employees WHERE idcode = ?";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setString(1, idcode);
        ResultSet rs = ps.executeQuery();

        CompanyDAO companydao = new CompanyDAO();
        if (rs.next()) {
            Company company = companydao.getCompany(rs.getString("companyid"));
            return company;
        }
    return null;
    }

    public ArrayList<Employee> getAllEmployees() throws SQLException, ClassNotFoundException {
        Connection con = ConnectionManager.getConnection();
        String sql = "SELECT idcode, name, surname, age, role, meetings, idstrategy, feepaid FROM employees";
        PreparedStatement ps = con.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();

        ArrayList<Employee> employees = new ArrayList<Employee>();
        while (rs.next()) {
            String idcode = rs.getString("idcode");
            String name = rs.getString("name");
            String surname = rs.getString("surname");
            int age = rs.getInt("age");
            String role = rs.getString("role");
            int meetings = rs.getInt("meetings");
            boolean feepaid = rs.getBoolean("feepaid");
            FeeStrategy feestrategy;
            if(rs.getInt("idstrategy") == 1){
                feestrategy = new MultipleEmployeesFee();
            }
            else{
                feestrategy = new SingleEmployeeFee();
            } 
            Subscription subscription = new Subscription(meetings, null, feestrategy, feepaid);
            Employee employee = new Employee(idcode, name, surname, age, role);
            employee.setSubscription(subscription);
            employees.add(employee);
        }
        return employees;
    }

    public Employee getEmployee(String idcode) throws SQLException, ClassNotFoundException {
        Connection con = ConnectionManager.getConnection();
        String sql = "SELECT idcode, name, surname, age, role, meetings, idstrategy, feepaid  FROM employees WHERE idcode = ?";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setString(1, idcode);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            String name = rs.getString("name");
            String surname = rs.getString("surname");
            int age = rs.getInt("age");
            String role = rs.getString("role");
            int meetings = rs.getInt("meetings");
            boolean feepaid = rs.getBoolean("feepaid");
            FeeStrategy feestrategy;
            if(rs.getInt("idstrategy") == 1){
                feestrategy = new MultipleEmployeesFee();
            }
            else{
                feestrategy = new SingleEmployeeFee();
            }
            Subscription subscription = new Subscription(meetings, null, feestrategy, feepaid);
            Employee employee = new Employee(idcode, name, surname, age, role);
            employee.setSubscription(subscription);
            return employee;
        }
        return null;
    }

    public ArrayList<Employee> getEmployeesByCompany(String companyID) throws SQLException, ClassNotFoundException {
        Connection con = ConnectionManager.getConnection();
        String sql = "SELECT idcode, name, surname, age, role FROM employees WHERE companyid = ?";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setString(1, companyID);
        ResultSet rs = ps.executeQuery();

        ArrayList<Employee> employees = new ArrayList<Employee>();
        SubscriptionDAO subscriptiondao = new SubscriptionDAO();
        while (rs.next()) {
            String idcode = rs.getString("idcode");
            String name = rs.getString("name");
            String surname = rs.getString("surname");
            int age = rs.getInt("age");
            String role = rs.getString("role");
            Employee employee = new Employee(idcode, name, surname, age, role);
            Subscription subscription = subscriptiondao.getEmployeeInfo(idcode);
            employee.setSubscription(subscription);
            employees.add(employee);
        }
        return employees;
    }

    public void insertEmployee(Employee employee, Subscription subscription, String companyid) throws SQLException, ClassNotFoundException {
        Connection con = ConnectionManager.getConnection();
        String sql = "INSERT INTO employees (idcode, name, surname, age, role, companyid, meetings, idstrategy, feepaid) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setString(1, employee.getIdcode());
        ps.setString(2, employee.getName());
        ps.setString(3, employee.getSurname());
        ps.setInt(4, employee.getAge());
        ps.setString(5, employee.getRole());
        ps.setString(6, companyid);
        ps.setInt(7, subscription.getMeetings());
        if(subscription.getFeeStrategy() instanceof MultipleEmployeesFee) {
            ps.setInt(8, 1);
        }
        else if(subscription.getFeeStrategy() instanceof SingleEmployeeFee) {
            ps.setInt(8, 2);
        }
        ps.setBoolean(9, false);
        ps.executeUpdate();
        ps.close();
    }

    public void delete(Employee employee) throws SQLException, ClassNotFoundException {
        Connection con = ConnectionManager.getConnection();
        String sql = "DELETE FROM employees WHERE idcode = ?";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setString(1, employee.getIdcode());
        ps.executeUpdate();
        ps.close();
    }
}

