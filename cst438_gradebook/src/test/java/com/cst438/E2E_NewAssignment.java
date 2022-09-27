package com.cst438;

import static org.junit.jupiter.api.Assertions.assertTrue;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import com.cst438.domain.Assignment;
import com.cst438.domain.AssignmentRepository;
import com.cst438.domain.Course;
import com.cst438.domain.CourseRepository;

@SpringBootTest
public class E2E_NewAssignment {

   //Test variables
   public static final String CHROME_DRIVER_FILE_LOCATION = "/Users/marilyn/Desktop/chromedriver";
   public static final String URL = "http://localhost:3000/AddAssignment";
   public static final String TEST_ASSIGNMENT_NAME = "testassign";
   public static final int TEST_COURSE_ID = 40443; 
   public static final String TEST_DATE = "2023-09-01";
   public static final int TEST_NEEDS_GRADING = 1;
   public static final int SLEEP_DURATION = 1000; // 1 second.
   public static final String TEST_COURSE_NAME = "cst123-testing";
   public static final int TEST_ASSIGNMENT_ID = 27;

   @Autowired
   AssignmentRepository assignmentRepository;
   
   @Autowired
   CourseRepository courseRepository;

   @Test
   public void addCourseTest() throws Exception {
   
      // If test assignment exists delete it
      Assignment x = new Assignment();
      Course course = courseRepository.findByName(TEST_COURSE_NAME);
      x.setId(TEST_ASSIGNMENT_ID);
      x.setName(TEST_ASSIGNMENT_NAME);
      x.setDueDate(java.sql.Date.valueOf(TEST_DATE));
      x.setNeedsGrading(TEST_NEEDS_GRADING);
      x.setCourse(course);
      
      Boolean b = assignmentRepository.equals(x);
      
      if (b) assignmentRepository.delete(x);

      System.setProperty("webdriver.chrome.driver", CHROME_DRIVER_FILE_LOCATION);
      WebDriver driver = new ChromeDriver();

      driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

      // Run test
      try {            

         // Go to project URL
         driver.get(URL);
         Thread.sleep(SLEEP_DURATION);

         // Input assignment name
         driver.findElement(By.xpath("/html/body/div/div/div/form/label[1]/input")).clear();
         driver.findElement(By.xpath("/html/body/div/div/div/form/label[1]/input")).sendKeys(TEST_ASSIGNMENT_NAME);

         // Input course name
         driver.findElement(By.xpath("/html/body/div/div/div/form/label[2]/input")).clear();
         driver.findElement(By.xpath("/html/body/div/div/div/form/label[2]/input")).sendKeys(TEST_COURSE_NAME);  

         // Input due date for assignment
         driver.findElement(By.xpath("/html/body/div/div/div/form/label[3]/input")).clear();
         driver.findElement(By.xpath("/html/body/div/div/div/form/label[3]/input")).sendKeys(TEST_DATE);
         
         //Submit new assignment to database
         driver.findElement(By.xpath("/html/body/div/div/div/form/input")).click();
         Thread.sleep(SLEEP_DURATION);
         
         // Check if new assignment exists, if it does the test passes
         Boolean bool = assignmentRepository.existsById(TEST_ASSIGNMENT_ID);
         Boolean found = false;
         if(bool) found = true;
         assertTrue( found, "Assignment has been added");

      } catch (Exception ex) {
         throw ex;
      } finally {

         // Clean up database by deleting created assignment
         Assignment a = new Assignment();
         Course c = courseRepository.findByName(TEST_COURSE_NAME);
         a.setName(TEST_ASSIGNMENT_NAME);
         a.setDueDate(java.sql.Date.valueOf(TEST_DATE));
         a.setNeedsGrading(TEST_NEEDS_GRADING);
         a.setId(TEST_ASSIGNMENT_ID);
         a.setCourse(c);
         assignmentRepository.delete(a);
         
         // Close driver
         driver.quit();
      }

   }
}