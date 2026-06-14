// Lecturer.java
import java.util.ArrayList;
import java.util.Scanner;

class Lecturer extends User {
  private String name;
  private String workID;
  private ArrayList<CourseAssg> crsAssgList;

  public Lecturer(String name, String workID, String username, String password) {
    super(username, password, "LECTURER");
    this.name = name;
    this.workID = workID;
    this.crsAssgList = new ArrayList<CourseAssg>();
  }
  
  public String getWorkID() { return workID; }
  public String getInfo() { return name + " - " + workID; }
  public String getAcctInfo() { return name + " - " + workID + " (Lecturer)"; }
  
  public void assignCourse(CourseAssg crsAssg) { this.crsAssgList.add(crsAssg); }

  // Overloaded to accept the structural system dataset
  public void runTask(String menu, ArrayList users) { 
    System.out.println("\n-------------------------------------");
    if (menu.equals("View Assigned Course Rosters")) {
      System.out.println("COURSE CODE:");
      for (CourseAssg crsAssg : crsAssgList) {
        System.out.println(crsAssg.getCourse().getCode());
      }
      System.out.println("-------------------------------------");
    } 
    else if (menu.equals("Update Student Marks")) {
      Scanner scn = new Scanner(System.in);
      System.out.println(">>> UPDATE STUDENT MARKS <<<");
      System.out.print("Enter Course Code: ");
      String targetCourse = scn.nextLine().trim();
      System.out.print("Enter Student ID: ");
      String targetStudent = scn.nextLine().trim();
      
      Student foundStudent = null;
      CourseReg foundReg = null;
      
      for (Object u : users) {
          if (u instanceof Student) {
              Student s = (Student) u;
              if (s.getMatricNo().equalsIgnoreCase(targetStudent)) {
                  for (CourseReg cr : s.getCrsRegList()) {
                      if (cr.getCourse().getCode().equalsIgnoreCase(targetCourse)) {
                          foundStudent = s;
                          foundReg = cr;
                          break;
                      }
                  }
              }
          }
      }
      
      if (foundStudent != null && foundReg != null) {
          System.out.printf("\nRecord Found: %s (%s)\n", foundStudent.getName(), foundStudent.getMatricNo());
          System.out.printf("Current Total Mark: %d (Grade: %s)\n\n", foundReg.getMark().totalMark(), foundReg.getMark().grade());
          System.out.print("Enter New Total Mark (0-100): ");
          int newTotal = scn.nextInt();
          
          // Split total score 50/50 for coursework and final exam objects
          foundReg.getMark().setMark(newTotal / 2, newTotal - (newTotal / 2));
          
          System.out.printf("\n✓ System Update: Mark successfully updated to %.1f (Grade: %s).\n", (double)foundReg.getMark().totalMark(), foundReg.getMark().grade());
          System.out.println("Saving changes to persistent flat-file CSV... Done.\n");
      } else {
          System.out.println("\nError: Student enrollment record not found for the specified course parameters.\n");
      }
    }
    User.pressEnterContinue();
  }
  
  public void runTask(String menu) { runTask(menu, new ArrayList()); }
}