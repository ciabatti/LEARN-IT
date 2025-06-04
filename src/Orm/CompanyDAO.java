package Orm;

import DomainModel.Company;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class CompanyDAO {

    public Company getCompany(String idcode) throws SQLException, ClassNotFoundException {
        Connection con = ConnectionManager.getConnection();
        String sql = "SELECT * FROM companies WHERE idcode = ?";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setString(1, idcode);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            String email = rs.getString("email");
            String name = rs.getString("name");
            String cellphone = rs.getString("cellphone");
            String address = rs.getString("address");
            Company company = new Company(idcode, email, name, address, cellphone);
            return company;
        }
        return null;
    }

    public ArrayList<Company> getAllCompanies() throws SQLException, ClassNotFoundException {
        Connection con = ConnectionManager.getConnection();
        String sql = "SELECT * FROM companies";
        PreparedStatement ps = con.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        ArrayList<Company> companies = new ArrayList<Company>();
        while (rs.next()) {
            String idcode = rs.getString("idcode");
            String email = rs.getString("email");
            String name = rs.getString("name");
            String address = rs.getString("address");
            String cellphone = rs.getString("cellphone");
            Company company = new Company(idcode, email, name, address, cellphone);
            companies.add(company);
        }
        return companies;
    }

    public void addCompany(Company company) throws SQLException, ClassNotFoundException {
        Connection con = ConnectionManager.getConnection();
        String sql = "INSERT INTO companies (idcode, email, name, address, cellphone) VALUES (?, ?, ?, ?, ?)";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setString(1, company.getIdcode());
        ps.setString(2, company.getEmail());
        ps.setString(3, company.getName());
        ps.setString(4, company.getAddress());
        ps.setString(5, company.getCellphone());
        ps.executeUpdate();
        ps.close();
    }

    public void deleteCompany(String idcode) throws SQLException, ClassNotFoundException {
        Connection con = ConnectionManager.getConnection();
        String sql = "DELETE FROM companies WHERE idcode = ?";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setString(1, idcode);
        ps.executeUpdate();
    }

}
