package orm;

import DomainModel.Trainer;
import DomainModel.Workshift;
import Orm.WorkshiftDAO;
import Orm.TrainerDAO;

import org.junit.jupiter.api.*;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class WorkshiftDAOTest {

    private static WorkshiftDAO workshiftDAO;
    private static TrainerDAO trainerDAO;

    private static Trainer testTrainer;
    private static final String TEST_EMAIL = "trainer@example.com"; // deve esistere nel DB o crearne uno per test
    private static Workshift testWorkshift1;
    private static Workshift testWorkshift2;

    @BeforeAll
    public static void setup() {
        workshiftDAO = new WorkshiftDAO();
        trainerDAO = new TrainerDAO();
        try {
            testTrainer = trainerDAO.getTrainerbyemail(TEST_EMAIL);
            if (testTrainer == null) {
                testTrainer = new Trainer(TEST_EMAIL, "Test", "Trainer");
            }
        } catch (Exception e) {
            fail("Setup trainer fallito: " + e.getMessage());
        }

        testWorkshift1 = new Workshift("2025-06-01", "09:00:00");
        testWorkshift2 = new Workshift("2025-06-01", "10:00:00");
    }

    @Test
    @Order(1)
    public void testAddWorkshift() {
        try {
            workshiftDAO.addWorkshift(testWorkshift1);
        } catch (Exception e) {
            fail("Aggiunta workshift fallita: " + e.getMessage());
        }
    }

    @Test
    @Order(2)
    public void testGetWorkshifts() {
        try {
            ArrayList<Workshift> workshifts = workshiftDAO.getWorkshifts();
            assertNotNull(workshifts);
            assertTrue(workshifts.stream().anyMatch(w -> w.getDate().equals(testWorkshift1.getDate()) && w.getTime().equals(testWorkshift1.getTime())));
        } catch (Exception e) {
            fail("Recupero workshifts fallito: " + e.getMessage());
        }
    }

    @Test
    @Order(3)
    public void testInsertIndividualWorkshift() {
        try {
            workshiftDAO.insert(testTrainer, testWorkshift1);
        } catch (Exception e) {
            fail("Inserimento workshift individuale fallito: " + e.getMessage());
        }
    }

    @Test
    @Order(4)
    public void testGetIndividualWorkshift() {
        try {
            ArrayList<Workshift> individualShifts = workshiftDAO.getIndividualWorkshift(testTrainer);
            assertNotNull(individualShifts);
            assertTrue(individualShifts.stream().anyMatch(w -> w.getDate().equals(testWorkshift1.getDate()) && w.getTime().equals(testWorkshift1.getTime())));
        } catch (Exception e) {
            fail("Recupero workshift individuale fallito: " + e.getMessage());
        }
    }

    @Test
    @Order(5)
    public void testModifyWorkshift() {
        try {
            workshiftDAO.modify(testTrainer, testWorkshift2, testWorkshift1);
            // dopo modifica, testWorkshift1 non dovrebbe più esistere, testWorkshift2 sì
            ArrayList<Workshift> individualShifts = workshiftDAO.getIndividualWorkshift(testTrainer);
            assertFalse(individualShifts.stream().anyMatch(w -> w.getDate().equals(testWorkshift1.getDate()) && w.getTime().equals(testWorkshift1.getTime())));
            assertTrue(individualShifts.stream().anyMatch(w -> w.getDate().equals(testWorkshift2.getDate()) && w.getTime().equals(testWorkshift2.getTime())));
        } catch (Exception e) {
            fail("Modifica workshift fallita: " + e.getMessage());
        }
    }

    @Test
    @Order(6)
    public void testDeleteIndividualWorkshift() {
        try {
            workshiftDAO.delete(testWorkshift2, testTrainer);
            ArrayList<Workshift> individualShifts = workshiftDAO.getIndividualWorkshift(testTrainer);
            assertFalse(individualShifts.stream().anyMatch(w -> w.getDate().equals(testWorkshift2.getDate()) && w.getTime().equals(testWorkshift2.getTime())));
        } catch (Exception e) {
            fail("Eliminazione workshift individuale fallita: " + e.getMessage());
        }
    }

    @Test
    @Order(7)
    public void testDeleteWorkshift() {
        try {
            workshiftDAO.deleteWorkshift(testWorkshift1);
            ArrayList<Workshift> workshifts = workshiftDAO.getWorkshifts();
            assertFalse(workshifts.stream().anyMatch(w -> w.getDate().equals(testWorkshift1.getDate()) && w.getTime().equals(testWorkshift1.getTime())));
        } catch (Exception e) {
            fail("Eliminazione workshift fallita: " + e.getMessage());
        }
    }

    @Test
    @Order(8)
    public void testGetDates() {
        try {
            ArrayList<String> dates = workshiftDAO.getDates();
            assertNotNull(dates);
            assertTrue(dates.size() >= 0);
        } catch (Exception e) {
            fail("Recupero date fallito: " + e.getMessage());
        }
    }


}
