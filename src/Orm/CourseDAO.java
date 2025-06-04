package Orm;

import DomainModel.FocusCourse;
import DomainModel.Course;

import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class CourseDAO {

    public int getId(FocusCourse focus) throws SQLException, ClassNotFoundException {
        Connection con = ConnectionManager.getConnection();

        String sql = "SELECT * FROM focuscourse WHERE name = ?";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setString(1, focus.toString());
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            int id = rs.getInt("id");
            return id;
        }
        return -1;
    }

    public FocusCourse getFocusCourse(int id) throws SQLException, ClassNotFoundException {
        Connection con = ConnectionManager.getConnection();

        String sql = "SELECT * FROM focuscourse WHERE id = ?";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, id);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            String name = rs.getString("name");
            FocusCourse focus = FocusCourse.valueOf(name);
            return focus;
        }
        return null;
    }

    public ArrayList<Course> getAllCourses() throws SQLException, ClassNotFoundException {
        Connection con = ConnectionManager.getConnection();

        String sql = "SELECT * FROM courses";
        PreparedStatement ps = con.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();

        ArrayList<Course> courses = new ArrayList<Course>();
        while (rs.next()) {
            String date = rs.getDate("date").toString();
            String time = rs.getTime("time").toString();
            String description = rs.getString("description");
            FocusCourse focus = getFocusCourse(rs.getInt("focuscourse"));
            Course course = new Course(date, time, description, focus);
            courses.add(course);
        }
        ps.close();
        return courses;
    }

    public void insertCourse (Course course) throws SQLException, ParseException, ClassNotFoundException {
        Connection con = ConnectionManager.getConnection();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

        String sql = "INSERT INTO courses ( date, time, description, focuscourse) VALUES (?, ?, ?, ?)";
        PreparedStatement ps = con.prepareStatement(sql);

        java.util.Date utilDate = format.parse(course.getDate());
        Date sqlDate = new Date(utilDate.getTime());
        LocalTime localTime = LocalTime.parse(course.getTime(), DateTimeFormatter.ofPattern("HH:mm:ss"));
        ps.setDate(1, sqlDate);
        ps.setTime(2, Time.valueOf(localTime));
        ps.setString(3, course.getDescription());
        ps.setInt(4, getId(course.getType()));
        ps.executeUpdate();
        ps.close();

    }

    public void deleteCourse (Course course) throws SQLException, ParseException, ClassNotFoundException {
        Connection con = ConnectionManager.getConnection();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

        String sql = "DELETE FROM courses WHERE date = ? AND time = ?";
        PreparedStatement ps = con.prepareStatement(sql);
        java.util.Date utilDate = format.parse(course.getDate());
        Date sqlDate = new Date(utilDate.getTime());
        LocalTime localTime = LocalTime.parse(course.getTime(), DateTimeFormatter.ofPattern("HH:mm:ss"));
        ps.setDate(1, sqlDate);
        ps.setTime(2, Time.valueOf(localTime));
        ps.executeUpdate();
        ps.close();
    }

    public void modifyCourse (Course course) throws SQLException, ParseException, ClassNotFoundException {
        Connection con = ConnectionManager.getConnection();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

        String sql = "UPDATE courses SET description = ?, focuscourse = ? WHERE date = ? AND time = ?";
        PreparedStatement ps = con.prepareStatement(sql);

        java.util.Date utilDate = format.parse(course.getDate());
        Date sqlDate = new Date(utilDate.getTime());
        LocalTime localTime = LocalTime.parse(course.getTime(), DateTimeFormatter.ofPattern("HH:mm:ss"));
        ps.setDate(3, sqlDate);
        ps.setTime(4, Time.valueOf(localTime));
        ps.setString(1, course.getDescription());
        ps.setInt(2, getId(course.getType()));

        ps.executeUpdate();
        ps.close();
    }

    public Course getCoursebyDateAndTime(String date, String time) throws SQLException, ParseException, ClassNotFoundException {
        Connection con = ConnectionManager.getConnection();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

        String sql = "SELECT * FROM courses WHERE date = ? AND time = ?";
        PreparedStatement ps = con.prepareStatement(sql);

        java.util.Date utilDate = format.parse(date);
        Date sqlDate = new Date(utilDate.getTime());
        LocalTime localTime = LocalTime.parse(time, DateTimeFormatter.ofPattern("HH:mm:ss"));
        ps.setDate(1, sqlDate);
        ps.setTime(2, Time.valueOf(localTime));

        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            String description = rs.getString("description");
            FocusCourse type = getFocusCourse(rs.getInt("focuscourse"));
            Course course = new Course(date, time, description, type);
            return course;
        }
        return null;
    }
}
