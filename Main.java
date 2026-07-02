// Main.java - SIMPLIFIED FORCED VERSION
import java.util.Scanner;
import java.util.ArrayList;

public class Main {
  private static final String USERS_FILE = "users.csv";
  private static final String COURSES_FILE = "courses.csv";
  private static final String LECTURER_COURSES_FILE = "lecturer_courses.csv";
  private static final String STUDENT_COURSES_FILE = "student_courses.csv";

  public static void main(String[] args) {
    // 1. ALWAYS create fresh data
    ArrayList users = new ArrayList();
    ArrayList courses = new ArrayList();
    
    // Create Admin
    users.add(new Admin("admin", "abc123", "ADMIN"));
    
    // Create Lecturer
    Lecturer lecturer = new Lecturer("Dr. Azra", "L002", "azra", "6789");
    users.add(lecturer);
    
    // Create Student
    Student student = new Student("Naik Muhammad Noorfazi", "CS19002", "nkmnrf", "12345");
    users.add(student);
    
    // Create Courses
    Course c1 = new Course("Information Systems", "SECI1013", 3);
    Course c2 = new Course("Object-Oriented Programming", "SECJ1013", 3);
    courses.add(c1);
    courses.add(c2);
    
    // 2. ADD MOCK DATA DIRECTLY TO THE STUDENT OBJECT
    System.out.println("Adding mock data...");
    
    // Add enrollments directly to the student
    StudentReg sr1 = new StudentReg(student, "2025/2026", 1);
    c1.registerStudent(sr1);
    student.registerCourse(new CourseReg(c1, "2025/2026", 1, new Mark(42, 43)));
    System.out.println("  ✓ Enrolled " + student.getName() + " in " + c1.getCode() + " (85%)");
    
    StudentReg sr2 = new StudentReg(student, "2025/2026", 1);
    c2.registerStudent(sr2);
    student.registerCourse(new CourseReg(c2, "2025/2026", 1, new Mark(38, 38)));
    System.out.println("  ✓ Enrolled " + student.getName() + " in " + c2.getCode() + " (76%)");
    
    // Add lecturer assignments
    lecturer.assignCourse(new CourseAssg(c1, "2025/2026", 1));
    c1.assignLecturer(new LecturerAssg(lecturer, "2025/2026", 1));
    System.out.println("  ✓ Assigned " + lecturer.getInfo() + " to " + c1.getCode());
    
    lecturer.assignCourse(new CourseAssg(c2, "2025/2026", 1));
    c2.assignLecturer(new LecturerAssg(lecturer, "2025/2026", 1));
    System.out.println("  ✓ Assigned " + lecturer.getInfo() + " to " + c2.getCode());
    
    System.out.println("✓ Mock data added successfully!\n");
    
    // 3. DEBUG - Verify data was added
    System.out.println("--- DEBUG: Checking student enrollments ---");
    System.out.println("Student: " + student.getName());
    System.out.println("Enrollments: " + student.getCrsRegList().size());
    for (CourseReg cr : student.getCrsRegList()) {
        System.out.println("  - " + cr.getCourse().getCode() + ": " + cr.getMark().totalMark());
    }
    System.out.println("--- END DEBUG ---\n");

    System.out.println("=====================================");
    System.out.println("Course Mark & Grade Management System");
    System.out.println("=====================================");
    System.out.println("✓ Data initialized with " + users.size() + " users");
    System.out.println("✓ Data initialized with " + courses.size() + " courses");
    System.out.println("=====================================\n");

    String[] startMenus =  {"Login", "Exit"};
    String[] adminMenus =  {"List Courses", "Course Info", "Logout"};
    String[] lectMenus =   {"View Assigned Course Rosters", "Update Student Marks", "Logout"};
    String[] studMenus =   {"View Registered Courses & Marks", "View Full Academic Transcript (CGPA)", "Logout"};

    User currentUser = null;
    boolean exit = false;
    
    while (!exit) {
      if (currentUser == null) {
        String baseChoice = chooseMenu(startMenus, "WELCOME MENU");
        if (baseChoice.equals("Login")) {
          currentUser = User.login(users);
        } else {
          saveAllData(users, courses);
          System.out.println("Exiting system...\nGoodbye!");
          exit = true;
        }
      } else {
        if (currentUser.isAdmin()) {
          String menu = chooseMenu(adminMenus, "ADMIN DASHBOARD");
          if (menu.equals("Logout")) {
            currentUser = null;
            saveAllData(users, courses);
          } else {
            ((Admin)currentUser).runTask(menu, users, courses);
          }
        } 
        else if (currentUser.isLecturer()) {
          String menu = chooseMenu(lectMenus, "LECTURER DASHBOARD");
          if (menu.equals("Logout")) {
            currentUser = null;
            saveAllData(users, courses);
          } else {
            ((Lecturer)currentUser).runTask(menu, users);
          }
        } 
        else if (currentUser.isStudent()) {
          String menu = chooseMenu(studMenus, "STUDENT DASHBOARD");
          if (menu.equals("Logout")) {
            currentUser = null;
            saveAllData(users, courses);
          } else {
            ((Student)currentUser).runTask(menu);
          }
        }
      }
    }
  }

  // Save all data to CSV files
  private static void saveAllData(ArrayList users, ArrayList courses) {
    System.out.println("\n--- Saving data to CSV files ---");
    CSVHelper.saveUsers(USERS_FILE, users);
    CSVHelper.saveCourses(COURSES_FILE, courses);
    CSVHelper.saveStudentEnrollments(STUDENT_COURSES_FILE, users);
    CSVHelper.saveLecturerAssignments(LECTURER_COURSES_FILE, users);
    System.out.println("--- Data saved successfully! ---\n");
  }

  // Display menu and get user choice
  public static String chooseMenu(String[] menus, String title) {
    System.out.println("=====================================");
    System.out.println("           " + title);
    System.out.println("=====================================");
    
    for (int i = 0; i < menus.length - 1; i++) {
      System.out.println((i+1) + ". " + menus[i]);
    }
    System.out.println("0. " + menus[menus.length - 1]);
    System.out.println("-------------------------------------");

    Scanner scn = new Scanner(System.in);
    int choice = -1;
    while (choice < 0 || choice >= menus.length) {
      System.out.print("ENTER CHOICE: ");
      try {
          int input = Integer.parseInt(scn.nextLine());
          if (input == 0) {
              choice = menus.length - 1; 
          } else if (input > 0 && input < menus.length) {
              choice = input - 1;
          }
      } catch (Exception e) {
          // ignore parsing errors and prompt again
      }
    }
    System.out.println();
    return menus[choice];
  }
}