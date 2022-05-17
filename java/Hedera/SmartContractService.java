package Hedera;

import org.web3j.abi.datatypes.generated.Bytes32;
import org.web3j.utils.Numeric;

//import org.web3j.abi.datatypes.generated.Bytes32;

import com.hedera.hashgraph.sdk.Client;
import com.hedera.hashgraph.sdk.ContractCallQuery;
import com.hedera.hashgraph.sdk.ContractCreateFlow;
import com.hedera.hashgraph.sdk.ContractCreateTransaction;
import com.hedera.hashgraph.sdk.ContractExecuteTransaction;
import com.hedera.hashgraph.sdk.ContractFunctionParameters;
import com.hedera.hashgraph.sdk.ContractFunctionResult;
import com.hedera.hashgraph.sdk.ContractId;
import com.hedera.hashgraph.sdk.ContractInfo;
import com.hedera.hashgraph.sdk.ContractInfoQuery;
import com.hedera.hashgraph.sdk.FileId;
import com.hedera.hashgraph.sdk.Hbar;
import com.hedera.hashgraph.sdk.Status;
import com.hedera.hashgraph.sdk.TransactionReceipt;
import com.hedera.hashgraph.sdk.TransactionResponse;

public class SmartContractService {
	
	public ContractId contractId;
	
	/**
	 *  Create a smart contract
	 */
	public void createSmartContract(Client client, FileId fileId, ContractFunctionParameters param) {
		ContractId newContractId = null;
		try {
			
			System.out.println("SmartContractService.CreateSmartContract():");
 			
			// Create the transaction
			ContractCreateTransaction contractTx = new ContractCreateTransaction()
					.setBytecodeFileId(fileId)
 					.setGas(100_000)
				    .setConstructorParameters(param);
				     //Provide the constructor parameters for the contract
//				    .setConstructorParameters(new ContractFunctionParameters().addString("Hello from Hedera!"));
 
//			ContractCreateTransaction modifyTransactionFee = contractTx.setMaxTransactionFee(new Hbar(10));
	 
			//Submit the transaction to the Hedera test network
			TransactionResponse contractResponse = contractTx.execute(client);
//			TransactionResponse contractResponse =
//					//.freezeWith(client)
//					modifyTransactionFee.execute(client);
		 
			// Get the receipt of the transaction
			TransactionReceipt receipt = contractResponse.getReceipt(client);

			//Get the new contract ID
			newContractId = receipt.contractId;
 
			this.contractId = newContractId;
			
			//Log the smart contract ID
			System.out.println("The smart contract Id is " + newContractId);
			System.out.println("The smart contract Id in Solidity format is " + newContractId.toSolidityAddress() + '\n');

		} catch (Exception e) {
			e.printStackTrace();
		}
//		return newContractId;
	}

	/**
	 *  Create smart contract info Using CreateContractFlow
	 */
	public void createGetSmartContractUsingCreateContractFlow(byte[] bytecode, Client client, ContractFunctionParameters param) {
 		try {			
 			System.out.println("SmartContractService.createGetSmartContractUsingCreateContractFlow():");
 			
 			//Create the transaction
 			ContractCreateFlow contractCreate = new ContractCreateFlow()
 	 			 .setBytecode(bytecode)
 			     .setGas(100_000)
 			     .setConstructorParameters(param);

 			//Sign the transaction with the client operator key and submit to a Hedera network
 			TransactionResponse txResponse = contractCreate.execute(client);

 			//Get the receipt of the transaction
 			TransactionReceipt receipt = txResponse.getReceipt(client);

 			//Get the new contract ID
 			ContractId newContractId = receipt.contractId;
 			
 			this.contractId = newContractId;
 			        
 			System.out.println("The new contract ID is " + newContractId + '\n');
 			
		} catch (Exception e) {
			System.out.println(e);
		}
	}
	
	/**
	 *  Call smart contract Function
	 */
	public void callSmartContractFunction_HelloApp(Client client, ContractId newContractId) {
		try {			
			System.out.println("SmartContractService.callSmartContractFunction_HelloApp():");
 	
			//Create the transaction
			ContractExecuteTransaction transaction = new ContractExecuteTransaction()
					.setContractId(newContractId)
					.setGas(1_000_000)
//					.setMaxTransactionFee(new Hbar(2))
					.setFunction("set_message", 
						new ContractFunctionParameters()
						.addString("hello from hedera again!"));

			//Sign with the client operator private key to pay for the transaction and submit the query to a Hedera network
			TransactionResponse txResponse = transaction.execute(client);

			//Request the receipt of the transaction
			TransactionReceipt receipt = txResponse.getReceipt(client);

			//Get the transaction consensus status
			Status transactionStatus = receipt.status;

			System.out.println("The transaction consensus status is " + transactionStatus + '\n');
 
		} catch (Exception e) {
			System.out.println(e);
		}
	}
	
	/**
	 *  Call smart contract Function
	 */
	public void callSmartContractFunction_ERC1400v2_setDocument(Client client, ContractId newContractId) { //, ContractFunctionParameters param) {
		try {			
			System.out.println("SmartContractService.callSmartContractFunction_ERC1400v2_setDocument():");
 		
	 		byte[] _name = "name".getBytes();
//			Bytes32 _name = SmartContractService.stringToBytes32("name");
		 
	 		String _uri = "test/uri";
	 		
	 		byte[] _documentHash = "documentHash".getBytes();	
//	 		Bytes32 _documentHash = SmartContractService.stringToBytes32("documentHash");
 
			//Create the transaction
			ContractExecuteTransaction transaction = new ContractExecuteTransaction()
					.setContractId(newContractId)
					.setGas(1_000_000)
//					.setMaxTransactionFee(new Hbar(2))
					.setFunction("setDocument", 
							new ContractFunctionParameters()
							.addBytes32(_name)
							.addString(_uri)
							.addBytes32(_documentHash));
			
			//Sign with the client operator private key to pay for the transaction and submit the query to a Hedera network
			TransactionResponse txResponse = transaction.execute(client);

			//Request the receipt of the transaction
			TransactionReceipt receipt = txResponse.getReceipt(client);

			//Get the transaction consensus status
			Status transactionStatus = receipt.status;

			System.out.println("The transaction consensus status is " + transactionStatus);
			
			
			//////////////////
			
			//Query the contract for the contract message
			 ContractCallQuery contractCallQuery = new ContractCallQuery()
			        //Set ID of the contract to query
			        .setContractId(newContractId)
			        //Set the gas to execute the contract call
			        .setGas(100_000)
			        //Set the contract function
			        .setFunction("getDocument", 
			        		new ContractFunctionParameters().addBytes32(_name))
			        //Set the query payment for the node returning the request
			        //This value must cover the cost of the request otherwise will fail 
			        .setQueryPayment(new Hbar(2));
			 
			 //Submit the query to a Hedera network
			ContractFunctionResult contractUpdateResult = contractCallQuery.execute(client);

			//Get the updated message
			String message2 = contractUpdateResult.getString(0);

			//Log the updated message
			System.out.println("The contract updated message: " + message2 + '\n');
 
		} catch (Exception e) {
			System.out.println(e);
		}
	}
	
	
	/**
	 *  Call smart contract Function
	 */
	public void callSmartContractFunction_ERC1400_setDocument(Client client, ContractId newContractId, byte[] fileHash) { //, ContractFunctionParameters param) {
		try {			
			System.out.println("SmartContractService.callSmartContractFunction_ERC1400_setDocument():");
			System.out.println(fileHash);
	 		byte[] _name = "name".getBytes();
			System.out.println(_name); 
			System.out.println(_name.toString()); 
//			Bytes32 _name = SmartContractService.stringToBytes32("name");
		 
			
			Bytes32 myStringInBytes32 = new Bytes32(fileHash);
			
			
	 		String _uri = "test2/uri";
	 		//String _uri = fileHash;
	 		
	 		//byte[] _documentHash = "documentHash".getBytes();	
	 		
	 		//fileHash.getBytes();
	 		//byte[] _documentHash = Numeric.hexStringToByteArray(fileHash);
	 		byte[] _documentHash =  fileHash;

			//byte[] _documentHash = "fileHash".getBytes();	
			System.out.println(_documentHash.toString()); 
//	 		Bytes32 _documentHash = SmartContractService.stringToBytes32("documentHash");
 
 
			//Create the transaction
			ContractExecuteTransaction transaction = new ContractExecuteTransaction()
					.setContractId(newContractId)
					.setGas(1_000_000)
//					.setMaxTransactionFee(new Hbar(2))
					.setFunction("setDocument", 
							new ContractFunctionParameters()
							.addBytes32(_name)
							.addString(_uri)
							.addBytes32(_documentHash));
			
			//Sign with the client operator private key to pay for the transaction and submit the query to a Hedera network
			TransactionResponse txResponse = transaction.execute(client);

			//Request the receipt of the transaction
			TransactionReceipt receipt = txResponse.getReceipt(client);

			//Get the transaction consensus status
			Status transactionStatus = receipt.status;

			System.out.println("The transaction consensus status is " + transactionStatus);
			
			
			//////////////////
			
			//Query the contract for the contract message
			 ContractCallQuery contractCallQuery = new ContractCallQuery()
			        //Set ID of the contract to query
			        .setContractId(newContractId)
			        //Set the gas to execute the contract call
			        .setGas(100_000)
			        //Set the contract function
			        .setFunction("getDocument", 
			        		new ContractFunctionParameters().addBytes32(_name))
			        //Set the query payment for the node returning the request
			        //This value must cover the cost of the request otherwise will fail 
			        .setQueryPayment(new Hbar(2));
			 
			 
			 //Submit the query to a Hedera network
			ContractFunctionResult contractUpdateResult = contractCallQuery.execute(client);

			System.out.println("*******************************************");
 
			//Get the updated message
			//String message2 = contractUpdateResult.getString(0);
			
			byte[] messageByte1 = contractUpdateResult.getBytes32(0);
			System.out.println("The contract updated messageByte1: " + bytesToHex(messageByte1).toString());
			
			byte[] messageByte2 = contractUpdateResult.getBytes32(1);
			System.out.println("The contract updated messageByte2: " +  bytesToHex(messageByte2).toString() + '\n');
			
			
 
 
 
		} catch (Exception e) {
			System.out.println(e);
		}
	}
	/**
	 *  Call smart contract Function
	 */
	public void callSmartContractFunction_ERC1400v2_getDocument(Client client, ContractId newContractId) { //, ContractFunctionParameters param) {
		try {			
			System.out.println("SmartContractService.callSmartContractFunction_ERC1400v2_getDocument():");
 		
	 		byte[] _name = "name".getBytes();
 
	 		// Calls a function of the smart contract
	 		ContractCallQuery contractQuery = new ContractCallQuery()
					.setContractId(newContractId)
					.setGas(1_000_000)
					.setQueryPayment(new Hbar(2))
					.setFunction("getDocument", 
							new ContractFunctionParameters()
							.addBytes32(_name));
			
	 		//Submit to a Hedera network
	 		ContractFunctionResult getMessage = contractQuery.execute(client);
	 		
	 		System.out.println(getMessage.toString());
	 		
	 		//Get the message
	 		String message = getMessage.getString(0); 
	 		
	 	    //Log the message
	 		System.out.println("The contract message: " + message + '\n');


		} catch (Exception e) {
			System.out.println(e);
		}
	}
	
	/**
	 *  Call smart contract Function
	 */
	public void callSmartContractFunction_ERC1400_setDocument(Client client, ContractId newContractId) { //, ContractFunctionParameters param) {
		try {			
			System.out.println("SmartContractService.callSmartContractFunction_ERC1400_setDocument():");
 		
	 		byte[] _name = "name".getBytes();
//			Bytes32 _name = SmartContractService.stringToBytes32("name");
		 
	 		String _uri = "test/uri";
	 		
	 		byte[] _documentHash = "documentHash".getBytes();	
//	 		Bytes32 _documentHash = SmartContractService.stringToBytes32("documentHash");
 
			//Create the transaction
			ContractExecuteTransaction transaction = new ContractExecuteTransaction()
					.setContractId(newContractId)
					.setGas(1_000_000)
//					.setMaxTransactionFee(new Hbar(2))
					.setFunction("setDocument", 
							new ContractFunctionParameters()
							.addBytes32(_name)
							.addString(_uri)
							.addBytes32(_documentHash));
			
			//Sign with the client operator private key to pay for the transaction and submit the query to a Hedera network
			TransactionResponse txResponse = transaction.execute(client);

			//Request the receipt of the transaction
			TransactionReceipt receipt = txResponse.getReceipt(client);

			//Get the transaction consensus status
			Status transactionStatus = receipt.status;

			System.out.println("The transaction consensus status is " + transactionStatus);
	 
//			//////////////////
//		
//			//SC: CONFLICT WITH GOOGLE CLOUD PLATFORM photobuf
//			
//			Exception in thread "main" java.lang.VerifyError: Bad type on operand stack
//			Exception Details:
//			  Location:
//			    com/hedera/hashgraph/sdk/proto/TokenTransferList.mergeExpectedDecimals(Lcom/google/protobuf/UInt32Value;)V @31: invokevirtual
//			  Reason:
//			    Type 'com/google/protobuf/UInt32Value' (current frame, stack[2]) is not assignable to 'com/google/protobuf/GeneratedMessageLite'
//			  Current Frame:
//			    bci: @31
//			    flags: { }
//			    locals: { 'com/hedera/hashgraph/sdk/proto/TokenTransferList', 'com/google/protobuf/UInt32Value' }
//			    stack: { 'com/hedera/hashgraph/sdk/proto/TokenTransferList', 'com/google/protobuf/UInt32Value$Builder', 'com/google/protobuf/UInt32Value' }
//			  Bytecode:
//				  
//			//////////////////
  

//			//Query the contract for the contract message
//			 ContractCallQuery contractCallQuery = new ContractCallQuery()
//			        //Set ID of the contract to query
//			        .setContractId(newContractId)
//			        //Set the gas to execute the contract call
//			        .setGas(100_000)
//			        //Set the query payment for the node returning the request
//			        //This value must cover the cost of the request otherwise will fail 
//			        .setQueryPayment(new Hbar(2))
//			        //Set the contract function
//			        .setFunction("getDocument",
//					new ContractFunctionParameters()
//					.addBytes32(_name));
//
//			 //Submit the query to a Hedera network
//			ContractFunctionResult contractUpdateResult = contractCallQuery.execute(client);
//	 
//			//Get the updated message
//			String message2 = contractUpdateResult.getString(0);
//	
//				 
//			//Log the updated message
//			System.out.println("The contract updated message: " + message2 + '\n');
 
		} catch (Exception e) {
			System.out.println(e);
		}
	}
	
	/**
	 *  Call smart contract Function
	 */
	public void callSmartContractFunction_ERC1400_getDocument(Client client, ContractId newContractId) { //, ContractFunctionParameters param) {
		try {			
			System.out.println("SmartContractService.callSmartContractFunction_ERC1400_getDocument():");
 		
			byte[] _name = "name".getBytes();
			
	 		// Calls a function of the smart contract
	 		ContractCallQuery contractQuery = new ContractCallQuery()
					.setContractId(newContractId)
					.setGas(1_000_000)
					.setQueryPayment(new Hbar(2))
					.setFunction("getDocument",
							new ContractFunctionParameters()
							.addBytes32(_name));
			
	 		//Submit to a Hedera network
	 		ContractFunctionResult getMessage = contractQuery.execute(client);
	 		
	 		System.out.println(getMessage.toString());
	 		
	 		//Get the message
	 		String message = getMessage.getString(0); 
	 		System.out.println("The contract getString: " + message);
	 		
	 		byte[] message2 = getMessage.getBytes(0);
	 		System.out.println("The contract getBytes: " + message2.toString());
	 		
	 		byte[] message3 = getMessage.getBytes32(0);
	 		System.out.println("The contract getBytes32: " + message3.toString() + '\n');
	 		
	 		
	 		byte[] message4 = getMessage.getBytes(0);
	 		System.out.println("The contract getBytes: " + message4.toString());
	 		
		} catch (Exception e) {
			System.out.println(e);
		}
	}
	
	
	/**
	 *  Get smart contract info
	 */
	public void getSmartContractInfo(Client client, ContractId contractId) {
 		try {		
 			System.out.println("SmartContractService.GetSmartContractInfo():");
 			
			//Create the query
			ContractInfoQuery query = new ContractInfoQuery()
			    .setContractId(contractId);

			//Sign the query with the client operator private key and submit to a Hedera network
			ContractInfo info = query.execute(client);

			System.out.print(info.toString() + '\n');
			
		} catch (Exception e) {
			System.out.println(e);
		}
	}
	
//	/**
//	 *   https://ethereum.stackexchange.com/questions/23549/convert-string-to-bytes32-in-web3j
//	 */
//	public static Bytes32 stringToBytes32(String string) {
//        byte[] byteValue = string.getBytes();
//        byte[] byteValueLen32 = new byte[32];
//        System.arraycopy(byteValue, 0, byteValueLen32, 0, byteValue.length);
//        return new Bytes32(byteValueLen32);
//    }
	
	/**
	 *   https://ethereum.stackexchange.com/questions/23549/convert-string-to-bytes32-in-web3j
	 *   
	 *   NOTE:  https://stackoverflow.com/questions/8660151/how-do-i-use-stringutils-in-java
	 */
//	public static Bytes32 bytes32ToString(String string) {
//		StringUtils.newStringUsAscii(varTypeBytes32.getValue());
//    }
	
	
	
	private static final char[] HEX_ARRAY = "0123456789ABCDEF".toCharArray();
	public static String bytesToHex(byte[] bytes) {
	    char[] hexChars = new char[bytes.length * 2];
	    for (int j = 0; j < bytes.length; j++) {
	        int v = bytes[j] & 0xFF;
	        hexChars[j * 2] = HEX_ARRAY[v >>> 4];
	        hexChars[j * 2 + 1] = HEX_ARRAY[v & 0x0F];
	    }
	    return new String(hexChars);
	}
}