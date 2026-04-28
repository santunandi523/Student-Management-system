package StudentManagementSystem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Faculty {
    private Connection connection;

    public Faculty(Connection connection) {
        this.connection = connection;
    }

    public String addFaculty(String name, int age, String gender, int courseId, String specialization) {
        try {
            String query = "INSERT INTO faculties(name, age, gender, course_id, specialization) VALUES(?, ?, ?, ?, ?)";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, name);
            ps.setInt(2, age);
            ps.setString(3, gender);
            ps.setInt(4, courseId);
            ps.setString(5, specialization);
            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                return "<p style='color:green'>Faculty Added Successfully!</p><p><a href='?'>Back to Home</a></p>";
            } else {
                return "<p style='color:red'>Failed to add faculty!</p><p><a href='?'>Back to Home</a></p>";
            }
        } catch (SQLException e) {
            return "<p style='color:red'>Error: " + e.getMessage() + "</p><p><a href='?'>Back to Home</a></p>";
        }
    }

    public String viewFacultyById(int facultyId) {
        try {
            String query = "SELECT faculty_id, name, age, gender, course_id, specialization " +
                    "FROM faculties WHERE faculty_id = ?";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1, facultyId);
            ResultSet rs = ps.executeQuery();
            StringBuilder sb = new StringBuilder();
            sb.append("<table border='1' cellpadding='6'>"
                    + "<tr><th>FacultyID</th><th>Name</th><th>Age</th><th>Gender</th><th>CourseID</th><th>Specialization</th></tr>");
            if (rs.next()) {
                sb.append("<tr>")
                        .append("<td>").append(rs.getInt("faculty_id")).append("</td>")
                        .append("<td>").append(rs.getString("name")).append("</td>")
                        .append("<td>").append(rs.getInt("age")).append("</td>")
                        .append("<td>").append(rs.getString("gender")).append("</td>")
                        .append("<td>").append(rs.getInt("course_id")).append("</td>")
                        .append("<td>").append(rs.getString("specialization")).append("</td>")
                        .append("</tr>");
            } else {
                sb.append("<tr><td colspan='6'>Faculty not found!</td></tr>");
            }
            sb.append("</table>");
            return sb.toString();
        } catch (SQLException e) {
            return "<p style='color:red'>Error: " + e.getMessage() + "</p>";
        }
    }

    public String viewAllFaculties() {
        try {
            String query = "SELECT * FROM faculties";
            PreparedStatement ps = connection.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            StringBuilder sb = new StringBuilder();
            sb.append("<table border='1' cellpadding='6'>"
                    + "<tr><th>FacultyID</th><th>Name</th><th>Age</th><th>Gender</th><th>CourseID</th><th>Specialization</th></tr>");
            // FIX: added empty-result fallback — was silently returning a blank table
            boolean found = false;
            while (rs.next()) {
                found = true;
                sb.append("<tr>")
                        .append("<td>").append(rs.getInt("faculty_id")).append("</td>")
                        .append("<td>").append(rs.getString("name")).append("</td>")
                        .append("<td>").append(rs.getInt("age")).append("</td>")
                        .append("<td>").append(rs.getString("gender")).append("</td>")
                        .append("<td>").append(rs.getInt("course_id")).append("</td>")
                        .append("<td>").append(rs.getString("specialization")).append("</td>")
                        .append("</tr>");
            }
            if (!found) sb.append("<tr><td colspan='6'>No faculty records found!</td></tr>");
            sb.append("</table>");
            return sb.toString();
        } catch (SQLException e) {
            return "<p style='color:red'>Error: " + e.getMessage() + "</p>";
        }
    }
}
