// Course.java
import java.util.ArrayList;

class Course {
  private String name;
  private String code;
  private int credits;
  private ArrayList<LecturerAssg> lectAssgList;
  private ArrayList<StudentReg> studRegList; // ADDED

  public Course(String name, String code, int credits) {
    this.name = name;
    this.code = code;
    this.credits = credits;
    this.lectAssgList = new ArrayList<LecturerAssg>();
    this.studRegList = new ArrayList<StudentReg>(); // ADDED
  }
  
  public String getCode() { return this.code; }
  public String getName() { return this.name; } 
  public int getCredits() { return this.credits; }
  public ArrayList<StudentReg> getStudRegList() { return this.studRegList; } // ADDED

  public String toString() {
    return this.code + "-" + this.name;
  }
  
  public void assignLecturer(LecturerAssg lectAssg) {
    this.lectAssgList.add(lectAssg);
  }

  public void registerStudent(StudentReg studentReg) { // ADDED
    this.studRegList.add(studentReg);
  }
  
  public void listLecturer() {
    int num = 1;
    for (LecturerAssg lectAssg : lectAssgList) {
      System.out.printf("%d. %s\n", num, lectAssg.getLecturer().getInfo());
      num++;
    }
  }
}