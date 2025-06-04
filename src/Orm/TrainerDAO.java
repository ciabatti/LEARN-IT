package Orm;

import DomainModel.Trainer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class TrainerDAO {

    public ArrayList<Trainer> getAllTrainers() throws SQLException, ClassNotFoundException {
        Connection con = ConnectionManager.getConnection();

        String sql = "SELECT * FROM trainers";
        PreparedStatement ps = con.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();

        ArrayList <Trainer> trainers = new ArrayList<Trainer>();

        while(rs.next()) {
              String email = rs.getString("email");
              String name = rs.getString("name");
              String surname = rs.getString("surname");
              Trainer trainer = new Trainer(email, name, surname);
              trainers.add(trainer);
        }

        ps.close();
        return trainers;
    }

    public ArrayList <Trainer> getTrainersShifts() throws SQLException, ClassNotFoundException {
        Connection con = ConnectionManager.getConnection();

        String sql = "SELECT * FROM trainers ";
        PreparedStatement ps = con.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();

        ArrayList <Trainer> trainers = new ArrayList<Trainer>();
        WorkshiftDAO workshiftDAO = new WorkshiftDAO();

        while (rs.next()) {
            String email = rs.getString("email");
            String name = rs.getString("name");
            String surname = rs.getString("surname");
            Trainer trainer = new Trainer(email, name, surname);
            trainer.setWorkshifts(workshiftDAO.getIndividualWorkshift(trainer));
            trainers.add(trainer);
        }

        ps.close();
        return trainers;
    }

    public Trainer getTrainerbyemail(String email) throws SQLException, ClassNotFoundException {
        Connection con = ConnectionManager.getConnection();

        String sql = "SELECT * FROM trainers WHERE email = ?";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setString(1, email);
        ResultSet rs = ps.executeQuery();

        Trainer trainer = null;

        while(rs.next()) {
              String name = rs.getString("name");
              String surname = rs.getString("surname");
              trainer = new Trainer(email, name, surname);
        }

        ps.close();
        return trainer;
    }

    public void addTrainer(Trainer trainer) throws SQLException, ClassNotFoundException {
        Connection con = ConnectionManager.getConnection();
        String sql = "INSERT INTO trainers (email, name, surname) VALUES (?, ?, ?)";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setString(1, trainer.getEmail());
        ps.setString(2, trainer.getName());
        ps.setString(3, trainer.getSurname());
        ps.executeUpdate();
    }

    public void deleteTrainer(String email) throws SQLException, ClassNotFoundException {
        Connection con = ConnectionManager.getConnection();
        String sql = "DELETE FROM trainers WHERE email = ?";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setString(1, email);
        ps.executeUpdate();
    }

}
