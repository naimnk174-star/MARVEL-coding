// Admin.java
import java.util.ArrayList;
import java.util.Scanner;

class Admin extends User {
  public Admin(String username, String password, String role) {
    super(username, password, role);
  }
  
  public String getAcctInfo() { return "Admin (System Administrator)"; }

  public void runTask(String menu, ArrayList users, ArrayList courses) {
    System.out.println("\n-------------------------------------");
    if (menu.equals("Add New Course Offering")) {
       System.out.println("Task execution placeholder for: Add New Course Offering");
    } 
    else if (menu.equals("List Courses")) {
      for (Object crs : courses) { System.out.println((Course)crs); }
    } 
    else if (menu.equals("Assign Lecturer to Course")) {
       System.out.println("Task execution placeholder for: Assign Lecturer");
    }
    else if (menu.equals("Manage Student Enrollments") || menu.equals("Course Info")) {
      Scanner scn = new Scanner(System.in);
      System.out.print("Course Code: ");
      String courseCode = scn.nextLine().trim();
      
      Course crs = (Course)courses.stream()
                     .filter(c -> ((Course)c).getCode().equalsIgnoreCase(courseCode))
                     .findFirst().orElse(null);
      if (crs == null) {
          System.out.println("Error: Course selection code does not exist.");
          User.pressEnterContinue();
          return;
      }
      System.out.println(crs + "\n");
      System.out.println("Lecturers:");
      crs.listLecturer();
      System.out.println();
      
      System.out.println("Students:");
      int num = 1;
      if (crs.getStudRegList().isEmpty()) {
          System.out.println("  (No enrolled students standard record found)");
      } else {
          for (StudentReg sr : crs.getStudRegList()) {
              System.out.printf("%d. %s\n", num, sr.getStudent().getInfo());
              num++;
          }
      }
      System.out.println();
    }
    User.pressEnterContinue();
  }
}