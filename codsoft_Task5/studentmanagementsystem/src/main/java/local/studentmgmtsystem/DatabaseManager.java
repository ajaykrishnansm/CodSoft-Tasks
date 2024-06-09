package local.studentmgmtsystem;

import java.io.File;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
public class DatabaseManager {

	private static final String DB_URL = "jdbc:sqlite:" + System.getProperty("db.file", "students.db");

    public static Connection connect() {
        Connection conn = null;
        try {
            String workingDirectory = System.getProperty("user.dir");
            System.out.println("Current working directory: " + workingDirectory);
            File dbFile = new File(System.getProperty("db.file", "students.db"));
            System.out.println("Absolute path of students.db: " + dbFile.getAbsolutePath());
            if (dbFile.exists()) {
                System.out.println("students.db file exists at the specified location.");
            } else {
                System.out.println("students.db file does NOT exist at the specified location.");
            }
            conn = DriverManager.getConnection(DB_URL);
            System.out.println("Connection to SQLite has been established.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }
    public void createTables() {
        String studentTable = "CREATE TABLE IF NOT EXISTS students (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "name TEXT NOT NULL," +
                "roll_number TEXT UNIQUE NOT NULL," +
                "grade TEXT NOT NULL," +
                "age INTEGER CHECK (age > 0)," +
                "email TEXT CHECK (email LIKE '%_@_%._%')," +
                "address TEXT," +
                "phone_number TEXT)";
        try (Connection conn = connect();
             Statement stmt = conn.createStatement()) {
            stmt.execute(studentTable);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    public void insertStudent(Student student) {
        String sql = "INSERT INTO students(name, roll_number, grade, age, email, address, phone_number) VALUES(?,?,?,?,?,?,?)";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, student.getName());
            pstmt.setString(2, student.getRollNumber());
            pstmt.setString(3, student.getGrade());
            pstmt.setInt(4, student.getAge());
            pstmt.setString(5, student.getEmail());
            pstmt.setString(6, student.getAddress());
            pstmt.setString(7, student.getPhoneNumber());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    public void updateStudent(String rollNumber, Student newStudent) {
        String sql = "UPDATE students SET name=?, grade=?, age=?, email=?, address=?, phone_number=? WHERE roll_number=?";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, newStudent.getName());
            pstmt.setString(2, newStudent.getGrade());
            pstmt.setInt(3, newStudent.getAge());
            pstmt.setString(4, newStudent.getEmail());
            pstmt.setString(5, newStudent.getAddress());
            pstmt.setString(6, newStudent.getPhoneNumber());
            pstmt.setString(7, rollNumber);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    public int deleteStudent(String rollNumber) {
        String sql = "DELETE FROM students WHERE roll_number=?";
        int rowsAffected = 0;
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, rollNumber);
            rowsAffected = pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return rowsAffected;
    }
    public Student getStudentByRollNumber(String rollNumber) {
        String sql = "SELECT * FROM students WHERE roll_number=?";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, rollNumber);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                String name = rs.getString("name");
                String grade = rs.getString("grade");
                int age = rs.getInt("age");
                String email = rs.getString("email");
                String address = rs.getString("address");
                String phoneNumber = rs.getString("phone_number");
                return new Student(name, rollNumber, grade, age, email, address, phoneNumber);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public List<Student> getAllStudents() {
        List<Student> students = new ArrayList<>();
        String sql =  "SELECT * FROM students";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
             ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                String name = rs.getString("name");
                String rollNumber = rs.getString("roll_number");
                String grade = rs.getString("grade");
                int age = rs.getInt("age");
                String email = rs.getString("email");
                String address = rs.getString("address");
                String phoneNumber = rs.getString("phone_number");
                students.add(new Student(name, rollNumber, grade, age, email, address, phoneNumber));
            }
            
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return students;
    }
    public List<Student> searchStudent(String keyword) {
        List<Student> students = new ArrayList<>();
        String sql = "SELECT * FROM students WHERE name LIKE ? OR grade LIKE ? OR email LIKE ? OR address LIKE ? OR phone_number LIKE ?";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            for (int i = 1; i <= 5; i++) {
                pstmt.setString(i, "%" + keyword + "%");
            }
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                String name = rs.getString("name");
                String rollNumber = rs.getString("roll_number");
                String grade = rs.getString("grade");
                int age = rs.getInt("age");
                String email = rs.getString("email");
                String address = rs.getString("address");
                String phoneNumber = rs.getString("phone_number");
                students.add(new Student(name, rollNumber, grade, age, email, address, phoneNumber));
            }
           
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return students;
    }
    public boolean doesRollNumberExist(String rollNumber) {
        String sql = "SELECT COUNT(*) FROM students WHERE roll_number = ?";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, rollNumber);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
           System.out.println(e.getMessage());
        }
        return false;
    }
    public boolean doesPhoneNumberExist(String phoneNumber) {
        String sql = "SELECT COUNT(*) FROM students WHERE phone_number = ?";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, phoneNumber);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.out.println("Checking phone number existence failed:"+ e.getMessage());
        }
        return false;
    }

    public boolean doesEmailExist(String email) {
        String sql = "SELECT COUNT(*) FROM students WHERE email = ?";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, email);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.out.println("Checking email existence failed: "+e.getMessage());
        }
        return false;
    }
}