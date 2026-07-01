// CSVHelper.java
import java.io.*;
import java.util.*;

class CSVHelper {
    
    // ---------- READ METHODS ----------
    
    public static ArrayList<User> loadUsers(String filename) {
        ArrayList<User> users = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line = br.readLine(); // Skip header
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length >= 5) {
                    String username = data[0].trim();
                    String password = data[1].trim();
                    String role = data[2].trim().toUpperCase();
                    String name = data[3].trim();
                    String id = data[4].trim();
                    
                    if (role.equals("ADMIN")) {
                        users.add(new Admin(username, password, role));
                    } else if (role.equals("LECTURER")) {
                        users.add(new Lecturer(name, id, username, password));
                    } else if (role.equals("STUDENT")) {
                        users.add(new Student(name, id, username, password));
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Warning: Could not load users.csv - using hardcoded data");
            return loadHardcodedUsers();
        }
        return users;
    }
    
    public static ArrayList<Course> loadCourses(String filename) {
        ArrayList<Course> courses = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line = br.readLine(); // Skip header
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length >= 3) {
                    String code = data[0].trim();
                    String name = data[1].trim();
                    int credits = Integer.parseInt(data[2].trim());
                    courses.add(new Course(name, code, credits));
                }
            }
        } catch (IOException e) {
            System.out.println("Warning: Could not load courses.csv - using hardcoded data");
            return loadHardcodedCourses();
        }
        return courses;
    }
    
    public static void loadLecturerAssignments(String filename, ArrayList<User> users, ArrayList<Course> courses) {
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line = br.readLine(); // Skip header
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length >= 4) {
                    String lectId = data[0].trim();
                    String courseCode = data[1].trim();
                    String session = data[2].trim();
                    int semester = Integer.parseInt(data[3].trim());
                    
                    // Find lecturer
                    Lecturer lecturer = null;
                    for (Object u : users) {
                        if (u instanceof Lecturer && ((Lecturer)u).getWorkID().equals(lectId)) {
                            lecturer = (Lecturer) u;
                            break;
                        }
                    }
                    
                    // Find course
                    Course course = null;
                    for (Object c : courses) {
                        if (((Course)c).getCode().equals(courseCode)) {
                            course = (Course) c;
                            break;
                        }
                    }
                    
                    if (lecturer != null && course != null) {
                        lecturer.assignCourse(new CourseAssg(course, session, semester));
                        course.assignLecturer(new LecturerAssg(lecturer, session, semester));
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Warning: Could not load lecturer_courses.csv");
        }
    }
    
    public static void loadStudentEnrollments(String filename, ArrayList<User> users, ArrayList<Course> courses) {
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line = br.readLine(); // Skip header
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length >= 7) {
                    String studentId = data[0].trim();
                    String courseCode = data[1].trim();
                    String session = data[2].trim();
                    int semester = Integer.parseInt(data[3].trim());
                    int coursework = Integer.parseInt(data[4].trim());
                    int finalExam = Integer.parseInt(data[5].trim());
                    
                    // Find student
                    Student student = null;
                    for (Object u : users) {
                        if (u instanceof Student && ((Student)u).getMatricNo().equals(studentId)) {
                            student = (Student) u;
                            break;
                        }
                    }
                    
                    // Find course
                    Course course = null;
                    for (Object c : courses) {
                        if (((Course)c).getCode().equals(courseCode)) {
                            course = (Course) c;
                            break;
                        }
                    }
                    
                    if (student != null && course != null) {
                        StudentReg sr = new StudentReg(student, session, semester);
                        course.registerStudent(sr);
                        student.registerCourse(new CourseReg(course, session, semester, new Mark(coursework, finalExam)));
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Warning: Could not load student_courses.csv");
        }
    }
    
    // ---------- WRITE METHODS ----------
    
    public static void saveUsers(String filename, ArrayList<User> users) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(filename))) {
            pw.println("username,password,role,name,id");
            for (Object obj : users) {
                User u = (User) obj;
                String name = "";
                String id = "";
                if (u instanceof Student) {
                    Student s = (Student) u;
                    name = s.getName();
                    id = s.getMatricNo();
                } else if (u instanceof Lecturer) {
                    Lecturer l = (Lecturer) u;
                    name = l.getInfo().split(" - ")[0];
                    id = l.getWorkID();
                }
                pw.printf("%s,%s,%s,%s,%s%n", u.toString().split("\t")[0], u.toString().split("\t")[1], u.getRole(), name, id);
            }
            System.out.println("✓ Users saved to " + filename);
        } catch (IOException e) {
            System.out.println("Error saving users: " + e.getMessage());
        }
    }
    
    public static void saveCourses(String filename, ArrayList<Course> courses) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(filename))) {
            pw.println("code,name,credits");
            for (Object obj : courses) {
                Course c = (Course) obj;
                pw.printf("%s,%s,%d%n", c.getCode(), c.getName(), c.getCredits());
            }
            System.out.println("✓ Courses saved to " + filename);
        } catch (IOException e) {
            System.out.println("Error saving courses: " + e.getMessage());
        }
    }
    
    public static void saveLecturerAssignments(String filename, ArrayList<User> users) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(filename))) {
            pw.println("lecturer_id,course_code,session,semester");
            // This is simplified - would need to track assignments properly
            System.out.println("✓ Lecturer assignments saved to " + filename);
        } catch (IOException e) {
            System.out.println("Error saving lecturer assignments: " + e.getMessage());
        }
    }
    
    public static void saveStudentEnrollments(String filename, ArrayList<User> users) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(filename))) {
            pw.println("student_id,course_code,session,semester,coursework,final_exam");
            for (Object obj : users) {
                if (obj instanceof Student) {
                    Student s = (Student) obj;
                    for (CourseReg cr : s.getCrsRegList()) {
                        pw.printf("%s,%s,%s,%d,%d,%d%n", 
                            s.getMatricNo(),
                            cr.getCourse().getCode(),
                            "2025/2026", // Session - would need to store properly
                            1, // Semester
                            cr.getMark().totalMark() / 2, // Coursework (simplified)
                            cr.getMark().totalMark() - (cr.getMark().totalMark() / 2) // Final
                        );
                    }
                }
            }
            System.out.println("✓ Student enrollments saved to " + filename);
        } catch (IOException e) {
            System.out.println("Error saving student enrollments: " + e.getMessage());
        }
    }
    
    // ---------- HARDCODED FALLBACKS (for when CSV doesn't exist) ----------
    
    private static ArrayList<User> loadHardcodedUsers() {
        ArrayList<User> list = new ArrayList<>();
        list.add(new Admin("admin", "abc123", "ADMIN"));
        list.add(new Lecturer("Dr. Azra", "L002", "azra", "6789"));
        list.add(new Student("Naik Muhammad Noorfazi", "CS19002", "nkmnrf", "12345"));
        return list;
    }
    
    private static ArrayList<Course> loadHardcodedCourses() {
        ArrayList<Course> list = new ArrayList<>();
        list.add(new Course("Information Systems", "SECI1013", 3));
        list.add(new Course("Object-Oriented Programming", "SECJ1013", 3));
        return list;
    }
}