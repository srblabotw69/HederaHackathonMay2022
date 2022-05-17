package HederaApp;

import java.io.File;
import java.nio.charset.Charset;
import java.util.Objects;
import java.util.Random;

import io.github.cdimascio.dotenv.Dotenv;

import com.hedera.hashgraph.sdk.AccountId;
import com.hedera.hashgraph.sdk.ContractFunctionParameters;
import com.hedera.hashgraph.sdk.ContractId;
import com.hedera.hashgraph.sdk.FileId;
import com.hedera.hashgraph.sdk.PrivateKey;
import com.hedera.hashgraph.sdk.TokenId;
import com.hedera.hashgraph.sdk.TokenType;

import Hedera.AccountService;
import Hedera.FileHandling;
import Hedera.FileService;
import Hedera.TokenService;
import Hedera.SmartContractService;

public class MintTokenUsingHTSApp {

	final private static String binFileAbsPath = System.getProperty("user.dir")
			+ Objects.requireNonNull(Dotenv.load().get("SOURCE_CODE_PACKAGE_HTS_PATH"));
	final private static String filename = Objects.requireNonNull(Dotenv.load().get("CONTRACT_BIN_FILENAME_MINT"));

	public static void main(String[] args) throws Exception {
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
			
//			String tokenSymbol = givenUsingPlainJava_whenGeneratingRandomStringUnbounded_thenCorrect();
			
			tokenservice.createToken(AccountService.client, AccountService.treasuryKey,
					AccountService.treasuryKey, AccountService.treasuryId, "myTokenName", "myTokenSymbol",
					TokenType.FUNGIBLE_COMMON);
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

			tokenservice.associateToken(AccountService.client, accKey, accId,
					cid, tid);

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

//			/////////////////////////////////////////////////////
//			// Token dissociate NOT USED AT MOMMENT!!!!!!!!!!!
//			/////////////////////////////////////////////////////
//			
//			
//			List<TokenId> tokenList = new ArrayList<TokenId>();
//			for(int i=0;i<tokenList.size();i++){
//			    System.out.println("tokens associated with this account: " + '\n' + tokenList.get(i) + '\n');
//			} 
//			
//			tokenList.add(tokenservice.tokenId);
//			tokenservice.disAssociateToken(HTCToken.client, HTCToken.accountKey, HTCToken.accountId, tokenList);
	

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