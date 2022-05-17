package HederaApp;

import java.io.File;
import java.util.Objects;

import io.github.cdimascio.dotenv.Dotenv;

import com.hedera.hashgraph.sdk.AccountId;
import com.hedera.hashgraph.sdk.ContractFunctionParameters;
import com.hedera.hashgraph.sdk.ContractId;
import com.hedera.hashgraph.sdk.PrivateKey;

import Hedera.AccountService;
import Hedera.FileHandling;
import Hedera.FileService;
import Hedera.SmartContractService;

public class SmartContractServiceERC1400App_callContract {

	final private static String binFileAbsPath = System.getProperty("user.dir")
			+ Objects.requireNonNull(Dotenv.load().get("SOURCE_CODE_PACKAGE_HTS_ERC1400"));
	final private static String filename = Objects.requireNonNull(Dotenv.load().get("CONTRACT_BIN_FILENAME_ERC1400"));

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
				 

//			ContractId cid = ContractId.fromString("0.0.34804061");
			
			
			ContractId cid = ContractId.fromString("0.0.34812348");
 			
			/////////////////////////////////////////////////////
			// Call smart contract
			/////////////////////////////////////////////////////

 
 			
 			smtContract.callSmartContractFunction_ERC1400_setDocument(AccountService.client, cid);


 		  // sc: conflicts with google cloud photobuf !!!
			smtContract.callSmartContractFunction_ERC1400_getDocument(AccountService.client, cid);

			
			/////////////////////////////////////////////////////
			// Query smart contract Info
			/////////////////////////////////////////////////////
			
 
 			// sc: conflicts with google cloud photobuf !!!
			smtContract.getSmartContractInfo(AccountService.client, cid);
				

		} catch (Exception e) {
 			e.printStackTrace();
		}
	}
}