package Orm;


import DomainModel.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class SubscriptionDAO {
    public Subscription getEmployeeInfo(String idcode) throws SQLException, ClassNotFoundException {
        Connection con = ConnectionManager.getConnection();
        String sql = "SELECT meetings, idstrategy, feepaid  FROM employees WHERE idcode = ?";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setString(1, idcode);
        ResultSet rs = ps.executeQuery();

        EmployeeDAO employeedao = new EmployeeDAO();
        Employee employee = employeedao.getEmployee(idcode);
        if (rs.next()) {
            int meetings = rs.getInt("meetings");
            boolean feepaid = rs.getBoolean("feepaid");
            FeeStrategy feestrategy;
            if(rs.getInt("idstrategy") == 1){
                feestrategy = new MultipleEmployeesFee();
            }
            else{
                feestrategy = new SingleEmployeeFee();
            }
            Subscription subscription = new Subscription(meetings, employee, feestrategy, feepaid);
            return subscription;
        }
        return null;
    }

    public void editFeeStrategy(String idcode, FeeStrategy feeStrategy) throws SQLException, ClassNotFoundException {
        Connection con = ConnectionManager.getConnection();
        String sql = "UPDATE employees SET idstrategy = ? WHERE idcode = ?";
        PreparedStatement ps = con.prepareStatement(sql);
        if(feeStrategy instanceof MultipleEmployeesFee) {
            ps.setInt(1, 1);
        }
        else if(feeStrategy instanceof SingleEmployeeFee) {
            ps.setInt(1, 2);
        }
        ps.setString(2, idcode);
        ps.executeUpdate();
        ps.close();
    }

    public void editFeePaid(String idcode, boolean feepaid) throws SQLException, ClassNotFoundException {
        Connection con = ConnectionManager.getConnection();
        String sql = "UPDATE employees SET feepaid = ? WHERE idcode = ?";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setBoolean(1, feepaid);
        ps.setString(2, idcode);
        ps.executeUpdate();
        ps.close();
    }

    public ArrayList<Company> getCompanysNotPaid() throws SQLException, ClassNotFoundException {
        Connection con = ConnectionManager.getConnection();
        String sql = "SELECT DISTINCT companies.idcode, companies.email, companies.name, companies.address, companies.cellphone FROM companies, employees WHERE companies.idcode = employees.companyid AND employees.feepaid = false";
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
}

