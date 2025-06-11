package controllers;

import BusinessLogic.Admin;
import BusinessLogic.TrainerController;
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

public class TrainerControllerTest {
    @Test
    public void uploadSlidesAndVideos() {
        TrainerController trainerController = new TrainerController();
        Trainer trainer = new Trainer("test.test@mail.com", "Mario", "Rossi");
        TrainerDAO trainerDAO = new TrainerDAO();
        MaterialDAO materialDAO = new MaterialDAO();
        try {
            trainerDAO.addTrainer(trainer);
            trainerController.uploadSlideVideos("resources/testfile.pdf", trainer, true);
            ArrayList<Material> material = new ArrayList<>();
            material = materialDAO.getAllMaterial();
            assertEquals(material.get(material.size() - 1).getUploader().getEmail(), trainer.getEmail());
        } catch (SQLException | ParseException | MessagingException | IOException | ClassNotFoundException e) {
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
    public void viewSlidesAndVideos() {
        TrainerController trainerController = new TrainerController();
        Trainer trainer = new Trainer("test.test@mail.com", "Mario", "Rossi");
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
    public void deleteSlidesAndVideos() {
        TrainerController trainerController = new TrainerController();
        Trainer trainer = new Trainer("test.test@mail.com", "Mario", "Rossi");
        TrainerDAO trainerDAO = new TrainerDAO();
        MaterialDAO materialDAO = new MaterialDAO();
        try {
            trainerDAO.addTrainer(trainer);
            materialDAO.uploadMaterial("resources/testfile.pdf", trainer.getEmail(), true);
            ArrayList<Material> material = new ArrayList<>();
            material = materialDAO.getAllMaterial();
            trainerController.deleteSlideVideo(material.get(material.size() - 1));
            Material m = materialDAO.getMaterialbyfilename("testfile.pdf");
            assertNull(m);
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
    public void viewEmployeeInfo() {
        Employee employee = new Employee("ABC123", "Marta", "Verdi", 33, "test");
        FeeStrategy feeStrategy = new SingleEmployeeFee();
        Subscription subscription = new Subscription(1, employee, feeStrategy, false);
        employee.setSubscription(subscription);

        EmployeeDAO employeeDAO = new EmployeeDAO();
        TrainerController trainerController = new TrainerController();

        try{
            employeeDAO.insertEmployee(employee, subscription, "abc123");
            Employee c = trainerController.viewEmployeeInfo(employee.getIdcode());
            assertEquals(c.getIdcode(), employee.getIdcode());
            assertEquals(c.getName(), employee.getName());
            assertEquals(c.getSurname(), employee.getSurname());
            assertEquals(c.getAge(), employee.getAge());
            assertEquals(c.getSubscription().getFee(), employee.getSubscription().getFee());
        } catch (SQLException | ClassNotFoundException  e) {
            e.printStackTrace();
        } finally {
            try {
                employeeDAO.delete(employee);
            } catch (SQLException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    @Test
    public void viewEmployeeList(){
        Employee employee = new Employee("MMMVVV", "Marta", "Verdi", 33, "test");
        FeeStrategy feeStrategy = new SingleEmployeeFee();
        Subscription subscription = new Subscription(1, employee, feeStrategy, false);

        EmployeeDAO employeeDAO = new EmployeeDAO();
        Admin admin = new Admin();
        ArrayList<Employee> employees;

        try{
            employeeDAO.insertEmployee(employee, subscription, "abc123");
            employees = admin.viewEmployeesList();
            assertEquals( employees.get(employees.size()-1).getIdcode(), employee.getIdcode());
            assertEquals( employees.get(employees.size()-1).getName(), employee.getName());
            assertEquals( employees.get(employees.size()-1).getSurname(), employee.getSurname());
            assertEquals( employees.get(employees.size()-1).getAge(), employee.getAge());
            assertEquals( employees.get(employees.size()-1).getRole(), employee.getRole());
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                employeeDAO.delete(employee);
            } catch (SQLException |ClassNotFoundException e) {
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


}
