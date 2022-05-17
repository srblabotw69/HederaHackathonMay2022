package HederaApp;

import java.io.File;
import java.util.Objects;
import java.io.IOException;

import com.hedera.hashgraph.sdk.AccountId;
import com.hedera.hashgraph.sdk.ContractFunctionParameters;
//import com.hedera.hashgraph.sdk.TokenType;
import com.hedera.hashgraph.sdk.PrivateKey;

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
 
@WebServlet("/SmartContractServiceERC1400v2Servlet")
public class SmartContractServiceERC1400v2Servlet extends HttpServlet {

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

			AccountId accId = AccountId.fromString("0.0.34717281");
			PrivateKey accKey = PrivateKey.fromString("3030020100300706052b8104000a042204200ccad0888e6f4ffcea487faae34880fb81e62485d3530e8f6386d32893cc8f1a");
		
			/////////////////////////////////////////////////////
			// Split file to max 1024KB
			/////////////////////////////////////////////////////

			File file = new File(binFileAbsPath + filename);

			System.out.println("contract binary filename: " + file.getName());
			System.out.println("absolute filepath: " + file.getAbsolutePath() + '\n');

			// split into 1024KB files
			int numOfFiles = FileHandling.splitFile(file);

			/////////////////////////////////////////////////////
			// Add file to Hedera
			/////////////////////////////////////////////////////

			FileService fservice = new FileService();
			File file001 = new File(file.getAbsoluteFile() + ".001");
			byte[] bytecode = fservice.getByteCodeFromFile(file001.getAbsolutePath());

			// add file on hedera. file001
			fservice.addFileToHedera(bytecode, accKey, AccountService.client);

			// append to file. file002, file003, etc...
			String fileSuffix = "";
			for (int i = 2; i <= numOfFiles; i++) {
				fileSuffix = "." + String.format("%03d", i);
				System.out.println("Get file for appending:");
				System.out.println(file.getName() + fileSuffix);

				bytecode = fservice.getByteCodeFromFile(file.getAbsoluteFile() + fileSuffix);
				fservice.appendFileToHedera(bytecode, AccountService.client, accKey, fservice.fileId);
			}

			// show Hedera file content
			System.out.println("show Hedera file content after append:");
			fservice.getFileContent(AccountService.client, fservice.fileId);

			/////////////////////////////////////////////////////
			// Create smart contract
			/////////////////////////////////////////////////////

			SmartContractService smtContract = new SmartContractService();
			smtContract.createSmartContract(AccountService.client, fservice.fileId,
					new ContractFunctionParameters());

			/////////////////////////////////////////////////////
			// Call smart contract
			/////////////////////////////////////////////////////

			smtContract.callSmartContractFunction_ERC1400v2_setDocument(AccountService.client, smtContract.contractId);

			smtContract.callSmartContractFunction_ERC1400v2_getDocument(AccountService.client, smtContract.contractId);

			/////////////////////////////////////////////////////
			// Query smart contract Info
			/////////////////////////////////////////////////////
			smtContract.getSmartContractInfo(AccountService.client, smtContract.contractId);
			
			/////////////////////////////////////////////////////
			// Servlet post response
			/////////////////////////////////////////////////////		
			Object data = "You have called the Hello Smart Contract";
			request.setAttribute("data", data);

			request.getRequestDispatcher("hedera/HederaMain.jsp").forward(request, response);
			request.setAttribute("data", data);
			

		} catch (Exception e) {
			System.out.println(e);
		}
	}
	
//	protected void getMintTokenUsingHTS() throws Exception {
//		try {
//
//			/////////////////////////////////////////////////////
//			// Connect to DeveloperPortal account
//			/////////////////////////////////////////////////////
//
//			AccountService.connectToDeveloperPortal();
//
//			/////////////////////////////////////////////////////
//			// Create an account
//			/////////////////////////////////////////////////////
//
//			AccountService acctService = new AccountService();
//			acctService.createAccount();
//
//			/////////////////////////////////////////////////////
//			// Split file max 1024KB
//			/////////////////////////////////////////////////////
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
//			fservice.addFileToHedera(bytecode, acctService.accountKey, AccountService.client);
//
//			// append to file. file002, file003, etc...
//			String fileSuffix = "";
//			for (int i = 2; i <= numOfFiles; i++) {
//				fileSuffix = "." + String.format("%03d", i);
//				System.out.println("Get file for appending:");
//				System.out.println(file.getName() + fileSuffix);
//
//				bytecode = fservice.getByteCodeFromFile(file.getAbsoluteFile() + fileSuffix);
//				fservice.appendFileToHedera(bytecode, AccountService.client, acctService.accountKey, fservice.fileId);
//			}
//
//			// show Hedera file content
//			System.out.println("show Hedera file content after append:");
//			fservice.getFileContent(AccountService.client, fservice.fileId);
//
//			/////////////////////////////////////////////////////
//			// Create Fungible Token
//			/////////////////////////////////////////////////////
//
//			TokenService tokenservice = new TokenService();
//			tokenservice.createToken(bytecode, AccountService.client, AccountService.treasuryKey,
//					AccountService.treasuryKey, AccountService.treasuryId, "myTokenName", "myTokenSymbol",
//					TokenType.FUNGIBLE_COMMON);
//
//			/////////////////////////////////////////////////////
//			// Call smart contract and pass in the token id in
//			// solidity format
//			/////////////////////////////////////////////////////
//
//			SmartContractService smtContract = new SmartContractService();
//			smtContract.createSmartContract(AccountService.client, fservice.fileId,
//					new ContractFunctionParameters().addAddress(tokenservice.tokenId.toSolidityAddress()));
//
//			/////////////////////////////////////////////////////
//			// Token Associate
//			/////////////////////////////////////////////////////
//
//			tokenservice.associateToken(AccountService.client, acctService.accountKey, acctService.accountId,
//					smtContract.contractId, tokenservice.tokenId);
//
//			/////////////////////////////////////////////////////
//			// Get the tokenAssociate Transaction Record
//			/////////////////////////////////////////////////////
//
//			tokenservice.getTokenAssociateTransactionRecord(AccountService.client, AccountService.treasuryId);
//
//			/////////////////////////////////////////////////////
//			// Call the tokenTransfer Contract Function
//			/////////////////////////////////////////////////////
//
//// 			tokenservice.tokenTransfer(AccountService.client, AccountService.treasuryId, acctService.accountId, -10, 10);
//
//			long numOfToken = 1;
//			tokenservice.tokenTransfer(AccountService.client, AccountService.treasuryKey, AccountService.treasuryId,
//					acctService.accountId, tokenservice.tokenId, smtContract.contractId, numOfToken);
//
////			/////////////////////////////////////////////////////
////			// Token dissociate NOT USED AT MOMMENT!!!!!!!!!!!
////			/////////////////////////////////////////////////////
////			
////			
////			List<TokenId> tokenList = new ArrayList<TokenId>();
////			for(int i=0;i<tokenList.size();i++){
////			    System.out.println("tokens associated with this account: " + '\n' + tokenList.get(i) + '\n');
////			} 
////			
////			tokenList.add(tokenservice.tokenId);
////			tokenservice.disAssociateToken(HTCToken.client, HTCToken.accountKey, HTCToken.accountId, tokenList);
////			
//
//		} catch (Exception e) {
//			System.out.println(e);
//		}
//	}
}