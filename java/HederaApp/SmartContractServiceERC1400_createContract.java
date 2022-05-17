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

public class SmartContractServiceERC1400_createContract {

	final private static String binFileAbsPath = System.getProperty("user.dir")
			+ Objects.requireNonNull(Dotenv.load().get("SOURCE_CODE_PACKAGE_HTS_ERC1400"));
	final private static String filename = Objects.requireNonNull(Dotenv.load().get("CONTRACT_BIN_FILENAME_ERC1400"));

	//public SmartContractServiceERC1400_createContract(String fileHash) {
	public SmartContractServiceERC1400_createContract(byte[] fileHash) {
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
 
			//System.exit(0);
			//ContractId cid = ContractId.fromString("0.0.34752922");
 			
 
			ContractId cid = smtContract.contractId;
				
 			
			/////////////////////////////////////////////////////
			// Call smart contract
			/////////////////////////////////////////////////////

 			//smtContract.callSmartContractFunction_ERC1400v2_setDocument(AccountService.client, cid);
 			smtContract.callSmartContractFunction_ERC1400_setDocument(AccountService.client, cid, fileHash);

 			
			smtContract.callSmartContractFunction_ERC1400_getDocument(AccountService.client, cid);

			/////////////////////////////////////////////////////
			// Query smart contract Info
			/////////////////////////////////////////////////////
			
			smtContract.getSmartContractInfo(AccountService.client, cid);
				

		} catch (Exception e) {
 			e.printStackTrace();
		}
	}
}