package controllers;

import BusinessLogic.Admin;
import BusinessLogic.TrainerController;
import BusinessLogic.CompanyController;
import DomainModel.*;
import Orm.*;
import org.junit.Test;

import javax.mail.MessagingException;
import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.*;

public class CompanyControllerTest {
    @Test
    public void registerEmployee() {
        CompanyController companyController = new CompanyController();
        String idcode = "LLLNNN";
        Employee employee = new Employee(idcode, "Luca", "Neri", 29, "test2");
        Employee employee2 = new Employee("LLLBBB", "Lucia", "Bianchi", 27, "test3");
        EmployeeDAO employeeDAO = new EmployeeDAO();
        try{
            companyController.registerEmployee(employee, 2, "CCCTTT");
            ArrayList<Employee> employees = employeeDAO.getEmployeesByCompany("CCCTTT");
            assertEquals(employees.get(employees.size()-1).getIdcode(), employee.getIdcode());
            assertEquals(employees.get(employees.size()-1).getName(), employee.getName());
            assertEquals(employees.get(employees.size()-1).getSurname(), employee.getSurname());
            assertEquals(employees.get(employees.size()-1).getAge(), employee.getAge());
            assertEquals(employees.get(employees.size()-1).getRole(), employee.getRole());
            assertEquals(employees.get(employees.size()-1).getSubscription().getMeetings(), employee.getSubscription().getMeetings());
            assertEquals(employees.get(employees.size()-1).getSubscription().getFee(), employee.getSubscription().getFee());
            companyController.registerEmployee(employee2, 2, "abc123");
            employees = employeeDAO.getEmployeesByCompany("abc123");

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                employeeDAO.delete(employee);
                employeeDAO.delete(employee2);
            } catch (SQLException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    @Test
    public void viewSlidesAndVideos() {
        TrainerController trainerController = new TrainerController();
        Trainer trainer = new Trainer("paolorossi@mail.com", "Paolo", "Rossi");
        TrainerDAO trainerDAO = new TrainerDAO();
        MaterialDAO materialDAO = new MaterialDAO();
        try {
            trainerDAO.addTrainer(trainer);
            materialDAO.uploadMaterial("resources/testfile.pdf", trainer.getEmail(), true);
            ArrayList<Material> material = new ArrayList<>();
            material = trainerController.viewSlideVideo();
            assertEquals(material.get(material.size() - 1).getUploader().getEmail(), trainer.getEmail());
        } catch (SQLException | ParseException | IOException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                materialDAO.deleteMaterial("testfile.pdf");
                trainerDAO.deleteTrainer(trainer.getEmail());
            } catch (SQLException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    @Test
    public void  viewCourses(){
        Course course = new Course("2025-06-01", "12:00:00", "test", FocusCourse.JAVA);
        CourseDAO courseDAO = new CourseDAO();
        Admin admin = new Admin();
        ArrayList<Course> courses;
        try{
            courseDAO.insertCourse(course);
            courses = admin.viewCourses();
            assertEquals( courses.get(courses.size()-1).getDate(), course.getDate());
            assertEquals( courses.get(courses.size()-1).getTime(), course.getTime());
            assertEquals( courses.get(courses.size()-1).getDescription(), course.getDescription());
            assertEquals( courses.get(courses.size()-1).getType(), course.getType());
        } catch (SQLException | ParseException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally{
            try {
                courseDAO.deleteCourse(course);
            } catch (SQLException | ParseException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    @Test
    public void viewEmployeeInfo() {
        CompanyController companyController = new CompanyController();
        Employee employee = new Employee("LLLNNN", "Luca", "Neri", 29, "test2");
        MultipleEmployeesFee feeStrategy = new MultipleEmployeesFee();
        Subscription subscription = new Subscription(2, employee,feeStrategy, false);
        employee.setSubscription(subscription);
        EmployeeDAO employeeDAO = new EmployeeDAO();
        try{
            employeeDAO.insertEmployee(employee, subscription, "abc123");
            ArrayList<Employee> employees = companyController.viewEmployeesInfo("abc123");
            assertEquals(employees.get(employees.size()-1).getIdcode(), employee.getIdcode());
            assertEquals(employees.get(employees.size()-1).getName(), employee.getName());
            assertEquals(employees.get(employees.size()-1).getSurname(), employee.getSurname());
            assertEquals(employees.get(employees.size()-1).getAge(), employee.getAge());
            assertEquals(employees.get(employees.size()-1).getRole(), employee.getRole());
            assertEquals(employees.get(employees.size()-1).getSubscription().getMeetings(), employee.getSubscription().getMeetings());
            assertEquals(employees.get(employees.size()-1).getSubscription().getFee(), employee.getSubscription().getFee());
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                employeeDAO.delete(employee);
            } catch (SQLException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}
