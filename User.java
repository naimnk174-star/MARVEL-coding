// User.java
import java.util.ArrayList;
import java.util.Scanner;
import java.io.Console;

class User {
  private String username;
  private String password;
  private String role;

  public User() { };

  public User(String username, String password, String role) {
    this.username = username;
    this.password = password;
    this.role = role;
  }

  public String getRole() { return role; }

  // FIXED: Changed from '==' reference identity comparison to safe logical content check
  public boolean isAdmin() { return "ADMIN".equalsIgnoreCase(this.role); }
  public boolean isLecturer() { return "LECTURER".equalsIgnoreCase(this.role); }
  public boolean isStudent() { return "STUDENT".equalsIgnoreCase(this.role); }

  public boolean auth(String username, String password) {
    return (username.equals(this.username) && password.equals(this.password));
  }

  public static User login(ArrayList users) {
    Scanner scn = new Scanner(System.in);
    
    System.out.println(">>> SYSTEM LOGIN <<<");
    System.out.println("1. Student");
    System.out.println("2. Lecturer");
    System.out.println("3. Admin");
    System.out.println("0. Back to Main Menu");
    System.out.print("\nEnter role (0-3): ");
    
    int choice;
    try {
        choice = Integer.parseInt(scn.nextLine());
    } catch (Exception e) {
        return null;
    }
    
    String roleTag = "";
    if (choice == 1) { 
        roleTag = "STUDENT"; 
        System.out.println("\n-------------------------------------\n          STUDENT LOGIN\n-------------------------------------"); 
    } else if (choice == 2) { 
        roleTag = "LECTURER"; 
        System.out.println("\n-------------------------------------\n          LECTURER LOGIN\n-------------------------------------"); 
    } else if (choice == 3) { 
        roleTag = "ADMIN"; 
        System.out.println("\n-------------------------------------\n          ADMIN LOGIN\n-------------------------------------"); 
    } else {
        return null;
    }

    System.out.print("Enter Username: ");
    String username = scn.nextLine();

    System.out.print("Enter Password: ");
    String password = "";
    
    Console cons = System.console();
    if (cons != null) {
        // Securely hides input typing from display automatically 
        char[] passwd = cons.readPassword();
        password = new String(passwd);
    } else {
        // Fallback option so execution works normally inside IDE consoles
        password = scn.nextLine();
    }

    System.out.println("\nAuthenticating...");
    for (Object obj : users) {
      User user = (User) obj;
      if (user.auth(username, password) && user.getRole().equalsIgnoreCase(roleTag)) {
        System.out.println("✓ Login Successful!");
        System.out.printf("Welcome, %s!\n\n", user.getAcctInfo());
        return user;
      }
    }

    System.out.println("Error: Invalid credentials or mismatched dashboard role selection!\n");
    return null;
  }
  
  public static void pressEnterContinue() {
    Scanner scn = new Scanner(System.in);
    System.out.print("Press [Enter] to return to Dashboard...");
    scn.nextLine();
    System.out.println();
  }
  
  public String getAcctInfo() { return username; }
  public String toString() { return username + "\t" + password; }
}