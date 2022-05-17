package HederaApp;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.io.IOException;

import com.hedera.hashgraph.sdk.AccountId;
import com.hedera.hashgraph.sdk.AccountInfo;
import com.hedera.hashgraph.sdk.ContractFunctionParameters;
import com.hedera.hashgraph.sdk.ContractId;
//import com.hedera.hashgraph.sdk.TokenType;
import com.hedera.hashgraph.sdk.PrivateKey;
import com.hedera.hashgraph.sdk.TokenId;
import com.hedera.hashgraph.sdk.TokenRelationship;

import io.github.cdimascio.dotenv.Dotenv;

import Hedera.AccountService;
import Hedera.FileHandling;
import Hedera.FileService;
import Hedera.JSONToken;
import Hedera.SmartContractService;
//import Hedera.TokenService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
 
@WebServlet("/SmartContractServiceERC1400Servlet_callContract")
public class SmartContractServiceERC1400Servlet_callContract extends HttpServlet {

	final private static String binFileAbsPath = System.getProperty("user.dir")
			+ Objects.requireNonNull(Dotenv.load().get("SOURCE_CODE_PACKAGE_HTS_ERC1400V2"));
	final private static String filename = Objects.requireNonNull(Dotenv.load().get("CONTRACT_BIN_FILENAME_ERC1400V2"));

	private static final long serialVersionUID = 1L;
	
	@Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {

			/////////////////////////////////////////////////////
			// Connect to DeveloperPortal account
			/////////////////////////////////////////////////////

			AccountService.connectToDeveloperPortal();

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

//			AccountId accId = AccountId.fromString("0.0.34717281");
//			PrivateKey accKey = PrivateKey.fromString("3030020100300706052b8104000a042204200ccad0888e6f4ffcea487faae34880fb81e62485d3530e8f6386d32893cc8f1a");
//		
//			/////////////////////////////////////////////////////
//			// Split file to max 1024KB
//			/////////////////////////////////////////////////////
//
//			File file = new File(binFileAbsPath + filename);
//
//			System.out.println("contract binary filename: " + file.getName());
//			System.out.println("absolute filepath: " + file.getAbsolutePath() + '\n');
//
//			// split into 1024KB files
//			int numOfFiles = FileHandling.splitFile(file);
//
//			/////////////////////////////////////////////////////
//			// Add file to Hedera
//			/////////////////////////////////////////////////////
//
//			FileService fservice = new FileService();
//			File file001 = new File(file.getAbsoluteFile() + ".001");
//			byte[] bytecode = fservice.getByteCodeFromFile(file001.getAbsolutePath());
//
//			// add file on hedera. file001
//			fservice.addFileToHedera(bytecode, accKey, AccountService.client);
//
//			// append to file. file002, file003, etc...
//			String fileSuffix = "";
//			for (int i = 2; i <= numOfFiles; i++) {
//				fileSuffix = "." + String.format("%03d", i);
//				System.out.println("Get file for appending:");
//				System.out.println(file.getName() + fileSuffix);
//
//				bytecode = fservice.getByteCodeFromFile(file.getAbsoluteFile() + fileSuffix);
//				fservice.appendFileToHedera(bytecode, AccountService.client, accKey, fservice.fileId);
//			}
//
//			// show Hedera file content
//			System.out.println("show Hedera file content after append:");
//			fservice.getFileContent(AccountService.client, fservice.fileId);

			/////////////////////////////////////////////////////
			// Create smart contract
			/////////////////////////////////////////////////////

			SmartContractService smtContract = new SmartContractService();
//			smtContract.createSmartContract(AccountService.client, fservice.fileId,
//					new ContractFunctionParameters());
 
//			System.exit(0);
			
//			treasuryKey:302e020100300506032b6570042204201f316f53a5072f1d255566d0f8f59449632ec71ddd8955d98a2d3cc6ef08fd8e
//			treasuryId:0.0.34096250	
			
//			SmartContractService.CreateSmartContract():
//				The smart contract Id is 0.0.34752537
//				The smart contract Id in Solidity format is 0000000000000000000000000000000002124819
				 

			String contractIdInput = request.getParameter("contractIdInput");
			ContractId cid = ContractId.fromString(contractIdInput);
 			
 			
			/////////////////////////////////////////////////////
			// Call smart contract
			/////////////////////////////////////////////////////

 			smtContract.callSmartContractFunction_ERC1400_setDocument(AccountService.client, cid);

			smtContract.callSmartContractFunction_ERC1400_getDocument(AccountService.client, cid);
			
			/////////////////////////////////////////////////////
			// Query smart contract Info
			/////////////////////////////////////////////////////

			smtContract.getSmartContractInfo(AccountService.client, cid);
			
			
			/////////////////////////////////////////////////////
			// Connect to DeveloperPortal account
			/////////////////////////////////////////////////////

			AccountService.connectToDeveloperPortal();
			AccountId treasuryId = AccountService.treasuryId;

			/////////////////////////////////////////////////////
			// OPTION 2: Use existing account
			/////////////////////////////////////////////////////

			AccountId accId = AccountId.fromString("0.0.34717281");
			// PrivateKey accKey =
			// PrivateKey.fromString("3030020100300706052b8104000a042204200ccad0888e6f4ffcea487faae34880fb81e62485d3530e8f6386d32893cc8f1a");

			/////////////////////////////////////////////////////
			// Get account info
			/////////////////////////////////////////////////////

			AccountInfo accountInfo = AccountService.getAccountInfo(AccountService.client, accId);
			System.out.println("accountInfo: " + accountInfo.toString() + '\n');

//		TokenId tokenRelationships = null;

			List<JSONToken> tokenDetails = new ArrayList<JSONToken>();

			List<JSONToken> products = new ArrayList<JSONToken>();

//		Map<TokenId, TokenRelationship> accountTokenInfoMap = accountInfo.tokenRelationships;
//		System.out.println("All tokens associated with this account:");
//        for (TokenId key: accountTokenInfoMap.keySet()) {
//        	json.put("tokenID", key);
//            System.out.println(key);
//	        }
//        System.out.println("");

			Map<TokenId, TokenRelationship> accountTokenInfoMap2 = accountInfo.tokenRelationships;
			System.out.println("All tokens details associated with this account:");
			String substrTokenId, substrTokenIdWithoutLabel, substrTokenSym = null;

			for (TokenRelationship value : accountTokenInfoMap2.values()) {
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

			/////////////////////////////////////////////////////
			// Servlet post response
			/////////////////////////////////////////////////////
			Object data =  "Running contract with contractId of " + cid.toString();
			request.setAttribute("data", data);

//		request.setAttribute("accountInfo", accountInfo);
			request.setAttribute("treasuryId", treasuryId);
			request.setAttribute("accId", accId);
			request.setAttribute("products", products);
			request.setAttribute("tokenDetails", tokenDetails);
	 
			
			request.getRequestDispatcher("hedera/HederaMain.jsp").forward(request, response);

		} catch (Exception e) {
			System.out.println(e);
		}
	}
}