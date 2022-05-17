package HederaApp;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
 
@WebServlet("/helloservlet")
public class HelloServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

	@Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
         System.out.print("Servlet");
 
         Object data = "Some data, can be a String or a Javabean";
         request.setAttribute("data", data);
         
         request.getRequestDispatcher("hedera/HelloServlet.jsp").forward(request, response);
         request.setAttribute("data", data);
    }

}