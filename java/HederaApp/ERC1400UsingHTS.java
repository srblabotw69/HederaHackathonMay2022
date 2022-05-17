package HederaApp;

import java.io.File;
import java.util.Objects;

import io.github.cdimascio.dotenv.Dotenv;

import com.hedera.hashgraph.sdk.AccountId;
import com.hedera.hashgraph.sdk.AccountInfo;
import com.hedera.hashgraph.sdk.ContractFunctionParameters;
import com.hedera.hashgraph.sdk.ContractId;
import com.hedera.hashgraph.sdk.PrivateKey;
import com.hedera.hashgraph.sdk.TokenId;
import com.hedera.hashgraph.sdk.TokenType;

import Hedera.AccountService;
import Hedera.FileHandling;
import Hedera.FileService;
import Hedera.TokenService;
import Hedera.SmartContractService;

public class ERC1400UsingHTS {

	final private static String binFileAbsPath = System.getProperty("user.dir")
			+ Objects.requireNonNull(Dotenv.load().get("SOURCE_CODE_PACKAGE_HTS_ERC1400V2"));
	final private static String filename = Objects
			.requireNonNull(Dotenv.load().get("CONTRACT_BIN_FILENAME_ERC1400V2"));
 	
	public ContractId contractId;
	
	/**
	 *  
	 */
	public ERC1400UsingHTS(File file, String hashType) throws Exception {
		// 1. create token
		TokenId tokId = createERC1400Token();
		System.out.println("tokId: " + tokId.toString() + '\n');
		
		// 2. create file hash: "SHA-256" or "SHA3-256"
		System.out.println("file to hash: " + file.toString() + '\n');
		byte[] hashByte = FileHandling.createFileHashByte(file, hashType);
		System.out.println("hashStr: " + hashByte.toString() + '\n');


		// 3. call smart contract and store tokenId and filehash
		linkTokenIdToFilehash(tokId, hashByte);
		
		 
		// 3. store file hash in Hedera file service
	 
		
	}

	/**
	 *  
	 */
	public void linkTokenIdToFilehash(TokenId tokenId, byte[] _documentHash) throws Exception {
		
		// bytes32 _name, string calldata _uri, bytes32 _documentHash
//		String paramArray[] = {tokenId.toString(), hashStr.toString()};
		
		SmartContractService scs = new SmartContractService();
 
 		byte[] _name = "name".getBytes();
 		String _uri = "test/uri";
 		
 		scs.callSmartContractFunction_ERC1400v2_setDocument(AccountService.client, this.contractId);
		
		//check that params are passed thru function
//		scs.getSmartContractInfo(AccountService.client, this.contractId);
	}	

	
	/**
	 *  
	 */
	public TokenId createERC1400Token() throws Exception {
		
		TokenService tokenservice = new TokenService();
		
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
			PrivateKey accKey = PrivateKey.fromString(
					"3030020100300706052b8104000a042204200ccad0888e6f4ffcea487faae34880fb81e62485d3530e8f6386d32893cc8f1a");

			/////////////////////////////////////////////////////
			// Get account info
			/////////////////////////////////////////////////////

			AccountInfo accountInfo = AccountService.getAccountInfo(AccountService.client, accId);
			System.out.println(accountInfo.toString() + '\n');

			/////////////////////////////////////////////////////
			// Split file max 1024KB
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
			// Create Fungible Token
			/////////////////////////////////////////////////////

			tokenservice.createToken(AccountService.client, AccountService.treasuryKey, AccountService.treasuryKey,
					AccountService.treasuryId, "myTokenName", "myTokenSymbol", TokenType.FUNGIBLE_COMMON);

			/////////////////////////////////////////////////////
			// Call smart contract and pass in the token id in
			// solidity format
			/////////////////////////////////////////////////////

			SmartContractService smtContract = new SmartContractService();
			smtContract.createSmartContract(AccountService.client, fservice.fileId,
					new ContractFunctionParameters().addAddress(tokenservice.tokenId.toSolidityAddress()));
			
			this.contractId = smtContract.contractId;
			
			/////////////////////////////////////////////////////
			// Token Associate
			/////////////////////////////////////////////////////

			tokenservice.associateToken(AccountService.client, accKey, accId, smtContract.contractId,
					tokenservice.tokenId);

			/////////////////////////////////////////////////////
			// Get the tokenAssociate Transaction Record
			/////////////////////////////////////////////////////

			tokenservice.getTokenAssociateTransactionRecord(AccountService.client, AccountService.treasuryId);
 
			/////////////////////////////////////////////////////
			// Call the tokenTransfer Contract Function
			/////////////////////////////////////////////////////

// 			tokenservice.tokenTransfer(AccountService.client, AccountService.treasuryId, acctService.accountId, -10, 10);

//			long numOfToken = 1;
//			tokenservice.tokenTransfer(AccountService.client, AccountService.treasuryKey, AccountService.treasuryId,
//					acctService.accountId, tokenservice.tokenId, smtContract.contractId, numOfToken);

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
//			

		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return tokenservice.tokenId;
	}
}