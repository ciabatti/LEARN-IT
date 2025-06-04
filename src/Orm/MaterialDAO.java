package Orm;

import DomainModel.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;


public class MaterialDAO {

    public ArrayList<Material> getAllMaterial() throws SQLException, ClassNotFoundException {
        Connection con = ConnectionManager.getConnection();

        String sql = "SELECT * FROM materials";
        PreparedStatement ps = con.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();

        ArrayList<Material> material = new ArrayList<Material>();
        TrainerDAO trainerDAO = new TrainerDAO();
        while (rs.next()) {
            String date = rs.getDate("date").toString();
            String description = rs.getTime("description").toString();
            String filename = rs.getString("filename");
            byte[] file = rs.getBytes("file");
            Trainer uploader = trainerDAO.getTrainerbyemail(rs.getString("tr_mail"));
            if(rs.getInt("type") == 1){
                Slide slide = new Slide(file, filename, date, description, uploader);
                material.add(slide);}
            else{
                Video video = new Video(file, filename, date, description, uploader);
                material.add(video);
            }
        }
        return material;
    }

    public void uploadMaterial(String filename, String tr_mail, boolean isSlide) throws IOException, SQLException, ParseException, ClassNotFoundException {
        File file = new File(filename);
        FileInputStream fis = new FileInputStream(file);
        Connection con = ConnectionManager.getConnection();
        PreparedStatement ps = con.prepareStatement("INSERT INTO materials (file, filename, date, description, tr_mail, type) VALUES (?, ?, ?, ?, ?, ?)");
        ps.setBinaryStream(1, fis, (int) file.length());
        ps.setString(2, file.getName());
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        java.util.Date utilDate = format.parse(LocalDate.now().toString());
        Date sqlDate = new Date(utilDate.getTime());

        ps.setDate(3, sqlDate);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        LocalTime localTime = LocalTime.parse(formatter.format(LocalTime.now()));
        ps.setTime(4, Time.valueOf(localTime));
        ps.setString(5, tr_mail);
        if(isSlide)
            ps.setInt(6, 1);
        else
            ps.setInt(6, 2);
        ps.executeUpdate();
        ps.close();
        fis.close();
    }

    public void deleteMaterial(String filename) throws SQLException, ClassNotFoundException {
        Connection con = ConnectionManager.getConnection();
        String sql = "DELETE FROM materials WHERE filename = ?";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setString(1, filename);
        ps.executeUpdate();
        ps.close();
    }

    public Material getMaterialbyfilename(String filename) throws SQLException, ClassNotFoundException {
        Connection con = ConnectionManager.getConnection();

        String sql = "SELECT * FROM materials WHERE filename = ?";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setString(1, filename);
        ResultSet rs = ps.executeQuery();

        Material material = null;
        TrainerDAO trainerDAO = new TrainerDAO();
        while (rs.next()) {
            String date = rs.getDate("date").toString();
            String description = rs.getString("description");
            byte[] file = rs.getBytes("file");
            Trainer uploader = trainerDAO.getTrainerbyemail(rs.getString("tr_mail"));
            if(rs.getInt("type") == 1){
                Slide slide = new Slide(file, filename, date, description, uploader);
                material = slide;}
            else{
                Video video = new Video(file, filename, date, description, uploader);
                material = video;
            }
        }
        return material;
    }
}

