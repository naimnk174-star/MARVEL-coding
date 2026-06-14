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

  public String getRole() {
    return role;
  }

  public boolean isAdmin() {
    return this.role == "ADMIN";
  }

  public boolean isLecturer() {
    return this.role == "LECTURER";
  }

  public boolean isStudent() {
    return this.role == "STUDENT";
  }

  public String toString() {
    return username + "\t" + password;
  }

  public String getAcctInfo() {
    return username;
  }

  public boolean auth(String username, String password) {
    //System.out.println(username.equals(this.username) && password.equals(this.password));
    return (username.equals(this.username) && password.equals(this.password));
  }

  public static User login(ArrayList<User> users) {
    Scanner scn = new Scanner(System.in);

    System.out.println(">>> SYSTEM LOGIN <<<");
    System.out.println("1. Student");
    System.out.println("2. Lecturer");
    System.out.println("3. Admin");
    System.out.println("0. Back to Main Menu");
    System.out.print("\nEnter role (0-3): ");

    int roleChoice = scn.nextInt();
    scn.nextLine(); // Clear buffer
    
    String targetRole = "";
    if (roleChoice == 1) {
        targetRole = "STUDENT";
        System.out.println("\n-------------------------------------\n        STUDENT LOGIN\n-------------------------------------");
    } else if (roleChoice == 2) {
        targetRole = "LECTURER";
        System.out.println("\n-------------------------------------\n        LECTURER LOGIN\n-------------------------------------");
    } else if (roleChoice == 3) {
        targetRole = "ADMIN";
        System.out.println("\n-------------------------------------\n        ADMIN LOGIN\n-------------------------------------");
    } else {
        return null;
    }

    System.out.print("Username: ");
    String username = scn.nextLine();
    System.out.print("Password: ");
    String password = scn.nextLine();

    Console console = System.console();
    if (console != null) {
        char[] passChars = console.readPassword();
        password = new String(passChars);
    } else {
        // Fallback if running inside IDE terminal where Console object is null
        password = scn.nextLine();
    }

    System.out.println("\nAuthenticating...");

    for (int i = 0; i < users.size(); i++) {
      User user = (User)users.get(i);
      if (user.auth(username, password)) {
        System.out.println("Authenticate: " + user.getAcctInfo());
        System.out.println();
        return user;
      }
    }

    System.out.println("Invalid username and password!\n");
    return null;
  }
  
  public static void pressEnterContinue() {
    Scanner scn = new Scanner(System.in);
    System.out.print("Press enter to contnue...");
    String enter = scn.nextLine();
    System.out.println();
  }
}