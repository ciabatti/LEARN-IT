package orm;

import DomainModel.Trainer;
import Orm.TrainerDAO;
import Orm.WorkshiftDAO;

import org.junit.jupiter.api.*;

import java.sql.SQLException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TrainerDAOTest {

    private static TrainerDAO trainerDAO;

    private static final String TEST_EMAIL = "test.trainer@example.com";
    private static Trainer testTrainer;

    @BeforeAll
    public static void setup() {
        trainerDAO = new TrainerDAO();
        testTrainer = new Trainer(TEST_EMAIL, "TestName", "TestSurname");
    }

    @Test
    @Order(1)
    public void testAddTrainer() {
        try {
            trainerDAO.addTrainer(testTrainer);
        } catch (Exception e) {
            fail("Try to add a trainer failed: " + e.getMessage());
        }
    }

    @Test
    @Order(2)
    public void testGetTrainerByEmail() {
        try {
            Trainer t = trainerDAO.getTrainerbyemail(TEST_EMAIL);
            assertNotNull(t);
            assertEquals(TEST_EMAIL, t.getEmail());
            assertEquals(testTrainer.getName(), t.getName());
            assertEquals(testTrainer.getSurname(), t.getSurname());
        } catch (Exception e) {
            fail("Recupero trainer fallito: " + e.getMessage());
        }
    }

    @Test
    @Order(3)
    public void testGetAllTrainers() {
        try {
            ArrayList<Trainer> trainers = trainerDAO.getAllTrainers();
            assertNotNull(trainers);
            assertTrue(trainers.stream().anyMatch(t -> t.getEmail().equals(TEST_EMAIL)));
        } catch (Exception e) {
            fail("Recupero lista trainers fallito: " + e.getMessage());
        }
    }

    @Test
    @Order(4)
    public void testGetTrainersShifts() {
        try {
            ArrayList<Trainer> trainers = trainerDAO.getTrainersShifts();
            assertNotNull(trainers);
            // Il trainer inserito dovrebbe comparire nella lista, anche se non ha turni
            assertTrue(trainers.stream().anyMatch(t -> t.getEmail().equals(TEST_EMAIL)));
            // Per ogni trainer i workshifts non devono essere null (possono essere vuoti)
            trainers.forEach(t -> assertNotNull(t.getWorkshifts()));
        } catch (Exception e) {
            fail("Recupero trainers con turni fallito: " + e.getMessage());
        }
    }

    @Test
    @Order(5)
    public void testDeleteTrainer() {
        try {
            trainerDAO.deleteTrainer(TEST_EMAIL);
            Trainer t = trainerDAO.getTrainerbyemail(TEST_EMAIL);
            assertNull(t);
        } catch (Exception e) {
            fail("Cancellazione trainer fallita: " + e.getMessage());
        }
    }
}
