package orm;

import DomainModel.Material;
import DomainModel.Slide;
import DomainModel.Video;
import Orm.MaterialDAO;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class MaterialDAOTest {

    private static MaterialDAO materialDAO;

    @BeforeAll
    public static void setup() {
        materialDAO = new MaterialDAO();
    }

    private static final String TEST_FILE = "test/resources/testfile.pdf";
    private static final String TEST_EMAIL = "trainer@example.com";

    @Test
    @Order(1)
    public void testUploadMaterial() {
        try {
            materialDAO.uploadMaterial(TEST_FILE, TEST_EMAIL, true); // true = slide
        } catch (IOException | SQLException | ParseException | ClassNotFoundException e) {
            fail("Upload material fallito: " + e.getMessage());
        }
    }

    @Test
    @Order(2)
    public void testGetMaterialByFilename() {
        try {
            Material m = materialDAO.getMaterialbyfilename("testfile.pdf");
            assertNotNull(m, "Material non trovato");
            assertEquals("testfile.pdf", m.getFilename());
        } catch (SQLException | ClassNotFoundException e) {
            fail("Recupero material fallito: " + e.getMessage());
        }
    }

    @Test
    @Order(3)
    public void testGetAllMaterial() {
        try {
            ArrayList<Material> materials = materialDAO.getAllMaterial();
            assertNotNull(materials, "Lista materiali nulla");
            assertTrue(materials.size() > 0, "Lista materiali vuota");
        } catch (SQLException | ClassNotFoundException e) {
            fail("Recupero tutti materiali fallito: " + e.getMessage());
        }
    }

    @Test
    @Order(4)
    public void testDeleteMaterial() {
        try {
            materialDAO.deleteMaterial("testfile.pdf");
            Material m = materialDAO.getMaterialbyfilename("testfile.pdf");
            assertNull(m, "Material non eliminato");
        } catch (SQLException | ClassNotFoundException e) {
            fail("Eliminazione material fallita: " + e.getMessage());
        }
    }
}
