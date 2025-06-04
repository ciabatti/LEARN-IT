package orm;

import DomainModel.Company;
import Orm.CompanyDAO;
import org.junit.jupiter.api.*;

import java.sql.SQLException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CompanyDAOTest {

    private static CompanyDAO companyDAO;

    @BeforeAll
    public static void setup() {
        companyDAO = new CompanyDAO();
    }

    // Dati test
    private final static Company TEST_COMPANY = new Company(
            "TEST123",
            "test@example.com",
            "TestName",
            "TestAddress",
            "1234567890"
    );
    @Test
    @Order(1)
    public void testAddCompany() {
        try {
            companyDAO.addCompany(TEST_COMPANY);
        } catch (SQLException | ClassNotFoundException e) {
            fail("Aggiunta company fallita: " + e.getMessage());
        }
    }

    @Test
    @Order(2)
    public void testGetCompany() {
        try {
            Company c = companyDAO.getCompany(TEST_COMPANY.getIdcode());
            assertNotNull(c, "Company non trovata");
            assertEquals(TEST_COMPANY.getEmail(), c.getEmail());
            assertEquals(TEST_COMPANY.getName(), c.getName());
            assertEquals(TEST_COMPANY.getAddress(), c.getAddress());
            assertEquals(TEST_COMPANY.getCellphone(), c.getCellphone());
        } catch (SQLException | ClassNotFoundException e) {
            fail("Recupero company fallito: " + e.getMessage());
        }
    }

    @Test
    @Order(3)
    public void testGetAllCompanies() {
        try {
            ArrayList<Company> companies = companyDAO.getAllCompanies();
            assertNotNull(companies, "Lista companies null");
            assertTrue(companies.size() > 0, "Lista companies vuota");
        } catch (SQLException | ClassNotFoundException e) {
            fail("Recupero tutte companies fallito: " + e.getMessage());
        }
    }

    @Test
    @Order(4)
    public void testDeleteCompany() {
        try {
            companyDAO.deleteCompany(TEST_COMPANY.getIdcode());
            Company c = companyDAO.getCompany(TEST_COMPANY.getIdcode());
            assertNull(c, "Company non eliminata");
        } catch (SQLException | ClassNotFoundException e) {
            fail("Eliminazione company fallita: " + e.getMessage());
        }
    }

}
