// CSVHelper.java - COMPLETE VERSION WITH FIXED SAVE
import java.io.*;
import java.util.*;

class CSVHelper {
    
    // ---------- READ METHODS ----------
    
    public static ArrayList<User> loadUsers(String filename) {
        ArrayList<User> users = new ArrayList<>();
        File file = new File(filename);
        
        if (!file.exists()) {
            System.out.println("Warning: " + filename + " not found - using hardcoded users");
            return loadHardcodedUsers();
        }
        
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line = br.readLine(); // Skip header
            System.out.println("Reading users from " + filename + "...");
            
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                
                String[] data = line.split(",");
                if (data.length >= 5) {
                    String username = data[0].trim();
                    String password = data[1].trim();
                    String role = data[2].trim().toUpperCase();
                    String name = data[3].trim();
                    String id = data[4].trim();
                    
                    System.out.println("  Loaded: " + username + " (" + role + ")");
                    
                    if (role.equals("ADMIN")) {
                        users.add(new Admin(username, password, role));
                    } else if (role.equals("LECTURER")) {
                        users.add(new Lecturer(name, id, username, password));
                    } else if (role.equals("STUDENT")) {
                        users.add(new Student(name, id, username, password));
                    }
                }
            }
            System.out.println("✓ Loaded " + users.size() + " users from " + filename);
        } catch (IOException e) {
            System.out.println("Error reading " + filename + ": " + e.getMessage());
            return loadHardcodedUsers();
        }
        return users;
    }
    
    public static ArrayList<Course> loadCourses(String filename) {
        ArrayList<Course> courses = new ArrayList<>();
        File file = new File(filename);
        
        if (!file.exists()) {
            System.out.println("Warning: " + filename + " not found - using hardcoded courses");
            return loadHardcodedCourses();
        }
        
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line = br.readLine(); // Skip header
            System.out.println("Reading courses from " + filename + "...");
            
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                
                String[] data = line.split(",");
                if (data.length >= 3) {
                    String code = data[0].trim();
                    String name = data[1].trim();
                    int credits = Integer.parseInt(data[2].trim());
                    
                    System.out.println("  Loaded: " + code + " - " + name);
                    courses.add(new Course(name, code, credits));
                }
            }
            System.out.println("✓ Loaded " + courses.size() + " courses from " + filename);
        } catch (IOException e) {
            System.out.println("Error reading " + filename + ": " + e.getMessage());
            return loadHardcodedCourses();
        }
        return courses;
    }
    
    public static void loadLecturerAssignments(String filename, ArrayList<User> users, ArrayList<Course> courses) {
        File file = new File(filename);
        
        if (!file.exists()) {
            System.out.println("Warning: " + filename + " not found");
            return;
        }
        
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line = br.readLine(); // Skip header
            System.out.println("Reading lecturer assignments from " + filename + "...");
            int count = 0;
            
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                
                String[] data = line.split(",");
                if (data.length >= 4) {
                    String lectId = data[0].trim();
                    String courseCode = data[1].trim();
                    String session = data[2].trim();
                    int semester = Integer.parseInt(data[3].trim());
                    
                    Lecturer lecturer = null;
                    for (Object u : users) {
                        if (u instanceof Lecturer && ((Lecturer)u).getWorkID().equals(lectId)) {
                            lecturer = (Lecturer) u;
                            break;
                        }
                    }
                    
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
                        count++;
                        System.out.println("  Assigned: " + lectId + " -> " + courseCode);
                    }
                }
            }
            System.out.println("✓ Loaded " + count + " lecturer assignments from " + filename);
        } catch (IOException e) {
            System.out.println("Error reading " + filename + ": " + e.getMessage());
        }
    }
    
    public static void loadStudentEnrollments(String filename, ArrayList<User> users, ArrayList<Course> courses) {
        File file = new File(filename);
        
        if (!file.exists()) {
            System.out.println("Warning: " + filename + " not found");
            return;
        }
        
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line = br.readLine(); // Skip header
            System.out.println("Reading student enrollments from " + filename + "...");
            int count = 0;
            
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                
                String[] data = line.split(",");
                if (data.length >= 7) {
                    String studentId = data[0].trim();
                    String courseCode = data[1].trim();
                    String session = data[2].trim();
                    int semester = Integer.parseInt(data[3].trim());
                    int coursework = Integer.parseInt(data[4].trim());
                    int finalExam = Integer.parseInt(data[5].trim());
                    
                    Student student = null;
                    for (Object u : users) {
                        if (u instanceof Student && ((Student)u).getMatricNo().equals(studentId)) {
                            student = (Student) u;
                            break;
                        }
                    }
                    
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
                        count++;
                        System.out.println("  Enrolled: " + studentId + " -> " + courseCode + 
                                         " (CW:" + coursework + ", FE:" + finalExam + ")");
                    }
                }
            }
            System.out.println("✓ Loaded " + count + " student enrollments from " + filename);
        } catch (IOException e) {
            System.out.println("Error reading " + filename + ": " + e.getMessage());
        }
    }
    
    // ---------- WRITE METHODS ----------
    
    public static void saveUsers(String filename, ArrayList<User> users) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(filename))) {
            pw.println("username,password,role,name,id");
            int count = 0;
            for (Object obj : users) {
                User u = (User) obj;
                pw.println(u.toCSV());
                count++;
            }
            System.out.println("✓ Saved " + count + " users to " + filename);
        } catch (IOException e) {
            System.out.println("Error saving users: " + e.getMessage());
        }
    }
    
    public static void saveCourses(String filename, ArrayList<Course> courses) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(filename))) {
            pw.println("code,name,credits");
            int count = 0;
            for (Object obj : courses) {
                Course c = (Course) obj;
                pw.printf("%s,%s,%d%n", c.getCode(), c.getName(), c.getCredits());
                count++;
            }
            System.out.println("✓ Saved " + count + " courses to " + filename);
        } catch (IOException e) {
            System.out.println("Error saving courses: " + e.getMessage());
        }
    }
    
    public static void saveStudentEnrollments(String filename, ArrayList<User> users) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(filename))) {
            pw.println("student_id,course_code,session,semester,coursework,final_exam");
            int count = 0;
            
            for (Object obj : users) {
                if (obj instanceof Student) {
                    Student s = (Student) obj;
                    for (CourseReg cr : s.getCrsRegList()) {
                        int total = cr.getMark().totalMark();
                        int coursework = total / 2;
                        int finalExam = total - coursework;
                        
                        pw.printf("%s,%s,%s,%d,%d,%d%n", 
                            s.getMatricNo(),
                            cr.getCourse().getCode(),
                            "2025/2026",
                            1,
                            coursework,
                            finalExam
                        );
                        count++;
                    }
                }
            }
            System.out.println("✓ Saved " + count + " student enrollments to " + filename);
        } catch (IOException e) {
            System.out.println("Error saving student enrollments: " + e.getMessage());
        }
    }
    
    // ---------- FIXED: SAVE LECTURER ASSIGNMENTS ----------
    public static void saveLecturerAssignments(String filename, ArrayList<User> users) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(filename))) {
            pw.println("lecturer_id,course_code,session,semester");
            int count = 0;
            
            for (Object obj : users) {
                if (obj instanceof Lecturer) {
                    Lecturer l = (Lecturer) obj;
                    
                    // DEBUG: Print to console so we can see what's happening
                    System.out.println("  Saving assignments for lecturer: " + l.getInfo());
                    
                    // Loop through all course assignments
                    for (CourseAssg assg : l.getCrsAssgList()) {
                        pw.printf("%s,%s,%s,%d%n", 
                            l.getWorkID(),
                            assg.getCourse().getCode(),
                            "2025/2026",
                            1
                        );
                        count++;
                        System.out.println("    - Assigned to: " + assg.getCourse().getCode());
                    }
                }
            }
            System.out.println("✓ Saved " + count + " lecturer assignments to " + filename);
        } catch (IOException e) {
            System.out.println("Error saving lecturer assignments: " + e.getMessage());
        }
    }
    
    // ---------- HARDCODED FALLBACKS ----------
    
    private static ArrayList<User> loadHardcodedUsers() {
        System.out.println("Using hardcoded users...");
        ArrayList<User> list = new ArrayList<>();
        list.add(new Admin("admin", "abc123", "ADMIN"));
        list.add(new Lecturer("Dr. Azra", "L002", "azra", "6789"));
        list.add(new Student("Naik Muhammad Noorfazi", "CS19002", "nkmnrf", "12345"));
        return list;
    }
    
    private static ArrayList<Course> loadHardcodedCourses() {
        System.out.println("Using hardcoded courses...");
        ArrayList<Course> list = new ArrayList<>();
        list.add(new Course("Information Systems", "SECI1013", 3));
        list.add(new Course("Object-Oriented Programming", "SECJ1013", 3));
        return list;
    }
}