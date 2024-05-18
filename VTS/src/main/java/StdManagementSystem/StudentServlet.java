package StdManagementSystem;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.ProcessBuilder.Redirect;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/StudentServlet")
public class StudentServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/student_management", "root", "saisaran");

            switch(action) {
                case "add":
                    addStudent(request, response, conn);
                    break;
                case "update":
                    updateStudent(request, response, conn);
                    break;
                case "delete":
                    deleteStudent(request, response, conn);
                    break;
                case "viewById":
                    viewStudentById(request, response, conn);
                    break;
                case "view":
                    viewStudents(response, conn);
                    break;
            }

            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addStudent(HttpServletRequest request, HttpServletResponse response, Connection conn) throws SQLException, IOException {
        String name = request.getParameter("name");
        int age = Integer.parseInt(request.getParameter("age"));
        String email = request.getParameter("email");

        String sql = "INSERT INTO students (name, age, email) VALUES (?, ?, ?)";
        PreparedStatement statement = conn.prepareStatement(sql);
        statement.setString(1, name);
        statement.setInt(2, age);
        statement.setString(3, email);

        statement.executeUpdate();
        PrintWriter out = response.getWriter();
        out.println("<html> <body>");
        response.getWriter().println("Student added successfully.");
        
        
        out.println("<a href='index.html'>Back to Home</a>");
        out.println("</body> </html>");
    }

    private void updateStudent(HttpServletRequest request, HttpServletResponse response, Connection conn) throws SQLException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        String name = request.getParameter("name");
        int age = Integer.parseInt(request.getParameter("age"));
        String email = request.getParameter("email");

        String sql = "UPDATE students SET name = ?, age = ?, email = ? WHERE id = ?";
        PreparedStatement statement = conn.prepareStatement(sql);
        statement.setString(1, name);
        statement.setInt(2, age);
        statement.setString(3, email);
        statement.setInt(4, id);

        statement.executeUpdate();
        PrintWriter out = response.getWriter();
        out.println("<html> <body>");
        response.getWriter().println("Student updated successfully./n");
        
        
        out.println("<a href='index.html'>Back to Home</a>");
        out.println("</body> </html>");
    }

    private void deleteStudent(HttpServletRequest request, HttpServletResponse response, Connection conn) throws SQLException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));

        String sql = "DELETE FROM students WHERE id = ?";
        PreparedStatement statement = conn.prepareStatement(sql);
        statement.setInt(1, id);
        statement.executeUpdate();
        
        PrintWriter out = response.getWriter();
        out.println("<html> <body>");
        
        response.getWriter().println("Student deleted successfully./n");
        
        
        out.println("<a href='index.html'>Back to Home</a>");
        out.println("</body> </html>");
        
    }
    
    private void viewStudentById(HttpServletRequest request, HttpServletResponse response, Connection conn) throws SQLException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));

        String sql = "SELECT * FROM students WHERE id = ?";
        PreparedStatement statement = conn.prepareStatement(sql);
        statement.setInt(1, id);
        ResultSet resultSet = statement.executeQuery();

        PrintWriter out = response.getWriter();
        out.println("<html><body><h2>Student Details</h2><table border='1'><tr><th>ID</th><th>Name</th><th>Age</th><th>Email</th></tr>");

        if (resultSet.next()) {
            int studentId = resultSet.getInt("id");
            String name = resultSet.getString("name");
            int age = resultSet.getInt("age");
            String email = resultSet.getString("email");

            out.println("<tr><td>" + studentId + "</td><td>" + name + "</td><td>" + age + "</td><td>" + email + "</td></tr>");
        } else {
            out.println("<tr><td colspan='4'>Student not found</td></tr>");
        }
        out.println("</table>");
        
        out.println("<a href='index.html'>Back to Home</a>");
        out.println("</body> </html>");
    }

    private void viewStudents(HttpServletResponse response, Connection conn) throws SQLException, IOException {
        String sql = "SELECT * FROM students";
        PreparedStatement statement = conn.prepareStatement(sql);
        ResultSet resultSet = statement.executeQuery();

        PrintWriter out = response.getWriter();
        out.println("<html><body><h2>Student List</h2><table border='1'><tr><th>ID</th><th>Name</th><th>Age</th><th>Email</th></tr>");

        while (resultSet.next()) {
            int id = resultSet.getInt("id");
            String name = resultSet.getString("name");
            int age = resultSet.getInt("age");
            String email = resultSet.getString("email");

            out.println("<tr><td>" + id + "</td><td>" + name + "</td><td>" + age + "</td><td>" + email + "</td></tr>");
        }
        out.println("</table>");
        
        out.println("<a href='index.html'>Back to Home</a>");
        out.println("</body> </html>");
    }
}