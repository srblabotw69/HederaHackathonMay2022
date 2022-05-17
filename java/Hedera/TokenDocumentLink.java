package Hedera;

import java.util.Iterator;

import org.json.*;
import org.json.simple.parser.JSONParser;
 
import com.hedera.hashgraph.sdk.AccountId;
import com.hedera.hashgraph.sdk.Client;
import com.hedera.hashgraph.sdk.PrivateKey;

public class TokenDocumentLink extends SmartContractService {

	/**
	 * Associate documents to token
	 */
	public void createTokenDocumentLink(byte[] bytecode, Client client, PrivateKey adminKey, PrivateKey treasuryKey,
			AccountId treasuryId, String tokenName, int numOfDocs, String[] docHash) {
		try {
			System.out.println("ContractToDocumentLink.createTokenDocLink():");

			String json = "{" + "\"token\": " + tokenName + "," + "\"documents\": [";

			for (int i = 1; i <= numOfDocs; i++) {
				json += "{ \"ID\": " + Integer.toString(i) + ", \"name\": " + docHash[i] + " }";
				if (i != 100)
					json += ",";
			}
			json += "]}";

			JSONObject jsonObject = new JSONObject(json);

			System.out.println("This token is linked to these document(s)");
			System.out.println(jsonObject.toString() + '\n');

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Get documents related to token
	 */
	public void getTokenDocuments(String tokenID, String json, JSONObject jsonObject) {
		JSONParser parser = new JSONParser();
		try {
			System.out.println("ContractToDocumentLink.getTokenDocuments():");

			JSONObject questionMark = jsonObject.getJSONObject("documents");
			Iterator<String> keys = questionMark.keys();
			    
			while(keys.hasNext()) {
			    // loop to get the dynamic key
			    String currentDynamicKey = (String)keys.next();
			        
			    // get the value of the dynamic key
			    JSONObject currentDynamicValue = questionMark.getJSONObject(currentDynamicKey);
			   // do something here with the value...
			    
			    System.out.println(parser);
			    System.out.println(currentDynamicValue);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Get documents related to token
	 */
	public void createSmartContractToLinkDocumentsToToken(String tokenID, String json, JSONObject jsonObject) {
		JSONParser parser = new JSONParser();
		try {
			System.out.println("ContractToDocumentLink.createSmartContractToLinkDocumentsToToken():");
 
		    System.out.println(parser);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
