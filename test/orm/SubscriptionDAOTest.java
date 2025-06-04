package orm;

import DomainModel.*;
import Orm.SubscriptionDAO;
import Orm.EmployeeDAO;

import org.junit.jupiter.api.*;

import java.sql.SQLException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SubscriptionDAOTest {

    private static SubscriptionDAO subscriptionDAO;
    private static EmployeeDAO employeeDAO;

    private static final String TEST_IDCODE = "test123";
    private static final String TEST_COMPANY_IDCODE = "comp123";

    @BeforeAll
    public static void setup() throws SQLException, ClassNotFoundException {
        subscriptionDAO = new SubscriptionDAO();
        employeeDAO = new EmployeeDAO();

        // Create a test employee with correct constructor params: idcode, name, surname, age, role
        Employee testEmployee = new Employee(TEST_IDCODE, "John", "Doe", 30, "Developer");

        // Create a subscription for the employee
        Subscription testSubscription = new Subscription(5, testEmployee, new MultipleEmployeesFee(), false);

        // Insert the test employee and subscription into the database
        try {
            employeeDAO.insertEmployee(testEmployee, testSubscription, TEST_COMPANY_IDCODE);
        } catch (Exception e) {
            // Employee might already exist, ignore exception
        }
    }

    @Test
    @Order(1)
    public void testGetEmployeeInfo() {
        try {
            // Retrieve subscription info for the test employee
            Subscription subscription = subscriptionDAO.getEmployeeInfo(TEST_IDCODE);

            assertNotNull(subscription, "Subscription should not be null");
            assertEquals(TEST_IDCODE, subscription.getEmployee().getIdcode(), "Employee idcode should match");
            assertTrue(subscription.getMeetings() >= 0, "Meetings count should be non-negative");
            assertNotNull(subscription.getFeeStrategy(), "FeeStrategy should not be null");
        } catch (SQLException | ClassNotFoundException e) {
            fail("Exception during getEmployeeInfo: " + e.getMessage());
        }
    }

    @Test
    @Order(2)
    public void testEditFeeStrategy() {
        try {
            // Change fee strategy to MultipleEmployeesFee
            subscriptionDAO.editFeeStrategy(TEST_IDCODE, new MultipleEmployeesFee());

            Subscription subscription = subscriptionDAO.getEmployeeInfo(TEST_IDCODE);
            assertTrue(subscription.getFeeStrategy() instanceof MultipleEmployeesFee, "FeeStrategy should be MultipleEmployeesFee");

            // Change fee strategy to SingleEmployeeFee
            subscriptionDAO.editFeeStrategy(TEST_IDCODE, new SingleEmployeeFee());

            subscription = subscriptionDAO.getEmployeeInfo(TEST_IDCODE);
            assertTrue(subscription.getFeeStrategy() instanceof SingleEmployeeFee, "FeeStrategy should be SingleEmployeeFee");

        } catch (SQLException | ClassNotFoundException e) {
            fail("Exception during editFeeStrategy: " + e.getMessage());
        }
    }

    @Test
    @Order(3)
    public void testEditFeePaid() {
        try {
            // Set fee paid status to true
            subscriptionDAO.editFeePaid(TEST_IDCODE, true);
            Subscription subscription = subscriptionDAO.getEmployeeInfo(TEST_IDCODE);
            assertTrue(subscription.isPaid(), "Fee paid status should be true");

            // Set fee paid status to false
            subscriptionDAO.editFeePaid(TEST_IDCODE, false);
            subscription = subscriptionDAO.getEmployeeInfo(TEST_IDCODE);
            assertFalse(subscription.isPaid(), "Fee paid status should be false");

        } catch (SQLException | ClassNotFoundException e) {
            fail("Exception during editFeePaid: " + e.getMessage());
        }
    }

    @Test
    @Order(4)
    public void testGetCompanysNotPaid() {
        try {
            // Get companies with employees that have not paid fees
            ArrayList<Company> companies = subscriptionDAO.getCompanysNotPaid();

            assertNotNull(companies, "Companies list should not be null");

            // The list can be empty, but each company must have non-null idcode and email
            companies.forEach(company -> {
                assertNotNull(company.getIdcode(), "Company idcode should not be null");
                assertNotNull(company.getEmail(), "Company email should not be null");
            });

        } catch (SQLException | ClassNotFoundException e) {
            fail("Exception during getCompanysNotPaid: " + e.getMessage());
        }
    }

    @AfterAll
    public static void cleanup() throws SQLException, ClassNotFoundException {
        // Remove test employee from database after tests
        Employee testEmployee = new Employee(TEST_IDCODE, "John", "Doe", 30, "Developer");
        employeeDAO.delete(testEmployee);
    }
}
