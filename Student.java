// Student.java
import java.util.ArrayList;

class Student extends User {
  private String name;
  private String matricNo;
  private ArrayList<CourseReg> crsRegList;

  public Student(String name, String matricNo, String username, String password) {
    super(username, password, "STUDENT");
    this.name = name;
    this.matricNo = matricNo;
    this.crsRegList = new ArrayList<CourseReg>();
  }

  public String getMatricNo() { return this.matricNo; } 
  public String getName() { return this.name; } 
  public ArrayList<CourseReg> getCrsRegList() { return this.crsRegList; } 

  public void registerCourse(CourseReg courseReg) { 
    this.crsRegList.add(courseReg);
  }

  public String toString() {
    return name + "\t" + matricNo + "\t" + super.toString();
  }

  @Override
  public String toCSV() {
    return getUsername() + "," + getPassword() + "," + getRole() + "," + name + "," + matricNo;
  }

  public String getInfo() { 
    return name + " - " + matricNo; 
  }
  
  public String getAcctInfo() { 
    return name + " - " + matricNo + " (Student)"; 
  }

  public void runTask(String menu) {
    System.out.println("\n-------------------------------------");
    if (menu.equals("View Registered Courses & Marks")) {
      System.out.println(">>> COURSE PERFORMANCE INQUIRY <<<");
      System.out.println("Retrieving data...\n");
      System.out.printf("%-10s %-25s %-6s %-6s\n", "CODE", "COURSE NAME", "MARK", "GRADE");
      System.out.println("---------- ------------------------- ------ ------");
      
      // DEBUG: Print the size of the list
      System.out.println("DEBUG: crsRegList size = " + crsRegList.size());
      
      if (crsRegList.isEmpty()) {
          System.out.println("  No courses registered yet.");
      } else {
          for (CourseReg cr : crsRegList) {
            System.out.printf("%-10s %-25s %-6d %-6s\n", 
                cr.getCourse().getCode(), 
                cr.getCourse().getName(), 
                cr.getMark().totalMark(), 
                cr.getMark().grade());
          }
      }
      System.out.println();
    } 
    else if (menu.equals("View Full Academic Transcript (CGPA)")) {
      System.out.println(">>> ACADEMIC TRANSCRIPT CALCULATOR <<<");
      double totalPointsEarned = 0;
      int totalCreditsAttempted = 0;
      
      if (crsRegList.isEmpty()) {
          System.out.println("  No courses registered yet.");
      } else {
          for (CourseReg cr : crsRegList) {
              int crd = cr.getCourse().getCredits();
              totalCreditsAttempted += crd;
              totalPointsEarned += (cr.getMark().points() * crd);
          }
          double cgpa = totalCreditsAttempted == 0 ? 0.0 : totalPointsEarned / totalCreditsAttempted;
          System.out.printf("Total Credits Earned: %d\n", totalCreditsAttempted);
          System.out.printf("Calculated Cumulative GPA: %.2f\n\n", cgpa);
      }
    }
    
    User.pressEnterContinue();
  }
}