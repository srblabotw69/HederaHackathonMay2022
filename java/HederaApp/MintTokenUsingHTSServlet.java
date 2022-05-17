package HederaApp;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.io.IOException;
import java.nio.charset.Charset;

import com.hedera.hashgraph.sdk.AccountId;
import com.hedera.hashgraph.sdk.AccountInfo;
import com.hedera.hashgraph.sdk.ContractFunctionParameters;
import com.hedera.hashgraph.sdk.ContractId;
import com.hedera.hashgraph.sdk.FileId;
import com.hedera.hashgraph.sdk.PrivateKey;
import com.hedera.hashgraph.sdk.TokenId;
import com.hedera.hashgraph.sdk.TokenRelationship;
import com.hedera.hashgraph.sdk.TokenType;

import io.github.cdimascio.dotenv.Dotenv;

import Hedera.AccountService;
import Hedera.FileHandling;
import Hedera.FileService;
import Hedera.JSONToken;
import Hedera.SmartContractService;
import Hedera.TokenService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/MintTokenUsingHTSServlet")
public class MintTokenUsingHTSServlet extends HttpServlet {

	final private static String binFileAbsPath = System.getProperty("user.dir")
			+ Objects.requireNonNull(Dotenv.load().get("SOURCE_CODE_PACKAGE_PATH"));
	final private static String filename = Objects.requireNonNull(Dotenv.load().get("CONTRACT_BIN_FILENAME_HELLO"));

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
			PrivateKey accKey = PrivateKey.fromString(
					"3030020100300706052b8104000a042204200ccad0888e6f4ffcea487faae34880fb81e62485d3530e8f6386d32893cc8f1a");

			/////////////////////////////////////////////////////
			// Get account info
			/////////////////////////////////////////////////////

			AccountInfo accountInfo = AccountService.getAccountInfo(AccountService.client, accId);
			System.out.println("token accountInfo: " + accountInfo.toString() + '\n');

			/////////////////////////////////////////////////////
			// Split file max 1024KB
			/////////////////////////////////////////////////////

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
//			
//			//FileId fid = fservice.fileId;
//			FileId fid = FileId.fromString("0.0.34804691");
//
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
			// Create Fungible Token
			/////////////////////////////////////////////////////

			TokenService tokenservice = new TokenService();
			String tokenSymbol = givenUsingPlainJava_whenGeneratingRandomStringUnbounded_thenCorrect();

			tokenservice.createToken(AccountService.client, AccountService.treasuryKey, AccountService.treasuryKey,
					AccountService.treasuryId, "myTokenName", tokenSymbol, TokenType.FUNGIBLE_COMMON);
			TokenId tid = tokenservice.tokenId;

			/////////////////////////////////////////////////////
			// Call smart contract and pass in the token id in
			// solidity format
			/////////////////////////////////////////////////////

//			SmartContractService smtContract = new SmartContractService();
//			smtContract.createSmartContract(AccountService.client, fid,
//					new ContractFunctionParameters().addAddress(tokenservice.tokenId.toSolidityAddress()));
//			ContractId cid = smtContract.contractId;

			ContractId cid = ContractId.fromString("0.0.34804693");

			/////////////////////////////////////////////////////
			// Token Associate
			/////////////////////////////////////////////////////

			tokenservice.associateToken(AccountService.client, accKey, accId, cid, tid);

			/////////////////////////////////////////////////////
			// Get the tokenAssociate Transaction Record
			/////////////////////////////////////////////////////

			tokenservice.getTokenAssociateTransactionRecord(AccountService.client, AccountService.treasuryId);

			/////////////////////////////////////////////////////
			// Call the tokenTransfer Contract Function
			/////////////////////////////////////////////////////

// 			tokenservice.tokenTransfer(AccountService.client, AccountService.treasuryId, acctService.accountId, -10, 10);

			long numOfToken = 1;
			tokenservice.tokenTransfer(AccountService.client, AccountService.treasuryKey, AccountService.treasuryId,
					accId, tid, cid, numOfToken);

			/////////////////////////////////////////////////////
			// Get account token info
			/////////////////////////////////////////////////////
			
			accountInfo = AccountService.getAccountInfo(AccountService.client, accId);
			
			
			
//			TokenId tokenRelationships = null;

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
			Object data = "You have minted a token " + tid.toString() + " using HTS";
			request.setAttribute("data", data);

//			request.setAttribute("accountInfo", accountInfo);
			request.setAttribute("treasuryId", treasuryId);
			request.setAttribute("accId", accId);
			request.setAttribute("tokenDetails", tokenDetails);
			request.setAttribute("products", products);
			
			
			int randomNum = new Random().nextInt(11);
			request.setAttribute("randomNum", randomNum);
			
	
			request.getRequestDispatcher("hedera/HederaMain.jsp").forward(request, response);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
     * Generate random string to use as token symbol name
     */
	public static String givenUsingPlainJava_whenGeneratingRandomStringUnbounded_thenCorrect() {
	    byte[] array = new byte[7]; // length is bounded by 7
	    new Random().nextBytes(array);
	    String generatedString = new String(array, Charset.forName("UTF-8"));

	    System.out.println(generatedString);
	    return generatedString;
	}

}