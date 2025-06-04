package controllers;

import BusinessLogic.Admin;
import DomainModel.*;
import Orm.CourseDAO;
import Orm.EmployeeDAO;
import org.junit.Test;

import javax.mail.MessagingException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.*;

public class AdminTest {


    @Test
    public void setCourse(){
        Admin admin = new Admin();
        Course course = new Course("2025-12-12", "12:00:00", "test", FocusCourse.JAVA);
        try {
            admin.setCourse(course);
            CourseDAO courseDAO = new CourseDAO();
            Course a = courseDAO.getCoursebyDateAndTime(course.getDate(), course.getTime());
            assertEquals( course.getDate(), a.getDate());
            assertEquals( course.getTime(), a.getTime());
            assertEquals( course.getDescription(), a.getDescription());
            assertEquals( course.getType(), a.getType());
        } catch (SQLException | ParseException | MessagingException | ClassNotFoundException e) {
            e.printStackTrace();
        }finally{
            try {
                CourseDAO courseDAO = new CourseDAO();
                courseDAO.deleteCourse(course);
            } catch (SQLException | ParseException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }
    @Test
    public void  viewCourses(){
        Course course = new Course("2025-12-12", "12:00:00", "test", FocusCourse.JAVA);
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
    public void deleteCourse(){
        Course course = new Course("2025-06-01", "12:00:00", "test", FocusCourse.LATEX);
        CourseDAO courseDAO = new CourseDAO();
        Admin admin = new Admin();
        try{
            courseDAO.insertCourse(course);
            admin.deleteCourse(course);
            assertNull(courseDAO.getCoursebyDateAndTime(course.getDate(), course.getTime()));
        } catch (SQLException | ParseException | MessagingException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void modifyCourse(){
        Course course = new Course("2025-06-01", "12:00:00", "test", FocusCourse.LATEX);
        CourseDAO courseDAO = new CourseDAO();
        Admin admin = new Admin();
        try{
            courseDAO.insertCourse(course);
            Course newCourse = new Course("2025-06-01", "12:00:00", "test2", FocusCourse.JAVA);
            admin.modifyCourse(newCourse);
            Course a = courseDAO.getCoursebyDateAndTime(course.getDate(), course.getTime());
            assertEquals( newCourse.getDate(), a.getDate());
            assertEquals( newCourse.getTime(), a.getTime());
            assertEquals( newCourse.getDescription(), a.getDescription());
            assertEquals( newCourse.getType(), a.getType());
        } catch (SQLException | ParseException | MessagingException | ClassNotFoundException e) {
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
    public void viewEmployeesList(){
        Employee employee = new Employee("GGGVVV", "Giuseppe", "Verdi", 40, "tester");
        FeeStrategy feeStrategy = new SingleEmployeeFee();
        Subscription subscription = new Subscription(1, employee, feeStrategy, false);

        EmployeeDAO employeeDAO = new EmployeeDAO();
        Admin admin = new Admin();
        ArrayList<Employee> employees;

        try{
            employeeDAO.insertEmployee(employee, subscription, "CCCTTT");
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
}
