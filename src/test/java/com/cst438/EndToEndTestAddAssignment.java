package com.cst438;

import com.cst438.domain.*;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class EndToEndTestAddAssignment {

    public static final String CHROME_DRIVER_FILE_LOCATION = "C:/chromedriver.exe";
    public static final String URL = "https://cst438grade-fe-alt.herokuapp.com//";
    public static final String TEST_USER_EMAIL = "test@csumb.edu";
    public static final String TEST_INSTRUCTOR_EMAIL = "dwisneski@csumb.edu";
    public static final int SLEEP_DURATION = 1000; // 1 second.

    public static final String TEST_ASSIGNMENT_NAME = "Test Assignment";
    public static final int TEST_COURSE_ID = 123456;
    public static final String TEST_DUE_DATE = "20221025";
    public static final String TEST_STUDENT_NAME = "Test";
    public static final String TEST_COURSE_TITLE = "Test Course";

    @Autowired
    EnrollmentRepository enrollmentRepository;

    @Autowired
    CourseRepository courseRepository;

    @Autowired
    AssignmentGradeRepository assignnmentGradeRepository;

    @Autowired
    AssignmentRepository assignmentRepository;

    @Test
    public void addAssignmentTest() throws Exception {

        // Database setup: create course
        Course c = new Course();
        c.setCourse_id(TEST_COURSE_ID);
        c.setInstructor(TEST_INSTRUCTOR_EMAIL);
        c.setSemester("Fall");
        c.setYear(2022);
        c.setTitle(TEST_COURSE_TITLE);

        // add a student TEST into course
        Enrollment e = new Enrollment();
        e.setCourse(c);
        e.setStudentEmail(TEST_USER_EMAIL);
        e.setStudentName(TEST_STUDENT_NAME);

        courseRepository.save(c);
        e = enrollmentRepository.save(e);

        new AssignmentGrade();
        AssignmentGrade ag;

        System.setProperty("webdriver.chrome.driver", CHROME_DRIVER_FILE_LOCATION);
        WebDriver driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);

        driver.get(URL);
        Thread.sleep(SLEEP_DURATION);

        try {

            driver.findElement(By.xpath("//AddAssignment[@id='add-assignment']")).click();
            Thread.sleep(SLEEP_DURATION);

            WebElement we = driver.findElement(By.xpath("//TextField[@name='assignmentName']"));
            we.sendKeys(TEST_ASSIGNMENT_NAME);

            we = driver.findElement(By.xpath("//TextField[@name='courseId']"));
            we.sendKeys(Integer.toString(TEST_COURSE_ID));

            we = driver.findElement(By.xpath("//TextField[@name='dueDate']"));
            we.sendKeys(TEST_DUE_DATE);

            driver.findElement(By.xpath("//button[@id='create-assignment']")).click();
            Thread.sleep(SLEEP_DURATION);

            List<WebElement> elements = driver.findElements(By.xpath("//div[@data-field='assignmentName']/div"));
            boolean found_assignment = false;
            for (WebElement web_e : elements) {
                System.out.println(web_e.getText()); // for debug
                if (web_e.getText().equals(TEST_ASSIGNMENT_NAME)) {
                    found_assignment = true;
                    web_e.findElement(By.xpath("descendant::input")).click();
                    break;
                }
            }
            assertTrue(found_assignment, "Unable to locate Assignment.");

        } catch (Exception ex) {
            throw ex;
        } finally {

            // clean up database.
            List<Assignment> list_a = assignmentRepository.findNeedGradingByEmail(TEST_INSTRUCTOR_EMAIL);
            for (Assignment a : list_a) {

                ag = assignnmentGradeRepository.findByAssignmentIdAndStudentEmail(a.getId(), TEST_USER_EMAIL);
                if (ag != null)
                    assignnmentGradeRepository.delete(ag);
                assignmentRepository.delete(a);
            }
            enrollmentRepository.delete(e);
            courseRepository.delete(c);

            driver.quit();
        }

    }
}