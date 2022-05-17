package Hedera;

import java.nio.charset.StandardCharsets;
import java.io.*;

import com.google.protobuf.ByteString;
import com.hedera.hashgraph.sdk.*;
 
public class FileService {

	public FileId fileId;
	
	/**
	 * Import the compiled solidity contract GLDToken.sol
	 */
	public void addFileToHedera(byte[] bytecode, PrivateKey fileKey, Client client) {
		try {
			
			System.out.println("FileService.AddFileToHedera():");
 
			FileCreateTransaction transaction = new FileCreateTransaction().setKeys(fileKey).setContents(bytecode);

			// Change the default max transaction fee to 2 hbars
			FileCreateTransaction modifyMaxTransactionFee = transaction.setMaxTransactionFee(new Hbar(2));

			// Prepare transaction for signing, sign with the key on the file, sign with the
			// client operator key and submit to a Hedera network
			TransactionResponse txResponse = modifyMaxTransactionFee.freezeWith(client).sign(fileKey).execute(client);

			// Request the receipt
			TransactionReceipt receipt = txResponse.getReceipt(client);

			// Get the file ID from the receipt
			FileId bytecodeFileId = receipt.fileId;
			

			// Log the file ID
			System.out.println("The transaction status is " + receipt.status + '\n');


			// Log the file ID
			System.out.println("The smart contract bytecode file ID is " + bytecodeFileId + '\n');

			this.fileId = bytecodeFileId;

		} catch (Exception e) {
			System.out.println(e);
		}
	}
	
	/**
	 * Import the compiled solidity contract GLDToken.sol
	 */
	public void addFileToHedera(String str, PrivateKey fileKey, Client client) {
		try {
			
			System.out.println("FileService.AddFileToHedera():");
 
			FileCreateTransaction transaction = new FileCreateTransaction().setKeys(fileKey).setContents(str);

			// Change the default max transaction fee to 2 hbars
			FileCreateTransaction modifyMaxTransactionFee = transaction.setMaxTransactionFee(new Hbar(2));

			// Prepare transaction for signing, sign with the key on the file, sign with the
			// client operator key and submit to a Hedera network
			TransactionResponse txResponse = modifyMaxTransactionFee.freezeWith(client).sign(fileKey).execute(client);

			// Request the receipt
			TransactionReceipt receipt = txResponse.getReceipt(client);

			// Get the file ID from the receipt
			FileId bytecodeFileId = receipt.fileId;
			

			// Log the file ID
			System.out.println("The transaction status is " + receipt.status + '\n');


			// Log the file ID
			System.out.println("The smart contract bytecode file ID is " + bytecodeFileId + '\n');

			this.fileId = bytecodeFileId;

		} catch (Exception e) {
			System.out.println(e);
		}
	}
	
	/**
	 * Append to file
	 */
	public void appendFileToHedera(byte[] bytecode, Client client, PrivateKey accountkey, FileId fileId) {
		try {
			System.out.println("FileService.appendFileToHedera():");
			System.out.println("Appending fileId: " + fileId.toString());
			
			// Create the transaction
			FileAppendTransaction transaction = new FileAppendTransaction().setFileId(fileId).setContents(bytecode);

			// Change the default max transaction fee to 2 hbars
			FileAppendTransaction modifyMaxTransactionFee = transaction.setMaxTransactionFee(new Hbar(2));

			// Prepare transaction for signing, sign with the key on the file, sign with the
			// client operator key and submit to a Hedera network
			TransactionResponse txResponse = modifyMaxTransactionFee.freezeWith(client).sign(accountkey).execute(client);

			// Request the receipt
			TransactionReceipt receipt = txResponse.getReceipt(client);

			// Get the transaction consensus status
			Status transactionStatus = receipt.status;

			System.out.println("The transaction consensus status is " + transactionStatus + '\n');

		} catch (Exception e) {
			System.out.println(e);
		}
	}

	/**
	 * Import the compiled solidity contract GLDToken.sol
	 */
	public void deleteAccount(AccountId accountId, Client client, PrivateKey newKey, AccountId OPERATOR_ID) {
		try {
			System.out.println("FileService.deleteAccount()");

			// Create the transaction to delete an account
			AccountDeleteTransaction transaction = new AccountDeleteTransaction().setAccountId(accountId)
					.setTransferAccountId(OPERATOR_ID);

			// Freeze the transaction for signing, sign with the private key of the account
			// that will be deleted, sign with the operator key and submit to a Hedera
			// network
			TransactionResponse txResponse = transaction.freezeWith(client).sign(newKey).execute(client);

			// Request the receipt of the transaction
			TransactionReceipt receipt = txResponse.getReceipt(client);

			// Get the transaction consensus status
			Status transactionStatus = receipt.status;

			System.out.println("The transaction consensus status is " + transactionStatus);

		} catch (Exception e) {
			System.out.println(e);
		}
	}

	/**
	 * Query Account Info
	 */
	public static void getAccountInfo(Client client, AccountId accountId) throws Exception {
		try {
			// Create the account info query
			AccountInfoQuery query = new AccountInfoQuery().setAccountId(accountId);

			// Submit the query to a Hedera network
			AccountInfo accountInfo = query.execute(client);

			// Print the account key to the console
			System.out.println(accountInfo.toString() + '\n');

		} catch (Exception e) {
			System.out.println(e);
		}
	}

	/**
	 * Import the compiled solidity contract GLDToken.sol
	 */
	public byte[] getByteCodeFromFile(String filename) {
		String val = "";
		byte[] bytecode = new byte[(int) filename.length()];

		try {
			// InputStream inputstream = FileService.class.getResourceAsStream(filename);
			InputStream inputstream = new FileInputStream(filename);
			BufferedReader bufreader = new BufferedReader(new InputStreamReader(inputstream));

			// reads each line
			String l;
			while ((l = bufreader.readLine()) != null) {
				val = val + l;
				System.out.println("FileService.GetByteCodeFromFile() using file:");
				System.out.println(filename);
				System.out.println("Bytecode content in file: ");
				System.out.println(val + '\n');
			}

			// Store the "object" field from the HTS.json file as hex-encoded bytecode
			bytecode = val.getBytes(StandardCharsets.UTF_8);

			inputstream.close();

		} catch (Exception e) {
			System.out.println(e);
		}
		return bytecode;
	}

	/**
	 * get file content
	 * @return 
	 */
	public String getFileContent(Client client, FileId fileId) {
		String fileContent = null;
		try {
			System.out.println("FileService.getFileContent():");
			
			// Create the query
			FileContentsQuery query = new FileContentsQuery().setFileId(fileId);

			// Sign with client operator private key and submit the query to a Hedera
			// network
			ByteString contents = query.execute(client);
 		
			System.out.println(contents.toString());

			// Change to Utf-8 encoding
			String contentsToUtf8 = contents.toStringUtf8();
			fileContent = contentsToUtf8;
			
			System.out.println(contentsToUtf8.toString() + '\n');

		} catch (Exception e) {
			System.out.println(e);
		}
		return fileContent;
	}
}