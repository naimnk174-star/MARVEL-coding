// Main.java
import java.util.Scanner;
import java.util.ArrayList;

public class Main {
  public static void main(String[] args) {
    // 1. Manually initialize the user and course data arrays in memory
    ArrayList users = loadHardcodedUsers();
    ArrayList courses = loadHardcodedCourses();
    
    // 2. Establish relationships in memory (Assign Lecturers to Courses)
    assignHardcodedCourses(users, courses);
    
    // 3. Establish student enrollment records and assign mock marks
    registerStudentMockMarks(users, courses);

    System.out.println("Course Mark & Grade Management System");
    System.out.println("=====================================");

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
          System.out.println("Exiting system...\nGoodbye!");
          exit = true;
        }
      } else {
        if (currentUser.isAdmin()) {
          String menu = chooseMenu(adminMenus, "ADMIN DASHBOARD");
          if (menu.equals("Logout")) currentUser = null;
          else ((Admin)currentUser).runTask(menu, users, courses);
        } 
        else if (currentUser.isLecturer()) {
          String menu = chooseMenu(lectMenus, "LECTURER DASHBOARD");
          if (menu.equals("Logout")) currentUser = null;
          else ((Lecturer)currentUser).runTask(menu, users);
        } 
        else if (currentUser.isStudent()) {
          String menu = chooseMenu(studMenus, "STUDENT DASHBOARD");
          if (menu.equals("Logout")) currentUser = null;
          else ((Student)currentUser).runTask(menu);
        }
      }
    }
  }

  // --- HARDCODED DATA SEEDING METHODS (REPLACES THE CSV FUNCTIONS) ---

  public static ArrayList loadHardcodedUsers() {
    ArrayList list = new ArrayList();
    
    // Default Admin
    list.add(new Admin("admin", "abc123", "ADMIN"));
    
    // Lecturer Account requested: username 'azra' and password '6789'
    list.add(new Lecturer("Dr. Azra", "L002", "azra", "6789"));
    
    // Student Account requested: username 'nkmnrf' and password '12345'
    list.add(new Student("Naik Muhammad Noorfazi", "CS19002", "nkmnrf", "12345"));
    
    return list;
  }
  
  public static ArrayList loadHardcodedCourses() {
    ArrayList list = new ArrayList();
    
    // Creating manual system courses directly (Name, Code, Credits)
    list.add(new Course("Information Systems", "SECI1013", 3));
    list.add(new Course("Object-Oriented Programming", "SECJ1013", 3));
    
    return list;
  }
  
  public static void assignHardcodedCourses(ArrayList users, ArrayList courses) {
    // Locate the hardcoded objects we created above
    Course c1 = (Course) courses.stream().filter(c -> ((Course)c).getCode().equals("SECI1013")).findFirst().orElse(null);
    Course c2 = (Course) courses.stream().filter(c -> ((Course)c).getCode().equals("SECJ1013")).findFirst().orElse(null);
    Lecturer lect = (Lecturer) users.stream().filter(u -> (u instanceof Lecturer) && ((Lecturer)u).getWorkID().equals("L002")).findFirst().orElse(null);
    
    // Assign relationship linkages in memory
    if (lect != null) {
      if (c1 != null) {
        lect.assignCourse(new CourseAssg(c1, "2025/2026", 1));
        c1.assignLecturer(new LecturerAssg(lect, "2025/2026", 1));
      }
      if (c2 != null) {
        lect.assignCourse(new CourseAssg(c2, "2025/2026", 1));
        c2.assignLecturer(new LecturerAssg(lect, "2025/2026", 1));
      }
    }
  }

  public static void registerStudentMockMarks(ArrayList users, ArrayList courses) {
    for (Object u : users) {
      if (u instanceof Student) {
        Student s = (Student) u;
        if (s.getMatricNo().equalsIgnoreCase("CS19002")) {
          Course c1 = (Course) courses.stream().filter(c -> ((Course)c).getCode().equals("SECI1013")).findFirst().orElse(null);
          Course c2 = (Course) courses.stream().filter(c -> ((Course)c).getCode().equals("SECJ1013")).findFirst().orElse(null);
          
          if (c1 != null) {
              StudentReg sr1 = new StudentReg(s, "2025/2026", 1);
              c1.registerStudent(sr1);
              s.registerCourse(new CourseReg(c1, "2025/2026", 1, new Mark(42, 43))); // 85 -> Grade: A
          }
          if (c2 != null) {
              StudentReg sr2 = new StudentReg(s, "2025/2026", 1);
              c2.registerStudent(sr2);
              s.registerCourse(new CourseReg(c2, "2025/2026", 1, new Mark(38, 38))); // 76 -> Grade: B+
          }
        }
      }
    }
  }

  // --- INTERFACE UTILITIES ---

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