package Hedera;

import java.util.Collections;
import java.util.List;

import com.hedera.hashgraph.sdk.AccountBalance;
import com.hedera.hashgraph.sdk.AccountBalanceQuery;
import com.hedera.hashgraph.sdk.AccountId;
import com.hedera.hashgraph.sdk.Client;
import com.hedera.hashgraph.sdk.ContractId;
import com.hedera.hashgraph.sdk.Hbar;
import com.hedera.hashgraph.sdk.PrivateKey;
import com.hedera.hashgraph.sdk.Status;
import com.hedera.hashgraph.sdk.TokenAssociateTransaction;
import com.hedera.hashgraph.sdk.TokenCreateTransaction;
import com.hedera.hashgraph.sdk.TokenDeleteTransaction;
import com.hedera.hashgraph.sdk.TokenDissociateTransaction;
import com.hedera.hashgraph.sdk.TokenId;
import com.hedera.hashgraph.sdk.TokenInfoQuery;
import com.hedera.hashgraph.sdk.TokenType;
import com.hedera.hashgraph.sdk.TransactionReceipt;
import com.hedera.hashgraph.sdk.TransactionResponse;
import com.hedera.hashgraph.sdk.TransferTransaction;

public class TokenService {

	public TokenId tokenId;
	public TransactionResponse createTxResponse;
 

	/**
	 * Create token
	 */
	public void createToken(Client client, PrivateKey adminKey, PrivateKey treasuryKey,
			AccountId treasuryId, String tokenName, String tokenSymbol, TokenType tokenType) {
		try {
			System.out.println("TokenService.createAToken():");

			// Create the transaction
			TokenCreateTransaction transaction = new TokenCreateTransaction()
					.setTokenName(tokenName)
					.setTokenSymbol(tokenSymbol)
					.setTokenType(tokenType)
					.setTreasuryAccountId(treasuryId)
					.setInitialSupply(5000)
					.setAdminKey(adminKey.getPublicKey())
					.setMaxTransactionFee(new Hbar(10));
																														
			// Build the unsigned transaction, sign with admin private key of the token,
			// sign with the token treasury private key, submit the transaction to a Hedera
			// network
			TransactionResponse txResponse = transaction.freezeWith(client).sign(adminKey).sign(treasuryKey)
					.execute(client);

			// Request the receipt of the transaction
			TransactionReceipt receipt = txResponse.getReceipt(client);

			// Get the token ID from the receipt
			this.tokenId = receipt.tokenId;

			System.out.println("The new token ID is " + this.tokenId);
			
			this.createTxResponse = txResponse;
			
			System.out.println("The new token transaction ID is " + txResponse.transactionId);
			
			
			// Get the transaction consensus status
			Status transactionStatus = receipt.status;
			
			System.out.println("The transaction consensus status " + transactionStatus + '\n');

			
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	/**
	 * associate token to account
	 */
	public void associateToken(Client client, PrivateKey accountKey, AccountId accountIdTest, ContractId newContractId,
			TokenId tokenId) {
		try {
			System.out.println("TokenService.associateToken():");
			System.out.println("contractId: " + newContractId.toString() + " with solidity address of "
					+ newContractId.toSolidityAddress());
			System.out.println("accountId: " + accountIdTest.toString() + " with solidity address of "
					+ accountIdTest.toSolidityAddress());
			System.out.println("tokenId: " + tokenId.toString() + " with solidity address of "
					+ tokenId.toSolidityAddress());

			// Associate a token to an account
			TokenAssociateTransaction transaction = new TokenAssociateTransaction().setAccountId(accountIdTest)
					.setTokenIds(Collections.singletonList(tokenId));

			// Freeze the unsigned transaction, sign with the private key of the account
			// that is being associated to a token, submit the transaction to a Hedera
			// network
			TransactionResponse txResponse = transaction.freezeWith(client).sign(accountKey).execute(client);

			// Request the receipt of the transaction
			TransactionReceipt receipt = txResponse.getReceipt(client);

			// Get the transaction consensus status
			Status transactionStatus = receipt.status;
			
			System.out.println("The transaction consensus status " + transactionStatus + '\n');

//			this.txResponseAssociateToken = txResponse;

			
 
			
			
//			// Associate the token to an account using the HTS contract
//			ContractExecuteTransaction associateToken = new ContractExecuteTransaction()
//					// The contract to call
//					.setContractId(newContractId)
//					// The gas for the transaction
//					.setGas(2_000_000)
//					// The contract function to call and parameters to pass
//					.setFunction("tokenAssociate", new ContractFunctionParameters()
//							// The account ID to associate the token to
//							.addAddress(accountIdTest.toSolidityAddress())
//							// The token ID to associate to the account
//							.addAddress(tokenId.toSolidityAddress()));
//
//			// Sign with the account key to associate and submit to the Hedera network
//			TransactionResponse associateTokenResponse = associateToken.freezeWith(client).sign(privateKeyTest)
//					.execute(client);
//
//			this.associateTokenResponse = associateTokenResponse;
//
//			System.out.println("The transaction status: " + associateTokenResponse.getReceipt(client).status + '\n');


			
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	/**
	 * dissociate a token from account
	 */
	public void deleteToken(Client client, PrivateKey adminKey, TokenId tokenId) {
		try {
			System.out.println("TokenService.deleteToken():");

			//Create the transaction
			TokenDeleteTransaction transaction = new TokenDeleteTransaction()
			     .setTokenId(tokenId);

			//Freeze the unsigned transaction, sign with the admin private key of the account, submit the transaction to a Hedera network
			TransactionResponse txResponse = transaction.freezeWith(client).sign(adminKey).execute(client);

			//Request the receipt of the transaction
			TransactionReceipt receipt = txResponse.getReceipt(client);

			//Obtain the transaction consensus status
			Status transactionStatus = receipt.status;

			System.out.println("Token: " + tokenId + " have been deleted.");
			System.out.println("The transaction consensus status is: " + transactionStatus + '\n');

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
	/**
	 * dissociate a token from account
	 */
	public void dissociateToken(Client client, PrivateKey accountKey, AccountId accountId, List<TokenId> tokenList) {
		try {
			System.out.println("TokenService.disAssociateToken():");
			
			// Disassociate a token from an account
			TokenDissociateTransaction transaction = new TokenDissociateTransaction().setAccountId(accountId)
					.setTokenIds(tokenList);

			// Freeze the unsigned transaction, sign with the private key of the account
			// that is being dissociated from a token, submit the transaction to a Hedera
			// network
			TransactionResponse txResponse = transaction.freezeWith(client).sign(accountKey).execute(client);

			// Request the receipt of the transaction
			TransactionReceipt receipt = txResponse.getReceipt(client);

			// Obtain the transaction consensus status
			Status transactionStatus = receipt.status;

			System.out.println("The transaction consensus status is: " + transactionStatus + '\n');

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Get the tokenAssociate Transaction Record
	 */
	public void getTokenAssociateTransactionRecord(Client client, AccountId accountIdTest) {
		try {
			System.out.println("TokenService.getTokenAssociateTransactionRecord():");
 
			// The balance of the account
			AccountBalance accountBalance3 = new AccountBalanceQuery().setAccountId(accountIdTest).execute(client);

			System.out.println("The " + this.tokenId + " should now be associated to my account: ");
			System.out.println(accountBalance3.tokens.toString() + '\n');
 
		} catch (Exception e) {
			System.out.println(e);
		}
	}
	
	/**
	 * Get the tokenAssociate Transaction Record
	 */
	public void getTokenInfo(Client client, TokenId tokenId) {
		try {
			System.out.println("TokenService.getTokeInfo():");
 
			//Create the query
			TokenInfoQuery query = new TokenInfoQuery()
			    .setTokenId(tokenId);

			//Sign with the client operator private key, submit the query to the network and get the token supply
			long tokenSupply = query.execute(client).totalSupply;
			boolean isDeleted = query.execute(client).isDeleted;

			System.out.println("The token info is: ");
			System.out.println(tokenSupply);
			System.out.println("isDeleted: " + isDeleted + '\n');
		 
 
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	/**
	 * transfer token to another account
	 */
//	public void tokenTransfer(Client client, AccountId fromAccountId, AccountId toAccountId, int fromAmt, int toAmt) {
	public void tokenTransfer(Client client, PrivateKey treasuryKey, AccountId treasuryId, AccountId accountId,
			TokenId tokenId, ContractId newContractId, long numOfToken) {
		try {
			System.out.println("TokenService.tokenTransfer():");

			//Create the transfer transaction
			TransferTransaction transaction = new TransferTransaction()
			     .addTokenTransfer(tokenId, treasuryId, -1)
			     .addTokenTransfer(tokenId, accountId, 1);

			//Sign with the client operator key and submit the transaction to a Hedera network
			TransactionResponse txResponse = transaction.freezeWith(client).sign(treasuryKey).execute(client);

			//Request the receipt of the transaction
			TransactionReceipt receipt = txResponse.getReceipt(client);

			//Get the transaction consensus status
			Status transactionStatus = receipt.status;

			System.out.println("The transaction consensus status is " +transactionStatus);

			
//////////////////////////////////////////////////////////////////////////////////////////////

//			// Transfer the new token to the account
//			// Contract function params need to be in the order of the paramters provided in
//			// the tokenTransfer contract function
//			ContractExecuteTransaction tokenTransfer = new ContractExecuteTransaction().setContractId(newContractId)
//					.setGas(100_000).setFunction("tokenTransfer", new ContractFunctionParameters()
//							// The ID of the token
//							.addAddress(tokenId.toSolidityAddress())
//							// The account to transfer the tokens from
//							.addAddress(treasuryId.toSolidityAddress())
//							// The account to transfer the tokens to
//							.addAddress(accountId.toSolidityAddress())
//							// The number of tokens to transfer
//							.addInt64(numOfToken));
//			
//
//			// Sign the token transfer transaction with the treasury account to authorize
//			// the transfer and submit
//			ContractExecuteTransaction signTokenTransfer = tokenTransfer.freezeWith(client).sign(treasuryKey);
//
//			// Submit transfer transaction
//			TransactionResponse submitTransfer = signTokenTransfer.execute(client);
//	
//			
//			// Get transaction status
//			Status txStatus = submitTransfer.getReceipt(client).status;
//			System.exit(0);
//			// Verify your account received the 100 tokens
//			AccountBalance newAccountBalance = new AccountBalanceQuery().setAccountId(accountId).execute(client);
//
//			System.out.println("My new account balance is " + newAccountBalance.tokens);
//
//			System.out.println("The transaction status: " + txStatus + '\n');

		} catch (Exception e) {
			System.out.println(e);
		}
	}

}
