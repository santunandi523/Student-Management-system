package StudentManagementSystem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Courses {
    Connection connection;

    public Courses(Connection connection) {
        this.connection = connection;
    }

    public String viewAllCourses() {
        try {
            String query = "SELECT * FROM courses";
            PreparedStatement ps = connection.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            StringBuilder sb = new StringBuilder();
            sb.append("<table border='1' cellpadding='6'><tr><th>Course ID</th><th>Course Name</th></tr>");
            boolean found = false;
            while (rs.next()) {
                found = true;
                sb.append("<tr><td>").append(rs.getInt("course_id")).append("</td>")
                        .append("<td>").append(rs.getString("course_name")).append("</td></tr>");
            }
            if (!found) sb.append("<tr><td colspan='2'>No courses found!</td></tr>");
            sb.append("</table>");
            return sb.toString();
        } catch (SQLException e) {
            return "<p style='color:red'>Error: " + e.getMessage() + "</p>";
        }
    }
}
