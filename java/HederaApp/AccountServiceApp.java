package HederaApp;

import java.io.File;
import java.util.Objects;

import com.google.protobuf.ByteString;
import com.hedera.hashgraph.sdk.AccountId;
import com.hedera.hashgraph.sdk.AccountInfo;
import com.hedera.hashgraph.sdk.ContractFunctionParameters;
import com.hedera.hashgraph.sdk.FileContentsQuery;
import com.hedera.hashgraph.sdk.FileId;

import io.github.cdimascio.dotenv.Dotenv;

import Hedera.AccountService;
import Hedera.FileHandling;
import Hedera.FileService;
import Hedera.SmartContractService;

public class AccountServiceApp {

	final private static String binFileAbsPath = System.getProperty("user.dir")
			+ Objects.requireNonNull(Dotenv.load().get("SOURCE_CODE_PACKAGE_PATH"));
	final private static String filename = Objects.requireNonNull(Dotenv.load().get("CONTRACT_BIN_FILENAME_HELLO"));

	public static void main(String[] args) {
		try {

			/////////////////////////////////////////////////////
			// Connect to DeveloperPortal account
			/////////////////////////////////////////////////////
			
			AccountService.connectToDeveloperPortal();
			
			AccountService acctService = new AccountService();
			
//			acctService.createAccount();
//			The new account ID is: 0.0.34717281
//			The new account private key is: 3030020100300706052b8104000a042204200ccad0888e6f4ffcea487faae34880fb81e62485d3530e8f6386d32893cc8f1a
//			The smart contract bytecode file ID is 0.0.34717282
			
//			The new account ID is: 0.0.34717398
//			The smart contract bytecode file ID is 0.0.34717399
			
			
//			AccountId ai = AccountId.fromString("0.0.34717281");
//			PrivateKey pk = PrivateKey.fromString("3030020100300706052b8104000a042204200ccad0888e6f4ffcea487faae34880fb81e62485d3530e8f6386d32893cc8f1a");
//			
// 			//add accountId to Hedera file service
 			FileService fs = new FileService();
//			fs.addFileToHedera(ai.toString(), pk, AccountService.client);

			// The smart contract bytecode file ID is 0.0.34720668
			FileId fid = FileId.fromString("0.0.34720715");
			System.out.println("");

			String fileContent = fs.getFileContent(AccountService.client, fid); //check file content
		
			System.exit(0);
			
			
			
// 			AccountId ai = AccountId.fromString(accId);
// 			AccountInfo accountInfo = AccountService.getAccountInfo(AccountService.client, ai);
// 			AccountInfo accountInfo = AccountService.getAccountInfo(AccountService.client, AccountId.fromString(accId));
//			System.out.println(accountInfo.toString() + '\n');
			
			
//			System.out.println("newAccountId:");
// 			System.out.println(acctService.accountId.toString() + '\n');
			
	
			
			//0.0.34720714
			//The smart contract bytecode file ID is 0.0.34720715
  			
			//////////////////////////////////////
			
 			//Save accountId to file
			
			//option 1
 			byte[] byteAccountId = acctService.accountId.toBytes();
 			
 			//option 2
//			AccountId accountId = AccountId.fromString("0.0.34717398");
//			byte[] byteAccountId = accountId.toBytes();
			
// 			System.out.println("byteAccountId:");
// 			System.out.println(byteAccountId.toString() + '\n');

			//////////////////////////////////////
 			
 			//add accountId to Hedera file service
// 			FileService fs = new FileService();
 			
// 			fs.addFileToHedera(byteAccountId, acctService.accountKey, AccountService.client);
// 			fs.addFileToHedera("0.0.34717398", acctService.accountKey, AccountService.client);
 			fs.addFileToHedera(byteAccountId, AccountService.treasuryKey, AccountService.client);

 			
 			//option 1
 			fs.getFileContent(AccountService.client, fs.fileId); //check file content
// 			
 	 
//			
// 			System.out.println("convertedAccountId:");
// 		    System.out.println(AccountId.fromBytes(byteAccountId));
 		        
 			//option 2
 			fs.getFileContent(AccountService.client, FileId.fromString("0.0.34717398"));	

 			//option 3
			FileContentsQuery query = new FileContentsQuery().setFileId(FileId.fromString("0.0.34717398"));
			ByteString contents = query.execute(AccountService.client);
			System.out.println(contents);
//			String contentsToUtf8 = contents.toStringUtf8();
			
			
 			
 
// 			AccountId accountIdFromByte = AccountId.fromBytes("\030\341\374\306\020".getBytes());
//			String accountIdString = accountIdFromByte.toString();
//			System.out.println(accountIdString);
 
//			AccountInfo accountInfo = AccountService.getAccountInfo(AccountService.client, accountIdFromByte);
//			System.out.println(accountInfo.toString() + '\n');
//			

			
			 
// 			AccountId accountIdFromByte = AccountId.fromBytes("\030\341\374\306\020".getBytes());
//			String accountIdString = accountIdFromByte.toString();
//			System.out.println(accountIdString);
 
//			AccountInfo accountInfo = AccountService.getAccountInfo(AccountService.client, accountIdFromByte);
//			System.out.println(accountInfo.toString() + '\n');
			
 			
			//////////////////////////////////////		
 			
//			AccountInfo accountInfo = AccountService.getAccountInfo(AccountService.client, AccountService.treasuryId);
//			System.out.println(accountInfo.toString() + '\n');
				
 			
 			
//			System.out.println("accountKey:");
//			System.out.println(acctService.accountKey);
//			System.out.println("accountId:");
//			System.out.println(acctService.accountId.toString() + '\n');
 

			System.exit(1);

			/////////////////////////////////////////////////////
			// Create an account
			/////////////////////////////////////////////////////

//			AccountService acctService = new AccountService();
//			acctService.createAccount();

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
			fservice.addFileToHedera(bytecode, acctService.accountKey, AccountService.client);

			// append to file. file002, file003, etc...
			String fileSuffix = "";
			for (int i = 2; i <= numOfFiles; i++) {
				fileSuffix = "." + String.format("%03d", i);
				System.out.println("Get file for appending:");
				System.out.println(file.getName() + fileSuffix);

				bytecode = fservice.getByteCodeFromFile(file.getAbsoluteFile() + fileSuffix);
				fservice.appendFileToHedera(bytecode, AccountService.client, acctService.accountKey, fservice.fileId);
			}

			// show Hedera file content
			System.out.println("show Hedera file content after append:");
			fservice.getFileContent(AccountService.client, fservice.fileId);

			/////////////////////////////////////////////////////
			// Create smart contract
			/////////////////////////////////////////////////////

			SmartContractService smtContract = new SmartContractService();
			smtContract.createSmartContract(AccountService.client, fservice.fileId,
					new ContractFunctionParameters().addString("Hello from Hedera!"));

			/////////////////////////////////////////////////////
			// Call smart contract
			/////////////////////////////////////////////////////

			smtContract.callSmartContractFunction_HelloApp(AccountService.client, smtContract.contractId);

			/////////////////////////////////////////////////////
			// Query smart contract Info
			/////////////////////////////////////////////////////
			smtContract.getSmartContractInfo(AccountService.client, smtContract.contractId);

		} catch (Exception e) {
			System.out.println(e);
		}
	}
}