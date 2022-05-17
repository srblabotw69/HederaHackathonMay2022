package HederaApp;

import java.io.File;
import java.util.Objects;
import java.io.IOException;

import com.hedera.hashgraph.sdk.ContractFunctionParameters;
//import com.hedera.hashgraph.sdk.TokenType;

import io.github.cdimascio.dotenv.Dotenv;

import Hedera.AccountService;
import Hedera.FileHandling;
import Hedera.FileService;
import Hedera.SmartContractService;
//import Hedera.TokenService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
 
@WebServlet("/HederaMainServlet")
public class HederaMainServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	
	@Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
 
			/////////////////////////////////////////////////////
			// Servlet post response
			/////////////////////////////////////////////////////		
//			Object data = "Starting Token Tree Website";
//			request.setAttribute("data", data);
//			
			
//			request.getRequestDispatcher("hedera/HederaMain.jsp").forward(request, response);
			request.getRequestDispatcher("/HederaMain.jsp").forward(request, response);
			

		} catch (Exception e) {
			System.out.println(e);
		}
	}
	
	@Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
 
			/////////////////////////////////////////////////////
			// Servlet post response
			/////////////////////////////////////////////////////		
//			Object data = "Starting Token Tree Website";
//			request.setAttribute("data", data);
//			
			
//			request.getRequestDispatcher("hedera/HederaMain.jsp").forward(request, response);
			request.getRequestDispatcher("WEB-INF/HederaMain.jsp").forward(request, response);
			

		} catch (Exception e) {
			System.out.println(e);
		}
	}
}