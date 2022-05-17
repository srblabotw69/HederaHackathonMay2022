package HederaApp;

import java.io.File;
import java.util.Objects;

import com.google.protobuf.ByteString;
import com.hedera.hashgraph.sdk.AccountId;
import com.hedera.hashgraph.sdk.AccountInfo;
import com.hedera.hashgraph.sdk.ContractCreateTransaction;
import com.hedera.hashgraph.sdk.ContractExecuteTransaction;
import com.hedera.hashgraph.sdk.ContractFunctionParameters;
import com.hedera.hashgraph.sdk.ContractId;
import com.hedera.hashgraph.sdk.FileContentsQuery;
import com.hedera.hashgraph.sdk.FileId;
import com.hedera.hashgraph.sdk.PrivateKey;
import com.hedera.hashgraph.sdk.Status;
import com.hedera.hashgraph.sdk.TransactionReceipt;
import com.hedera.hashgraph.sdk.TransactionResponse;

import io.github.cdimascio.dotenv.Dotenv;

import Hedera.AccountService;
import Hedera.FileHandling;
import Hedera.FileService;
import Hedera.SmartContractService;

public class ERC1400UsingHTSApp {

	final private static String docPath = System.getProperty("user.dir")
			+ Objects.requireNonNull(Dotenv.load().get("DOCUMENTS_PATH"))
					+ Objects.requireNonNull(Dotenv.load().get("DOCUMENTS_FILE_ERC1400"));
			
	public static void main(String[] args) {
		try {
//			System.out.println("docPath: " + docPath.toString() + '\n');
//			File file  = new File(docPath);
//			String hashType = "SHA-256";
//			System.out.println("ERC1400UsingHTSApp.main(): " + file.toString());
// 
//			ERC1400UsingHTS  erc1400Tok = new ERC1400UsingHTS(file, hashType);
 		
			
			/////////////////////////////////////////////////////
			// Query smart contract Info
			/////////////////////////////////////////////////////
	 
			AccountService.connectToDeveloperPortal();
			SmartContractService smtContract = new SmartContractService();
			ContractId contractId = ContractId.fromString("0.0.34739052"); 
			smtContract.getSmartContractInfo(AccountService.client, contractId);
			
//			
//			//Create the transaction
//			ContractExecuteTransaction transaction = new ContractExecuteTransaction()
//			     .setContractId(contractId)
//			     .setGas(100_000_000)
//			     .setFunction("getDocument", new ContractFunctionParameters()
//			           .addString("hello from hedera again!"));
//
//			//Sign with the client operator private key to pay for the transaction and submit the query to a Hedera network
//			TransactionResponse txResponse = transaction.execute(AccountService.client);
//
//			//Request the receipt of the transaction
//			TransactionReceipt receipt = txResponse.getReceipt(AccountService.client);
//
//			//Get the transaction consensus status
//			Status transactionStatus = receipt.status;
//
//			System.out.println("The transaction consensus status is " +transactionStatus);
//
//			//v2.0.0
//			
			

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}