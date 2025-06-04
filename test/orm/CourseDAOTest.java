package orm;

import DomainModel.Course;
import DomainModel.FocusCourse;
import Orm.CourseDAO;

import org.junit.jupiter.api.*;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CourseDAOTest {

    private static CourseDAO courseDAO;

    // Usa date e orari fissi per test (assicurati che non ci siano collisioni nel DB)
    private static final String TEST_DATE = "2025-06-01";
    private static final String TEST_TIME = "10:00:00";
    private static final String TEST_DESCRIPTION = "Learning Java for Beginners";
    private static final FocusCourse TEST_FOCUS = FocusCourse.JAVA; // adatta al tuo enum

    @BeforeAll
    public static void setup() throws SQLException, ClassNotFoundException {
        courseDAO = new CourseDAO();
    }

    @Test
    @Order(1)
    public void testInsertCourse() {
        Course course = new Course(TEST_DATE, TEST_TIME, TEST_DESCRIPTION, TEST_FOCUS);
        try {
            courseDAO.insertCourse(course);
        } catch (Exception e) {
            fail("Exception during insertCourse: " + e.getMessage());
        }
    }

    @Test
    @Order(2)
    public void testGetCourseByDateAndTime() {
        try {
            Course course = courseDAO.getCoursebyDateAndTime(TEST_DATE, TEST_TIME);
            assertNotNull(course, "Course should not be null");
            assertEquals(TEST_DESCRIPTION, course.getDescription());
            assertEquals(TEST_FOCUS, course.getType());
        } catch (Exception e) {
            fail("Exception during getCoursebyDateAndTime: " + e.getMessage());
        }
    }

    @Test
    @Order(3)
    public void testModifyCourse() {
        try {
            Course course = courseDAO.getCoursebyDateAndTime(TEST_DATE, TEST_TIME);
            assertNotNull(course);

            String newDescription = "Modified description";
            course.setDescription(newDescription);
            course.setType(FocusCourse.JAVA); // esempio cambio focus

            courseDAO.modifyCourse(course);

            Course modified = courseDAO.getCoursebyDateAndTime(TEST_DATE, TEST_TIME);
            assertNotNull(modified);
            assertEquals(newDescription, modified.getDescription());
            assertEquals(FocusCourse.JAVA, modified.getType());

        } catch (Exception e) {
            fail("Exception during modifyCourse: " + e.getMessage());
        }
    }

    @Test
    @Order(4)
    public void testGetAllCourses() {
        try {
            ArrayList<Course> courses = courseDAO.getAllCourses();
            assertNotNull(courses);
            assertTrue(courses.size() > 0, "There should be at least one course");
        } catch (Exception e) {
            fail("Exception during getAllCourses: " + e.getMessage());
        }
    }

    @Test
    @Order(5)
    public void testDeleteCourse() {
        try {
            Course course = courseDAO.getCoursebyDateAndTime(TEST_DATE, TEST_TIME);
            assertNotNull(course);

            courseDAO.deleteCourse(course);

            Course deleted = courseDAO.getCoursebyDateAndTime(TEST_DATE, TEST_TIME);
            assertNull(deleted, "Course should have been deleted");

        } catch (Exception e) {
            fail("Exception during deleteCourse: " + e.getMessage());
        }
    }
}
