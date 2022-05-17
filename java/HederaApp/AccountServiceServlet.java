package HederaApp;
 
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.hedera.hashgraph.sdk.AccountId;
import com.hedera.hashgraph.sdk.AccountInfo;
import com.hedera.hashgraph.sdk.TokenId;
import com.hedera.hashgraph.sdk.TokenRelationship;

import Hedera.AccountService;
import Hedera.JSONToken;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/AccountServiceServlet")
public class AccountServiceServlet extends HttpServlet {
 
	private static final long serialVersionUID = 1L;

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {

			/////////////////////////////////////////////////////
			// Connect to DeveloperPortal account
			/////////////////////////////////////////////////////

			AccountService.connectToDeveloperPortal();
			
			AccountId treasuryId = AccountService.treasuryId;
			request.setAttribute("treasuryId", treasuryId);

//			/////////////////////////////////////////////////////
//			// OPTION 1:  Create a new account
//			/////////////////////////////////////////////////////
//
//			AccountService acctService = new AccountService();
//			acctService.createAccount();
//			PrivateKey accKey = acctService.accountKey;
// 
			/////////////////////////////////////////////////////
			// OPTION 2: Use existing account
			/////////////////////////////////////////////////////

			AccountId accId = AccountId.fromString("0.0.34717281");
//			PrivateKey accKey = PrivateKey.fromString("3030020100300706052b8104000a042204200ccad0888e6f4ffcea487faae34880fb81e62485d3530e8f6386d32893cc8f1a");
	
			request.setAttribute("accId", accId);
			
			/////////////////////////////////////////////////////
			// Get account info
			/////////////////////////////////////////////////////

			AccountInfo accountInfo = AccountService.getAccountInfo(AccountService.client, accId);
			System.out.println("token accountInfo: " + accountInfo.toString() + '\n');
			
			/////////////////////////////////////////////////////
			// Get account token info
			/////////////////////////////////////////////////////
//			TokenId tokenRelationships = null;
			
// 			JSONObject jsonObj = new JSONObject();
  			
 			List<JSONToken> tokenDetails = new ArrayList<JSONToken>();

 			List<JSONToken> tokenIdOnly = new ArrayList<JSONToken>();

//			Map<TokenId, TokenRelationship> accountTokenInfoMap = accountInfo.tokenRelationships;
//			System.out.println("All tokens associated with this account:");
//	        for (TokenId key: accountTokenInfoMap.keySet()) {
//	        	json.put("tokenID", key);
//	            System.out.println(key);
// 	        }
//	        System.out.println("");
	     
	    	Map<TokenId, TokenRelationship> accountTokenInfoMap2 = accountInfo.tokenRelationships;
			System.out.println("All tokens details associated with this account:");
			String substrTokenId, substrTokenIdWithoutLabel, substrTokenSym = null;
 			
 
	        for (TokenRelationship value: accountTokenInfoMap2.values()) {
 	        	substrTokenId = value.toString().substring(18, 38);
	         	substrTokenSym = value.toString().substring(47, 60);
	         	substrTokenIdWithoutLabel = value.toString().substring(26, 38);
	 	         	        	
	         	String lastChr = substrTokenId.substring(substrTokenId.length() - 1);
 	         	int numOfDocs = Integer.valueOf(lastChr); 
	          	tokenDetails.add(new JSONToken(substrTokenIdWithoutLabel, substrTokenSym, numOfDocs));
	          	
	          	tokenIdOnly.add(new JSONToken(substrTokenIdWithoutLabel));
	       
	         	System.out.println(substrTokenIdWithoutLabel);
	        	System.out.println(substrTokenId.toString() + " " + substrTokenSym.toString());
	        	System.out.println("numOfDocs: " + numOfDocs);
 
 	        }
	        System.out.println("");
	        
	        //System.out.println(products.toString());
	        
//	        for(int i = 0; i < products.size(); i++) {
//	            System.out.println(products.get(i).getId());
//	        }
 
			/////////////////////////////////////////////////////
			// Servlet post response
			/////////////////////////////////////////////////////
			Object data = "Here are the tokens associated with this the account.";
			request.setAttribute("data", data);
//			request.setAttribute("accountInfo", accountInfo);
// 			request.setAttribute("strObjList", jsonObj.toString());
 			
 			request.setAttribute("tokenDetails", tokenDetails);
  			request.setAttribute("products", tokenIdOnly);
 			

			
// 			String jsonString = accountInfo.toString().substring(11);
 			//System.out.println(accountInfo.toString().substring(11));
 			
 			
// 			JSONObject json = new JSONObject(jsonString);
//   			System.out.println(json.toString());
 			 
//			JSONObject json = (JSONObject) new JSONParser().parse(accountInfo.toString());
//			System.out.println(json.toString());
 
//			
//			List<String> accInfo = Arrays.asList(json.toString());
//			request.setAttribute("accInfo", accInfo);

			
//			JSONObject json = new JSONObject(accountInfo); 
//			request.setAttribute("accInfo", json);
  			System.out.println(request.getContextPath());
			request.getRequestDispatcher("hedera/HederaMain.jsp").forward(request, response);
//			response.sendRedirect(request.getContextPath() + "/hedera/HederaMain.jsp");
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}