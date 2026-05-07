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
        out.println("<head><title>Users List</title><link rel='stylesheet' href='hello.css'></head>");
        out.println("<body><div class='container'>");
        out.println("<h1>Users from Database</h1>");
        out.println("<table>");
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
        out.println("<div class='card'>");
        out.println("<h2>New User</h2>");
        out.println("<form action='hello' method='POST'>");
        out.println("  <div class='field'><label for='name'>Name</label><input type='text' id='name' name='name' required></div>");
        out.println("  <div class='field'><label for='description'>Description</label><input type='text' id='description' name='description' required></div>");
        out.println("  <button type='submit'>Save</button>");
        out.println("</form>");
        out.println("</div>");
        out.println("</div></body>");
        out.println("</html>");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    //cambiado aqui
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