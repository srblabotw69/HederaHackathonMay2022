package HederaApp;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.io.IOException;

import com.hedera.hashgraph.sdk.AccountId;
import com.hedera.hashgraph.sdk.AccountInfo;
import com.hedera.hashgraph.sdk.PrivateKey;
import com.hedera.hashgraph.sdk.TokenId;
import com.hedera.hashgraph.sdk.TokenRelationship;

import Hedera.AccountService;
import Hedera.JSONToken;
import Hedera.TokenService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/MintTokenUsingHTSServlet_deleteToken")
public class MintTokenUsingHTSServlet_deleteToken extends HttpServlet {

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
			PrivateKey accKey = PrivateKey.fromString("3030020100300706052b8104000a042204200ccad0888e6f4ffcea487faae34880fb81e62485d3530e8f6386d32893cc8f1a");
		
			/////////////////////////////////////////////////////
			// Delete Fungible Token
			/////////////////////////////////////////////////////

			TokenService tokenservice = new TokenService();
			
//			String tokenIdFromJSP = request.getParameter("tokenId");
//			System.out.println("tokenId from JSP: " + tokenIdFromJSP.toString());
			
			String categoryFromJSP = request.getParameter("category");
			System.out.println("category from JSP: " + categoryFromJSP.toString());

			TokenId tokenIdToDelete = TokenId.fromString(categoryFromJSP);
			tokenservice.deleteToken(AccountService.client, accKey, tokenIdToDelete);

			/////////////////////////////////////////////////////
			// Token dissociate from List
			/////////////////////////////////////////////////////
 
			List<TokenId> tokenList = new ArrayList<TokenId>();
			tokenList.add(tokenIdToDelete);
			
			for(int i=0;i<tokenList.size();i++){
			    System.out.println("tokens associated with this account: " + '\n' + tokenList.get(i) + '\n');
			} 
	 
			tokenservice.dissociateToken(AccountService.client, accKey, accId, tokenList);
 	
 
			/////////////////////////////////////////////////////
			// Servlet post response
			/////////////////////////////////////////////////////
			Object data = "You have deleted a token using HTS";
			request.setAttribute("data", data);
			
			
			
			
			/////////////////////////////////////////////////////
			// Get account info
			/////////////////////////////////////////////////////

			AccountInfo accountInfo = AccountService.getAccountInfo(AccountService.client, accId);
			System.out.println("accountInfo: " + accountInfo.toString() + '\n');
			
			
			/////////////////////////////////////////////////////
			// Get account token info
			/////////////////////////////////////////////////////
//			TokenId tokenRelationships = null;
			
//				JSONObject jsonObj = new JSONObject();
				
				List<JSONToken> tokenDetails = new ArrayList<JSONToken>();

				List<JSONToken> products = new ArrayList<JSONToken>();

//			Map<TokenId, TokenRelationship> accountTokenInfoMap = accountInfo.tokenRelationships;
//			System.out.println("All tokens associated with this account:");
//	        for (TokenId key: accountTokenInfoMap.keySet()) {
//	        	json.put("tokenID", key);
//	            System.out.println(key);
//		        }
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
	          	
	          	products.add(new JSONToken(substrTokenIdWithoutLabel));
	  
	         	System.out.println(substrTokenIdWithoutLabel);
	        	System.out.println(substrTokenId.toString() + " " + substrTokenSym.toString());
	         	System.out.println("numOfDocs: " + numOfDocs);

		    }
	        System.out.println("");
 
			request.setAttribute("treasuryId", treasuryId);
			
			request.setAttribute("accId", accId);
	        
			request.setAttribute("tokenDetails", tokenDetails);
			request.setAttribute("products", products);

//			request.getRequestDispatcher("hedera/MintTokenUsingHTS.jsp").forward(request, response);
			request.getRequestDispatcher("hedera/HederaMain.jsp").forward(request, response);
 

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}