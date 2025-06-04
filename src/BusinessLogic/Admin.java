package BusinessLogic;

import com.google.common.collect.Lists;
import DomainModel.*;
import Orm.*;
import javax.mail.MessagingException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.*;

public class Admin {
    private final Notifier notifier;

    public Admin() {
        notifier = Notifier.getInstance();
    }

    public void setCourse(Course course) throws SQLException, ParseException, MessagingException, ClassNotFoundException {
        CourseDAO courseDAO = new CourseDAO();
        courseDAO.insertCourse(course);

        CompanyDAO companyDAO = new CompanyDAO();

        notifier.sendEmailCompany(companyDAO.getAllCompanies() , "New course", "a new course has been created. You can check it on the website.");

        TrainerDAO trainerDAO = new TrainerDAO();

        notifier.sendEmailTrainer(trainerDAO.getAllTrainers(), "New course", "a new course has been created. You can check it on the website.");
    }

    public void createWorkshifts() throws SQLException, ParseException, MessagingException, ClassNotFoundException {
        WorkshiftDAO workshiftDAO = new WorkshiftDAO();
        TrainerDAO trainerDAO = new TrainerDAO();
        ArrayList<Workshift> workshifts = workshiftDAO.getWorkshifts();
        ArrayList<Trainer> trainers = trainerDAO.getAllTrainers();

        for (String date : workshiftDAO.getDates()) {
            ArrayList<Workshift> wsdate = new ArrayList<>();
            for (Workshift workshift : workshifts) {
                if (Objects.equals(workshift.getDate(), date)) {
                    wsdate.add(workshift);
                }
            }
            Collections.shuffle(trainers);
            List<List<Trainer>> wstrainers = Lists.partition(trainers, wsdate.size());
            int i = 0;
            for(List<Trainer> wsed: wstrainers) {
                for (Trainer trainer : wsed) {
                    workshiftDAO.insert(trainer, wsdate.get(i));
                }
                i++;
            }
        }
        notifier.sendEmailTrainer(trainers, "Workshifts created", "your workshifts for the Trainers have been created. You can check them on the website.");
    }

    public void modifyWorkshift(Trainer trainer, Workshift oldworkshift, Workshift newworkshift ) throws SQLException, ParseException, MessagingException, ClassNotFoundException {
        WorkshiftDAO workshiftDAO = new WorkshiftDAO();
        workshiftDAO.modify(trainer, newworkshift, oldworkshift);
        ArrayList<Trainer> trainers = new ArrayList<>();
        trainers.add(trainer);
        notifier.sendEmailTrainer(trainers, "Workshift modified", "your workshift for the Trainers has been modified. You can check it on the website.");
    }

    public ArrayList<AbstractMap.SimpleEntry<Workshift, Trainer>> viewWorkshifts() throws SQLException, ClassNotFoundException {
        WorkshiftDAO workshiftDAO = new WorkshiftDAO();
        return workshiftDAO.getAllIndividualWorkshift();
    }

    public ArrayList<Course> viewCourses() throws SQLException, ClassNotFoundException {
        CourseDAO courseDAO = new CourseDAO();
        return courseDAO.getAllCourses();
    }

    public void deleteCourse(Course course) throws SQLException, MessagingException, ParseException, ClassNotFoundException {
        CourseDAO courseDAO = new CourseDAO();
        courseDAO.deleteCourse(course);

        CompanyDAO companyDAO = new CompanyDAO();
        notifier.sendEmailCompany(companyDAO.getAllCompanies(), "Course deleted", "an course has been deleted. You can check it on the website.");

        TrainerDAO trainerDAO = new TrainerDAO();
        notifier.sendEmailTrainer(trainerDAO.getAllTrainers(), "Course deleted", "an course has been deleted. You can check it on the website.");
    }

    public void modifyCourse(Course course) throws SQLException, ParseException, MessagingException, ClassNotFoundException {
        CourseDAO courseDAO = new CourseDAO();
        courseDAO.modifyCourse(course);

        CompanyDAO companyDAO = new CompanyDAO();
        notifier.sendEmailCompany(companyDAO.getAllCompanies(), "Course modified", "an course has been modified. You can check it on the website.");

        TrainerDAO trainerDAO = new TrainerDAO();
        notifier.sendEmailTrainer(trainerDAO.getAllTrainers(), "Course modified", "an course has been modified. You can check it on the website.");

    }

    public ArrayList<Employee> viewEmployeesList() throws SQLException, ClassNotFoundException {
        EmployeeDAO employeeDAO = new EmployeeDAO();
        return employeeDAO.getAllEmployees();
    }

    public void paymentReminder() throws SQLException, MessagingException, ClassNotFoundException {
        SubscriptionDAO subscriptionDAO = new SubscriptionDAO();
        ArrayList<Company> companies = subscriptionDAO.getCompanysNotPaid();

        EmployeeDAO employeeDAO = new EmployeeDAO();
        for (Company company : companies) {
            company.setEmployee(employeeDAO.getEmployeesByCompany(company.getIdcode()));
        }
        notifier.sendEmailCompany(companies, "Payment reminder", "you have to pay the trainers fee. You can check it on the website.");
    }








}
