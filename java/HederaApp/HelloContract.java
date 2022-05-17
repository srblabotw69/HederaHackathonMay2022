package HederaApp;

import java.io.File;
import java.util.Objects;

import com.hedera.hashgraph.sdk.ContractFunctionParameters;

import io.github.cdimascio.dotenv.Dotenv;

import Hedera.AccountService;
import Hedera.FileHandling;
import Hedera.FileService;
import Hedera.SmartContractService;

public class HelloContract {

	final private static String binFileAbsPath = System.getProperty("user.dir")
			+ Objects.requireNonNull(Dotenv.load().get("SOURCE_CODE_PACKAGE_PATH"));
	final private static String filename = Objects.requireNonNull(Dotenv.load().get("CONTRACT_BIN_FILENAME_HELLO"));

	public static void main(String[] args) {
		try {

			/////////////////////////////////////////////////////
			// Connect to DeveloperPortal account
			/////////////////////////////////////////////////////

			AccountService.connectToDeveloperPortal();

			/////////////////////////////////////////////////////
			// Create an account
			/////////////////////////////////////////////////////

			AccountService acctService = new AccountService();
			acctService.createAccount();

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