package orm;

import DomainModel.*;
import Orm.EmployeeDAO;
import Orm.CompanyDAO;
import Orm.SubscriptionDAO;

import org.junit.jupiter.api.*;

import java.sql.SQLException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class EmployeeDAOTest {

    private static EmployeeDAO employeeDAO;
    private static SubscriptionDAO subscriptionDAO;
    private static CompanyDAO companyDAO;

    private static final String TEST_IDCODE = "test_emp_001";
    private static final String TEST_COMPANY_ID = "test_comp_001";

    private static Employee testEmployee;
    private static Subscription testSubscription;

    @BeforeAll
    public static void setup() throws SQLException, ClassNotFoundException {
        employeeDAO = new EmployeeDAO();
        subscriptionDAO = new SubscriptionDAO();
        companyDAO = new CompanyDAO();

        // Prepare test Employee and Subscription objects
        testEmployee = new Employee(TEST_IDCODE, "Alice", "Smith", 30, "Developer");
        testSubscription = new Subscription(10, testEmployee, new MultipleEmployeesFee(), false);

        // Insert test employee to DB for retrieval and update tests
        employeeDAO.insertEmployee(testEmployee, testSubscription, TEST_COMPANY_ID);
    }

    @Test
    @Order(1)
    public void testGetCompany() {
        try {
            // Retrieve the company associated with the test employee's idcode
            Company company = employeeDAO.getCompany(TEST_IDCODE);

            // Company may be null if the test DB doesn't have it, but method should not throw exceptions
            if (company != null) {
                assertNotNull(company.getIdcode(), "Company idcode should not be null");
                assertNotNull(company.getName(), "Company name should not be null");
            }
        } catch (SQLException | ClassNotFoundException e) {
            fail("Exception during getCompany: " + e.getMessage());
        }
    }

    @Test
    @Order(2)
    public void testGetAllEmployees() {
        try {
            // Fetch all employees from the database
            ArrayList<Employee> employees = employeeDAO.getAllEmployees();

            assertNotNull(employees, "Employees list should not be null");
            assertTrue(employees.size() > 0, "Employees list should have at least one employee");

            // Check that the test employee is in the list
            boolean foundTestEmployee = employees.stream()
                    .anyMatch(emp -> emp.getIdcode().equals(TEST_IDCODE));
            assertTrue(foundTestEmployee, "Test employee should be present in all employees list");

        } catch (SQLException | ClassNotFoundException e) {
            fail("Exception during getAllEmployees: " + e.getMessage());
        }
    }

    @Test
    @Order(3)
    public void testGetEmployee() {
        try {
            // Retrieve specific employee by idcode
            Employee employee = employeeDAO.getEmployee(TEST_IDCODE);

            assertNotNull(employee, "Employee should not be null");
            assertEquals(TEST_IDCODE, employee.getIdcode(), "Employee idcode should match");
            assertEquals("Alice", employee.getName(), "Employee name should match");
            assertNotNull(employee.getSubscription(), "Employee subscription should not be null");

        } catch (SQLException | ClassNotFoundException e) {
            fail("Exception during getEmployee: " + e.getMessage());
        }
    }

    @Test
    @Order(4)
    public void testGetEmployeesByCompany() {
        try {
            // Retrieve employees associated with a specific company id
            ArrayList<Employee> employees = employeeDAO.getEmployeesByCompany(TEST_COMPANY_ID);

            assertNotNull(employees, "Employees list should not be null");
            // It's possible no employees are linked, but test employee should appear if inserted properly
            boolean found = employees.stream()
                    .anyMatch(emp -> emp.getIdcode().equals(TEST_IDCODE));
            assertTrue(found, "Test employee should be present in company employees list");

        } catch (SQLException | ClassNotFoundException e) {
            fail("Exception during getEmployeesByCompany: " + e.getMessage());
        }
    }

    @Test
    @Order(5)
    public void testInsertAndDeleteEmployee() {
        try {
            // Prepare new employee and subscription
            Employee newEmployee = new Employee("temp_emp_002", "Bob", "Johnson", 40, "Manager");
            Subscription newSubscription = new Subscription(5, newEmployee, new SingleEmployeeFee(), false);

            // Insert new employee
            employeeDAO.insertEmployee(newEmployee, newSubscription, TEST_COMPANY_ID);

            // Verify insertion by fetching the employee
            Employee fetched = employeeDAO.getEmployee("temp_emp_002");
            assertNotNull(fetched, "Inserted employee should be retrievable");
            assertEquals("Bob", fetched.getName(), "Inserted employee name should match");

            // Delete the inserted employee
            employeeDAO.delete(newEmployee);

            // Verify deletion
            Employee deleted = employeeDAO.getEmployee("temp_emp_002");
            assertNull(deleted, "Deleted employee should no longer exist");

        } catch (SQLException | ClassNotFoundException e) {
            fail("Exception during insertAndDeleteEmployee: " + e.getMessage());
        }
    }

    @AfterAll
    public static void cleanup() throws SQLException, ClassNotFoundException {
        // Remove test employee from database after tests
        employeeDAO.delete(testEmployee);
    }
}
