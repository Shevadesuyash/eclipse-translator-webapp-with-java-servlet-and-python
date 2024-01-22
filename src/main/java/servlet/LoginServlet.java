package servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import servlet.DatabaseConnection;

@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {

			String inputUsername = request.getParameter("username");
			String inputPassword = request.getParameter("password");

			Connection con = DatabaseConnection.initializeDatabase();

			String sql = "SELECT * FROM users WHERE username = ? AND password = ?";

			PreparedStatement st = con.prepareStatement(sql);

			st.setString(1, inputUsername);
			st.setString(2, inputPassword);

			try (ResultSet resultSet = st.executeQuery()) {

				if (resultSet.next()) {

					// If authentication is successful, create a session.
					HttpSession session = request.getSession(true);

					// Store the username in the session.
					session.setAttribute("username", inputUsername);

					// Redirect to the home page or any other secured page.
					response.sendRedirect("translator.html");
				} else {
					// Handle invalid credentials.
					response.sendRedirect("login.html?error=1");
				}

			} catch (SQLException e) {
				e.printStackTrace();

			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
