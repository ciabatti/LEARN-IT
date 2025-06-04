package BusinessLogic;

import DomainModel.Employee;
import DomainModel.*;
import Orm.*;

import javax.mail.MessagingException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;

public class TrainerController {
    private final Notifier notifier;

    public TrainerController() {
        notifier = Notifier.getInstance();
    }

    public void uploadSlideVideos(String filename, Trainer trainer, boolean isSlidePack) throws SQLException, IOException, ParseException, MessagingException, ClassNotFoundException {
        MaterialDAO materialDAO = new MaterialDAO();
        materialDAO.uploadMaterial(filename, trainer.getEmail(), isSlidePack);

        CompanyDAO companyDAO = new CompanyDAO();
        ArrayList<Company> companies = new ArrayList<>();
        companies = companyDAO.getAllCompanies();
        notifier.sendEmailCompany(companies, "New material", "new material has been uploaded. You can check it on the website.");
    }

    public ArrayList<Material> viewSlideVideo() throws SQLException, ClassNotFoundException, IOException {
        MaterialDAO materialDAO = new MaterialDAO();
        ArrayList<Material> material = new ArrayList<>();
        material = materialDAO.getAllMaterial();
        for(Material m : material){
            File file = new File("imgs/out/" + m.getFilename());
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(m.getFile());
        }
        return material;
    }

    public void deleteSlideVideo(Material material) throws SQLException, ClassNotFoundException {
        MaterialDAO materialDAO = new MaterialDAO();
        materialDAO.deleteMaterial(material.getFilename());
    }

    public ArrayList<Employee> viewEmployeeList() throws SQLException, ClassNotFoundException {
        EmployeeDAO employeeDAO = new EmployeeDAO();
        return employeeDAO.getAllEmployees();
    }

    public ArrayList<Workshift> viewWorkshifts(Trainer trainer) throws SQLException, ClassNotFoundException {
        WorkshiftDAO workshiftDAO = new WorkshiftDAO();
        return workshiftDAO.getIndividualWorkshift(trainer);
    }

    public ArrayList<Course> viewCourses() throws SQLException, ClassNotFoundException {
        CourseDAO courseDAO = new CourseDAO();
        return courseDAO.getAllCourses();
    }

    public Employee viewEmployeeInfo(String idcode) throws SQLException, ClassNotFoundException {
        EmployeeDAO employeeDAO = new EmployeeDAO();
        SubscriptionDAO subscriptionDAO = new SubscriptionDAO();
        Employee employee = employeeDAO.getEmployee(idcode);
        employee.setSubscription(subscriptionDAO.getEmployeeInfo(idcode));
        return employee;
    }


}
