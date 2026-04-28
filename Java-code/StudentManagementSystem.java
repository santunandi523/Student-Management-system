package StudentManagementSystem;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;

import org.apache.catalina.Context;
import org.apache.catalina.startup.Tomcat;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/sms")

public class StudentManagementSystem extends HttpServlet {

    static final String URL = "jdbc:mysql://127.0.0.1:3306/university";
    static final String USER = "root";
    static final String PASS = "Santujeni@2116";

    private Connection connection;
    private Student student;
    private Faculty faculty;
    private Courses courses;

    public static void main(String[] args) throws Exception {
        Tomcat tomcat = new Tomcat();
        tomcat.setPort(8081);

       
        tomcat.getConnector();

        Context context = tomcat.addContext("", new File(".").getAbsolutePath());
        
        Tomcat.addServlet(context, "smsServlet", new StudentManagementSystem());
        context.addServletMappingDecoded("/sms", "smsServlet");

        tomcat.start();
        System.out.println("Tomcat started on http://localhost:8081/sms");
        tomcat.getServer().await();
    }

    @Override
    public void init() throws ServletException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection = DriverManager.getConnection(URL, USER, PASS);

            student = new Student(connection);
            faculty = new Faculty(connection);
            courses = new Courses(connection);

        } catch (Exception e) {
            throw new ServletException("DB connection failed: " + e.getMessage());
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        resp.setContentType("text/html");
        PrintWriter out = resp.getWriter();

        String action = req.getParameter("action");

        if (action == null) {
            out.println(buildPage("Home", getMenuHtml()));
            return;
        }

        String result = "";
        String title = "Student Management System";

        switch (action) {

            case "viewAllStudents":
                title = "All Students";
                result = student.viewAllStudents();
                break;

            case "viewAllFaculty":
                title = "All Faculty";
                result = faculty.viewAllFaculties();
                break;

            case "viewAllCourses":
                title = "All Courses";
                result = courses.viewAllCourses();
                break;

            case "showAddStudentForm":
                title = "Add Student";
                result = getAddStudentFormHtml();
                break;

            case "showAddFacultyForm":
                title = "Add Faculty";
                result = getAddFacultyFormHtml();
                break;

            case "showViewStudentByIdForm":
                title = "View Student By ID";
                result = getSingleInputFormHtml("viewStudentById", "studentId", "Student ID", "View Student");
                break;

            case "showViewStudentByCourseIdForm":
                title = "View Student By Course ID";
                result = getSingleInputFormHtml("viewStudentByCourseId", "courseId", "Course ID", "View Students");
                break;

            case "showViewAllMarksByStudentIdForm":
                title = "View Marks By Student ID";
                result = getSingleInputFormHtml("viewAllMarksByStudentId", "studentId", "Student ID", "View Marks");
                break;

            case "showAddStudentMarksForm":
                title = "Add Student Marks";
                result = getAddStudentMarksFormHtml();
                break;

            default:
                result = "<p>Invalid Action</p>";
        }

        out.println(buildPage(title, result));
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        resp.setContentType("text/html");
        PrintWriter out = resp.getWriter();

        String action = req.getParameter("action");
        String result = "";

        if ("addStudent".equals(action)) {

            String name = req.getParameter("name");
            int age = Integer.parseInt(req.getParameter("age"));
            String gender = req.getParameter("gender");
            int courseId = Integer.parseInt(req.getParameter("courseId"));
            String section = req.getParameter("section");

            result = student.addStudent(name, age, gender, courseId, section);

        } else if ("addFaculty".equals(action)) {

            String name = req.getParameter("name");
            int age = Integer.parseInt(req.getParameter("age"));
            String gender = req.getParameter("gender");
            int courseId = Integer.parseInt(req.getParameter("courseId"));
            String specialization = req.getParameter("specialization");

            result = faculty.addFaculty(name, age, gender, courseId, specialization);

        } else if ("viewStudentById".equals(action)) {
            int studentId = Integer.parseInt(req.getParameter("studentId"));
            result = student.viewStudentById(studentId) + "<br><p><a href='?'>Back to Home</a></p>";

        } else if ("viewStudentByCourseId".equals(action)) {
            int courseId = Integer.parseInt(req.getParameter("courseId"));
            result = student.viewStudentByCourseId(courseId) + "<br><p><a href='?'>Back to Home</a></p>";

        } else if ("viewAllMarksByStudentId".equals(action)) {
            int studentId = Integer.parseInt(req.getParameter("studentId"));
            result = student.viewAllMarksByStudentId(studentId) + "<br><p><a href='?'>Back to Home</a></p>";

        } else if ("addStudentMarks".equals(action)) {
            int studentId = Integer.parseInt(req.getParameter("studentId"));
            int courseId = Integer.parseInt(req.getParameter("courseId"));
            int marks = Integer.parseInt(req.getParameter("marks"));
            result = student.addStudentMarksByStudentId(studentId, courseId, marks) + "<br><p><a href='?'>Back to Home</a></p>";
        }

        out.println(buildPage("Result", result));
    }

    // ---------- HTML ----------
    private String getMenuHtml() {
        return """
                <h2>Student Management System</h2>
                <ul>
                    <li><a href='?action=viewAllStudents'>View Students</a></li>
                    <li><a href='?action=viewAllFaculty'>View Faculty</a></li>
                    <li><a href='?action=viewAllCourses'>View Courses</a></li>
                    <li><a href='?action=showAddStudentForm'>Add Student</a></li>
                    <li><a href='?action=showAddFacultyForm'>Add Faculty</a></li>
                    <li><a href='?action=showViewStudentByIdForm'>View Student By ID</a></li>
                    <li><a href='?action=showViewStudentByCourseIdForm'>View Student By Course ID</a></li>
                    <li><a href='?action=showAddStudentMarksForm'>Add Student Marks</a></li>
                    <li><a href='?action=showViewAllMarksByStudentIdForm'>View All Marks By Student ID</a></li>
                </ul>
                """;
    }

    private String getAddStudentFormHtml() {
        return """
                <form method='post' action='/sms'>
                    <input type='hidden' name='action' value='addStudent'/>
                    Name: <input name='name' required/><br><br>
                    Age: <input name='age' required/><br><br>
                    Gender: <input name='gender' required/><br><br>
                    Course ID: <input name='courseId' required/><br><br>
                    Section: <input name='section' required/><br><br>
                    <button type='submit'>Add Student</button>
                </form>
                <br><p><a href='?'>Back to Home</a></p>
                """;
    }

    private String getAddFacultyFormHtml() {
        return """
                <form method='post' action='/sms'>
                    <input type='hidden' name='action' value='addFaculty'/>
                    Name: <input name='name' required/><br><br>
                    Age: <input name='age' required/><br><br>
                    Gender: <input name='gender' required/><br><br>
                    Course ID: <input name='courseId' required/><br><br>
                    Specialization: <input name='specialization' required/><br><br>
                    <button type='submit'>Add Faculty</button>
                </form>
                <br><p><a href='?'>Back to Home</a></p>
                """;
    }

    private String getSingleInputFormHtml(String actionValue, String inputName, String label, String buttonText) {
        return """
               <form method='post' action='/sms'>
                   <input type='hidden' name='action' value='%s'/>
                   %s: <input name='%s' required/><br><br>
                   <button type='submit'>%s</button>
               </form>
               <br><p><a href='?'>Back to Home</a></p>
               """.formatted(actionValue, label, inputName, buttonText);
    }

    private String getAddStudentMarksFormHtml() {
        return """
                <form method='post' action='/sms'>
                    <input type='hidden' name='action' value='addStudentMarks'/>
                    Student ID: <input name='studentId' required/><br><br>
                    Course ID: <input name='courseId' required/><br><br>
                    Marks: <input name='marks' required/><br><br>
                    <button type='submit'>Add Marks</button>
                </form>
                <br><p><a href='?'>Back to Home</a></p>
                """;
    }

    private String buildPage(String title, String body) {
        return "<html><body>" +
                "<h1>" + title + "</h1>" +
                body +
                "</body></html>";
    }
}