package Hedera;

import java.util.Objects;

import io.github.cdimascio.dotenv.Dotenv;

import com.hedera.hashgraph.sdk.AccountBalance;
import com.hedera.hashgraph.sdk.AccountBalanceQuery;
import com.hedera.hashgraph.sdk.AccountCreateTransaction;
import com.hedera.hashgraph.sdk.AccountDeleteTransaction;
import com.hedera.hashgraph.sdk.AccountId;
import com.hedera.hashgraph.sdk.AccountInfo;
import com.hedera.hashgraph.sdk.AccountInfoQuery;
import com.hedera.hashgraph.sdk.Client;
//import com.hedera.hashgraph.sdk.FileId;
import com.hedera.hashgraph.sdk.Hbar;
import com.hedera.hashgraph.sdk.PrivateKey;
import com.hedera.hashgraph.sdk.PublicKey;
import com.hedera.hashgraph.sdk.Status;
import com.hedera.hashgraph.sdk.TransactionReceipt;
import com.hedera.hashgraph.sdk.TransactionResponse;
import com.hedera.hashgraph.sdk.TransferTransaction;

public class AccountService {
	public static Client client;
	public static AccountId treasuryId;
	public static PrivateKey treasuryKey;

	public PrivateKey accountKey;
	public AccountId accountId;
//	public FileId fileId;

	/**
	 * Sign into user account
	 */
	public static void connectToDeveloperPortal() throws Exception {
		try {
			System.out.println("AccountService.connectToDeveloperPortal():");
			// Grab your Hedera testnet account ID and private key
			AccountId myAccountId = AccountId
					.fromString(Objects.requireNonNull(Dotenv.load().get("TESTNET_ACCOUNT_ID")));
			PrivateKey myPrivateKey = PrivateKey
					.fromString(Objects.requireNonNull(Dotenv.load().get("TESTNET_PRIVATE_KEY")));

			// Create your Hedera testnet client
			Client client = Client.forTestnet();
			client.setOperator(myAccountId, myPrivateKey);

			AccountService.client = client;
			AccountService.treasuryKey = myPrivateKey;
			AccountService.treasuryId = myAccountId;
			
			System.out.println("client:" + client.toString());
			System.out.println("treasuryKey:" + myPrivateKey.toString());
			System.out.println("treasuryId:" + myAccountId.toString());

		} catch (Exception e) {
			System.out.println(e);
		}
	}

	/**
	 * delete account
	 */
	public static void deleteAccount(Client client, AccountId accountToDeleteId, AccountId transferAccountId,
			PrivateKey newKey) throws Exception {
		try {
			// Create the transaction to delete an account
			AccountDeleteTransaction transaction = new AccountDeleteTransaction().setAccountId(accountToDeleteId)
					.setTransferAccountId(transferAccountId);

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
	 * Get Account Balance
	 */
	public static void getAccountBalance(Client client, AccountId accountId) {
		try {

			// Create the account balance query
			AccountBalanceQuery query = new AccountBalanceQuery().setAccountId(accountId);

			// Sign with client operator private key and submit the query to a Hedera
			// network
			AccountBalance accountBalance = query.execute(client);

			// Print the balance of hbars
			System.out.println("The hbar account balance for this account " + accountId.toString() + " is "
					+ accountBalance.hbars + '\n');

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Get Account Info
	 */
	public static AccountInfo getAccountInfo(Client client, AccountId newAccountId) {
		AccountInfo accountInfo = null;
		try {

			// Create the account info query
			AccountInfoQuery query = new AccountInfoQuery().setAccountId(newAccountId);

			// Submit the query to a Hedera network
			accountInfo = query.execute(client);

			// Print the account key to the console
			System.out.println("The info for this account is: ");
			System.out.println(accountInfo.toString() + '\n');

		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return accountInfo;
	}

	/**
	 * Transfer Hbar from one account to another
	 */
	public static void transferHbar(Client client, AccountId myAccountId, AccountId newAccountId) {
		try {

			// Transfer hbar
			TransactionResponse sendHbar = new TransferTransaction()
					.addHbarTransfer(myAccountId, Hbar.fromTinybars(-1000))
					.addHbarTransfer(newAccountId, Hbar.fromTinybars(1000)).execute(client);

			System.out.println("The transfer transaction was: " + sendHbar.getReceipt(client).status);

			// Request the cost of the query
			Hbar queryCost = new AccountBalanceQuery().setAccountId(newAccountId).getCost(client);

			System.out.println("The cost of this query is: " + queryCost);

			// Check the new account's balance
			AccountBalance accountBalanceNew = new AccountBalanceQuery().setAccountId(newAccountId).execute(client);

			System.out.println("The new account balance is: " + accountBalanceNew.hbars);

		} catch (Exception e) {
			System.out.println(e);
		}
	}

	/**
	 * create account
	 */
	public void createAccount() throws Exception {
		try {
			// Generate a new key pair
			PrivateKey newAccountPrivateKey = PrivateKey.generateECDSA();
			PublicKey newAccountPublicKey = newAccountPrivateKey.getPublicKey();

			// Create new account and assign the public key
			TransactionResponse newAccount = new AccountCreateTransaction().setKey(newAccountPublicKey)
					.setInitialBalance(new Hbar(10)).execute(AccountService.client);

			// Get the new account ID
			AccountId newAccountId = newAccount.getReceipt(AccountService.client).accountId;
			System.out.println("The new account ID is: " + newAccountId);
			
			System.out.println("The new account private key is: " + newAccountPrivateKey);

			// Check the new account's balance
			AccountBalance accountBalance = new AccountBalanceQuery().setAccountId(newAccountId).execute(client);
			System.out.println("The new account balance is: " + accountBalance.hbars + '\n');

			this.accountKey = newAccountPrivateKey;
			this.accountId = newAccountId;
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}