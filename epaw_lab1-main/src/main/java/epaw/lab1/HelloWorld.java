package epaw.lab1;

import epaw.lab1.util.DBManager;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

@WebServlet("/hello")
public class HelloWorld extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        
        out.println("<!DOCTYPE html>");
        out.println("<html>");
        out.println("<head><title>Users List</title></head>");
        out.println("<body>");
        out.println("<h1>Users from Database</h1>");
        out.println("<table border='1'>");
        out.println("<tr><th>ID</th><th>Name</th><th>Description</th></tr>");

        try (DBManager db = new DBManager()) {
            PreparedStatement stmt = db.prepareStatement("SELECT id, name, description FROM users");
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String description = rs.getString("description");
                
                out.println("<tr>");
                out.println("<td>" + id + "</td>");
                out.println("<td>" + name + "</td>");
                out.println("<td>" + description + "</td>");
                out.println("</tr>");
            }
        } catch (Exception e) {
            out.println("<tr><td colspan='3'>Error: " + e.getMessage() + "</td></tr>");
            e.printStackTrace();
        }

        out.println("</table>");
        // Codigo Marti
        out.println("<h2>New User</h2>");
        out.println("<form action='hello' method='POST'");
        out.println("  <div>");
        out.println("    <label for='name'>Name:</label><br>");
        out.println("    <input type='text' id='name' name='name' required>");
        out.println("  </div>");
        out.println("  <div style='margin-top: 10px;'>");
        out.println("    <label for='description'>Description:</label><br>");
        out.println("    <input type='text' id='description' name='description' required>");
        out.println("  </div>");
        out.println("  <div style='margin-top: 15px;'>");
        out.println("    <button type='submit'>Save</button>");
        out.println("  </div>");
        out.println("</form>");
        //.
        out.println("</body>");
        out.println("</html>");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    //codigo Marti
    String name = request.getParameter("name");
    String description = request.getParameter("description");

    try (DBManager db = new DBManager()) {
        String sql = "INSERT INTO users (name, description) VALUES (?, ?)";
        PreparedStatement stmt = db.prepareStatement(sql);
        stmt.setString(1, name);
        stmt.setString(2, description);
        
        stmt.executeUpdate();
        
    } catch (Exception e) {
        e.printStackTrace();
    }

    response.sendRedirect("hello");
    }
}