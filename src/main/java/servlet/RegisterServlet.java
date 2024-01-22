package servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import servlet.DatabaseConnection;

@WebServlet("/RegisterServlet")
public class RegisterServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;


    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    	 try {
            
             Connection con = DatabaseConnection.initializeDatabase();

             PreparedStatement st = con.prepareStatement("insert into users(username,password,email) values(?, ?, ?)");

             st.setString(1, request.getParameter("username"));

             st.setString(2, request.getParameter("password"));

             st.setString(3, request.getParameter("email"));
             
             st.executeUpdate();

             st.close();
             con.close();

             PrintWriter out = response.getWriter();
             out.println("<html><body><b>Successfully Inserted" + "</b></body></html>");
         } catch (Exception e) {
             e.printStackTrace();
         }

        
        response.sendRedirect("login.html");
    }
}
