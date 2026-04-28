package StudentManagementSystem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Student {
    private Connection connection;

    public Student(Connection connection) {
        this.connection = connection;
    }

    public String addStudent(String name, int age, String gender, int courseId, String section) {
        try {
            String query = "INSERT INTO students(name,age,gender,course_id,section) VALUES(?,?,?,?,?)";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, name);
            ps.setInt(2, age);
            ps.setString(3, gender);
            ps.setInt(4, courseId);
            ps.setString(5, section);
            int rows = ps.executeUpdate();
            return rows > 0 ? "<p style='color:green'>Student Added Successfully!</p>"
                    : "<p style='color:red'>Failed to add student.</p>";
        } catch (SQLException e) {
            return "<p style='color:red'>Error: " + e.getMessage() + "</p>";
        }
    }

    public String viewStudentById(int studentId) {
        try {
            String query = "SELECT student_id, name, age, gender, course_id, section FROM students WHERE student_id = ?";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1, studentId);
            ResultSet rs = ps.executeQuery();
            StringBuilder sb = new StringBuilder();
            sb.append("<table border='1' cellpadding='6'><tr><th>StudentID</th><th>Name</th><th>Age</th><th>Gender</th><th>CourseID</th><th>Section</th></tr>");
            if (rs.next()) {
                sb.append("<tr><td>").append(rs.getInt("student_id")).append("</td>")
                        .append("<td>").append(rs.getString("name")).append("</td>")
                        .append("<td>").append(rs.getInt("age")).append("</td>")
                        .append("<td>").append(rs.getString("gender")).append("</td>")
                        .append("<td>").append(rs.getInt("course_id")).append("</td>")
                        .append("<td>").append(rs.getString("section")).append("</td></tr>");
            } else {
                sb.append("<tr><td colspan='6'>Student not found!</td></tr>");
            }
            sb.append("</table>");
            return sb.toString();
        } catch (SQLException e) {
            return "<p style='color:red'>Error: " + e.getMessage() + "</p>";
        }
    }

    public String viewStudentByCourseId(int courseId) {
        try {
            String query = "SELECT * FROM students WHERE course_id = ?";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1, courseId);
            ResultSet rs = ps.executeQuery();
            StringBuilder sb = new StringBuilder();
            sb.append("<table border='1' cellpadding='6'><tr><th>StudentID</th><th>Name</th><th>Age</th><th>Gender</th><th>CourseID</th><th>Section</th></tr>");
            boolean found = false;
            while (rs.next()) {
                found = true;
                sb.append("<tr><td>").append(rs.getInt("student_id")).append("</td>")
                        .append("<td>").append(rs.getString("name")).append("</td>")
                        .append("<td>").append(rs.getInt("age")).append("</td>")
                        .append("<td>").append(rs.getString("gender")).append("</td>")
                        .append("<td>").append(rs.getInt("course_id")).append("</td>")
                        .append("<td>").append(rs.getString("section")).append("</td></tr>");
            }
            if (!found) sb.append("<tr><td colspan='6'>No students found!</td></tr>");
            sb.append("</table>");
            return sb.toString();
        } catch (SQLException e) {
            return "<p style='color:red'>Error: " + e.getMessage() + "</p>";
        }
    }

    public String viewAllStudents() {
        try {
            String query = "SELECT * FROM students";
            PreparedStatement ps = connection.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            StringBuilder sb = new StringBuilder();
            sb.append("<table border='1' cellpadding='6'><tr><th>StudentID</th><th>Name</th><th>Age</th><th>Gender</th><th>CourseID</th><th>Section</th></tr>");
            while (rs.next()) {
                sb.append("<tr><td>").append(rs.getInt("student_id")).append("</td>")
                        .append("<td>").append(rs.getString("name")).append("</td>")
                        .append("<td>").append(rs.getInt("age")).append("</td>")
                        .append("<td>").append(rs.getString("gender")).append("</td>")
                        .append("<td>").append(rs.getInt("course_id")).append("</td>")
                        .append("<td>").append(rs.getString("section")).append("</td></tr>");
            }
            sb.append("</table>");
            return sb.toString();
        } catch (SQLException e) {
            return "<p style='color:red'>Error: " + e.getMessage() + "</p>";
        }
    }

    public String addStudentMarksByStudentId(int studentId, int courseId, int marks) {
        try {
            String fetchQuery = "SELECT section FROM students WHERE student_id = ? AND course_id = ?";
            PreparedStatement fetchPs = connection.prepareStatement(fetchQuery);
            fetchPs.setInt(1, studentId);
            fetchPs.setInt(2, courseId);
            ResultSet rs = fetchPs.executeQuery();
            if (rs.next()) {
                String section = rs.getString("section");
                String insertQuery = "INSERT INTO student_marks(student_id, course_id, section, marks) VALUES (?, ?, ?, ?)";
                PreparedStatement insertPs = connection.prepareStatement(insertQuery);
                insertPs.setInt(1, studentId);
                insertPs.setInt(2, courseId);
                insertPs.setString(3, section);
                insertPs.setInt(4, marks);
                int rows = insertPs.executeUpdate();
                return rows > 0 ? "<p style='color:green'>Marks added successfully!</p>"
                        : "<p style='color:red'>Failed to add marks.</p>";
            } else {
                return "<p style='color:red'>Student not found OR wrong course_id.</p>";
            }
        } catch (SQLException e) {
            return "<p style='color:red'>Error: " + e.getMessage() + "</p>";
        }
    }

    public String viewAllMarksByStudentId(int studentId) {
        try {
            String query = "SELECT s.student_id, s.name, sm.course_id, s.section, sm.marks " +
                    "FROM students s JOIN student_marks sm ON s.student_id = sm.student_id WHERE s.student_id = ?";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1, studentId);
            ResultSet rs = ps.executeQuery();
            StringBuilder sb = new StringBuilder();
            sb.append("<table border='1' cellpadding='6'><tr><th>StudentID</th><th>Name</th><th>CourseID</th><th>Section</th><th>Marks</th></tr>");
            boolean found = false;
            while (rs.next()) {
                found = true;
                sb.append("<tr><td>").append(rs.getInt("student_id")).append("</td>")
                        .append("<td>").append(rs.getString("name")).append("</td>")
                        .append("<td>").append(rs.getInt("course_id")).append("</td>")
                        .append("<td>").append(rs.getString("section")).append("</td>")
                        .append("<td>").append(rs.getInt("marks")).append("</td></tr>");
            }
            if (!found) sb.append("<tr><td colspan='5'>No records found!</td></tr>");
            sb.append("</table>");
            return sb.toString();
        } catch (SQLException e) {
            return "<p style='color:red'>Error: " + e.getMessage() + "</p>";
        }
    }

    public String viewAllMarksByCourseId(int courseId) {
        try {
            String query = "SELECT s.student_id, s.name, sm.course_id, s.section, sm.marks " +
                    "FROM students s JOIN student_marks sm ON s.student_id = sm.student_id WHERE s.course_id = ?";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1, courseId);
            ResultSet rs = ps.executeQuery();
            StringBuilder sb = new StringBuilder();
            sb.append("<table border='1' cellpadding='6'><tr><th>StudentID</th><th>Name</th><th>CourseID</th><th>Section</th><th>Marks</th></tr>");
            boolean found = false;
            while (rs.next()) {
                found = true;
                sb.append("<tr><td>").append(rs.getInt("student_id")).append("</td>")
                        .append("<td>").append(rs.getString("name")).append("</td>")
                        .append("<td>").append(rs.getInt("course_id")).append("</td>")
                        .append("<td>").append(rs.getString("section")).append("</td>")
                        .append("<td>").append(rs.getInt("marks")).append("</td></tr>");
            }
            if (!found) sb.append("<tr><td colspan='5'>No records found!</td></tr>");
            sb.append("</table>");
            return sb.toString();
        } catch (SQLException e) {
            return "<p style='color:red'>Error: " + e.getMessage() + "</p>";
        }
    }
}
