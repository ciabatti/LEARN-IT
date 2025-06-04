package Orm;

import DomainModel.Trainer;
import DomainModel.Workshift;

import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.AbstractMap;
import java.util.ArrayList;

public class WorkshiftDAO {

    public ArrayList<Workshift> getWorkshifts() throws SQLException, ClassNotFoundException {
         Connection con = ConnectionManager.getConnection();

         String sql = "SELECT * FROM workshifts";
         PreparedStatement ps = con.prepareStatement(sql);
         ResultSet rs = ps.executeQuery();

         ArrayList <Workshift> workshifts = new ArrayList<Workshift>();

         while (rs.next()) {
             String date = rs.getDate("date").toString();
             String time = rs.getTime("time").toString();
             Workshift workshift = new Workshift(date, time);
             workshifts.add(workshift);
         }

         ps.close();
         return workshifts;
    }


    public  ArrayList <Workshift> getIndividualWorkshift (Trainer trainer) throws SQLException, ClassNotFoundException {
        Connection con = ConnectionManager.getConnection();

        String sql = "SELECT * FROM workshifts_trainer WHERE email = ?";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setString(1, trainer.getEmail());
        ResultSet rs = ps.executeQuery();

        ArrayList <Workshift> workshifts = new ArrayList<Workshift>();

        while (rs.next()) {
            String date = rs.getDate("date").toString();
            String time = rs.getTime("time").toString();
            Workshift workshift = new Workshift(date, time);
            workshifts.add(workshift);
        }

        ps.close();
        return workshifts;
    }

    public ArrayList<AbstractMap.SimpleEntry<Workshift, Trainer>> getAllIndividualWorkshift() throws SQLException, ClassNotFoundException {
        Connection con = ConnectionManager.getConnection();

        String sql = "SELECT * FROM workshifts_trainer";
        PreparedStatement ps = con.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();

        ArrayList<AbstractMap.SimpleEntry<Workshift, Trainer>> workshifts = new ArrayList<>();

        while (rs.next()) {
            String date = rs.getDate("date").toString();
            String time = rs.getTime("time").toString();
            String tr_mail = rs.getString("email");
            Workshift workshift = new Workshift(date, time);
            Trainer trainer = new TrainerDAO().getTrainerbyemail(tr_mail);
            AbstractMap.SimpleEntry<Workshift, Trainer> entry = new AbstractMap.SimpleEntry<Workshift, Trainer>(workshift, trainer);
            workshifts.add(entry);
        }
        ps.close();
        return workshifts;
    }


    public void insert (Trainer trainer, Workshift workshift) throws SQLException, ParseException, ClassNotFoundException {
        Connection con = ConnectionManager.getConnection();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

        String sql = "INSERT INTO workshifts_trainer (email, date, time) VALUES (?, ?, ?)";
        PreparedStatement ps = con.prepareStatement(sql);
        java.util.Date utilDate = format.parse(workshift.getDate());
        Date sqlDate = new Date(utilDate.getTime());
        LocalTime localTime = LocalTime.parse(workshift.getTime(), DateTimeFormatter.ofPattern("HH:mm:ss"));
        ps.setString(1, trainer.getEmail());
        ps.setDate(2, sqlDate);
        ps.setTime(3, Time.valueOf(localTime));
        ps.executeUpdate();

        ps.close();
    }

    public void delete(Workshift workshift, Trainer trainer) throws SQLException, ClassNotFoundException, ParseException {
        Connection con = ConnectionManager.getConnection();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

        String sql = "DELETE FROM workshifts_trainer WHERE email = ? AND date = ? AND time = ? ";
        PreparedStatement ps = con.prepareStatement(sql);
        java.util.Date utilDate = format.parse(workshift.getDate());
        Date sqlDate = new Date(utilDate.getTime());
        LocalTime localTime = LocalTime.parse(workshift.getTime(), DateTimeFormatter.ofPattern("HH:mm:ss"));
        ps.setString(1, trainer.getEmail());
        ps.setDate(2, sqlDate);
        ps.setTime(3, Time.valueOf(localTime));
        ps.executeUpdate();

        ps.close();
    }

    public void modify (Trainer trainer, Workshift newworkshift, Workshift oldworkshift) throws SQLException, ParseException, ClassNotFoundException {
        delete(oldworkshift, trainer);
        insert(trainer, newworkshift);
    }

    public ArrayList<String> getDates() throws SQLException, ClassNotFoundException {
        Connection con = ConnectionManager.getConnection();
        String sql = "SELECT DISTINCT date FROM workshifts";
        PreparedStatement ps = con.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();

        ArrayList <String> dates = new ArrayList<String>();

        while (rs.next()) {
            String date = rs.getDate("date").toString();
            dates.add(date);
        }

        ps.close();
        return dates;
    }

    public void addWorkshift(Workshift workshift) throws SQLException, ClassNotFoundException, ParseException {
        Connection con = ConnectionManager.getConnection();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

        String sql = "INSERT INTO workshifts (date, time) VALUES (?, ?)";
        PreparedStatement ps = con.prepareStatement(sql);
        java.util.Date utilDate = format.parse(workshift.getDate());
        Date sqlDate = new Date(utilDate.getTime());
        LocalTime localTime = LocalTime.parse(workshift.getTime(), DateTimeFormatter.ofPattern("HH:mm:ss"));
        ps.setDate(1, sqlDate);
        ps.setTime(2, Time.valueOf(localTime));
        ps.executeUpdate();
        ps.close();
    }

    public void deleteWorkshift(Workshift workshift) throws SQLException, ClassNotFoundException, ParseException {
        Connection con = ConnectionManager.getConnection();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String sql = "DELETE FROM workshifts WHERE date = ? AND time = ?";
        PreparedStatement ps = con.prepareStatement(sql);
        java.util.Date utilDate = format.parse(workshift.getDate());
        Date sqlDate = new Date(utilDate.getTime());
        LocalTime localTime = LocalTime.parse(workshift.getTime(), DateTimeFormatter.ofPattern("HH:mm:ss"));
        ps.setDate(1, sqlDate);
        ps.setTime(2, Time.valueOf(localTime));
        ps.executeUpdate();
        ps.close();
    }
}
