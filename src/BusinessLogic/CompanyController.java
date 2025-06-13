package BusinessLogic;

import DomainModel.*;
import Orm.*;

import javax.mail.MessagingException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

public class CompanyController {
    private final Notifier notifier;

    public CompanyController() {
        notifier = Notifier.getInstance();
    }

    public void registerEmployee(Employee employee, int meetings, String companyid) throws SQLException, ClassNotFoundException {
        EmployeeDAO employeeDAO = new EmployeeDAO();
        ArrayList<Employee> empoyees = employeeDAO.getEmployeesByCompany(companyid);
        FeeStrategy feeStrategy;
        if(empoyees.isEmpty())
            feeStrategy = new SingleEmployeeFee();
        else if (empoyees.size() >= 1) {
            feeStrategy = new MultipleEmployeesFee();
            SubscriptionDAO subscriptionDAO = new SubscriptionDAO();
            subscriptionDAO.editFeeStrategy(empoyees.get(0).getIdcode(), feeStrategy);
        }
        else
            feeStrategy = new MultipleEmployeesFee();
        Subscription subscription = new Subscription(meetings, employee, feeStrategy, false);
        employee.setSubscription(subscription);
        employeeDAO.insertEmployee(employee, subscription, companyid);
    }

    public void payFee(String idcodeEmployee, String idcodeCompany) throws SQLException, MessagingException, ClassNotFoundException {
        SubscriptionDAO subscriptionDAO = new SubscriptionDAO();
        subscriptionDAO.editFeePaid(idcodeEmployee, true);
        Subscription subscription = subscriptionDAO.getEmployeeInfo(idcodeEmployee);

        CompanyDAO companyDAO = new CompanyDAO();
        ArrayList<Company> companies = new ArrayList<>();
        companies.add(companyDAO.getCompany(idcodeCompany));
        notifier.sendEmailCompany(companies, "Fee paid", "the fee of "+ subscription.getFee() + " euros for your employee "+ subscription.getEmployee().getName()+ " " +
                subscription.getEmployee().getSurname() + " has been paid successfully.");
    }

    public ArrayList<Employee> viewEmployeesInfo(String companyid) throws SQLException, ClassNotFoundException {
        EmployeeDAO employeeDAO = new EmployeeDAO();
        return employeeDAO.getEmployeesByCompany(companyid);
    }

    public ArrayList<Material> viewSlideVideos() throws SQLException, ClassNotFoundException, IOException {
        MaterialDAO materialDAO = new MaterialDAO();
        ArrayList<Material> material = new ArrayList<>();
        material = materialDAO.getAllMaterial();
        for(Material m : material){
            File file = new File("slide/out/" + m.getFilename());
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(m.getFile());
        }
        return material;
    }

    public ArrayList<Course> viewCourses() throws SQLException, ClassNotFoundException {
        CourseDAO courseDAO = new CourseDAO();
        return courseDAO.getAllCourses();
    }


}
