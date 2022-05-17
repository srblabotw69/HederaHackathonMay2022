package HederaApp;

import java.util.ArrayList;
import java.util.List;
 
import com.hedera.hashgraph.sdk.AccountId;
import com.hedera.hashgraph.sdk.PrivateKey;
import com.hedera.hashgraph.sdk.TokenId;

import Hedera.AccountService;
import Hedera.TokenService;

public class TokenServiceApp {

	public static void main(String[] args) {
		try {

			/////////////////////////////////////////////////////
			// Connect to DeveloperPortal account
			/////////////////////////////////////////////////////

			AccountService.connectToDeveloperPortal();
			
			/////////////////////////////////////////////////////
			// OPTION 2: Use existing account
			/////////////////////////////////////////////////////

			AccountId accId = AccountId.fromString("0.0.34717281");
			PrivateKey accKey = PrivateKey.fromString("3030020100300706052b8104000a042204200ccad0888e6f4ffcea487faae34880fb81e62485d3530e8f6386d32893cc8f1a");

			/////////////////////////////////////////////////////
			//  Token Service
			/////////////////////////////////////////////////////

			TokenService tokenservice = new TokenService();
 
			//0.0.34737422
//			TokenId tokenId = TokenId.fromString("0.0.34737422"); //deleted, disassociated
//			TokenId tokenId = TokenId.fromString("0.0.34737392"); //not deleted, disassociated
			
			
			//0.0.3473748
			TokenId tokenId = TokenId.fromString("0.0.34737488"); 
			tokenservice.getTokenInfo(AccountService.client, tokenId);
			
			/////////////////////////////////////////////////////
			//  Delete Token  
			/////////////////////////////////////////////////////
			
			tokenservice.deleteToken(AccountService.client, accKey, tokenId);
			
			/////////////////////////////////////////////////////
			// Token dissociate from List
			/////////////////////////////////////////////////////
 
			List<TokenId> tokenList = new ArrayList<TokenId>();		
			tokenList.add(tokenId);
			
			for(int i=0;i<tokenList.size();i++){
			    System.out.println("tokens in tokenList: " + '\n' + tokenList.get(i) + '\n');
			} 
	 
			tokenservice.dissociateToken(AccountService.client, accKey, accId, tokenList);


		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}