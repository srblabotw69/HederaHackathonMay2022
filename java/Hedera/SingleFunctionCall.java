 package Hedera;

public class SingleFunctionCall {
	
	public static void main(String[] args) throws Exception {
		try {
			
			// put your code here
			

			AccountService.connectToDeveloperPortal();
			System.out.println("Hello There");			
//			AccountService.transferHbar(AccountService.client, AccountService.treasuryId, null);
		
//			AccountId acctId = AccountId.fromString("0.0.34286957");  // failed pre-check with the status `INVALID_ACCOUNT_ID`
//			AccountId acctId = AccountId.fromString("0.0.34096250");  //works. The new account balance is: 9328.5727912 ℏ    treasury account.  
			
//			AccountId acctId = AccountId.fromString("0.0.34286943");  //works. The new account balance is: 0.0001 ℏ
//			AccountId acctId = AccountId.fromString("0.0.34286893");  //works. The new account balance is: 28.99319946 ℏ
//			AccountId acctId = AccountId.fromString("0.0.34096250");  //works. The new account balance is: 75 ℏ

			
			// RUN THIS link to see accounts >= 1000 hbar!!!!!!!!!!!!
			//
			// Returns account IDs greater than or equal to 1000
			//
			// https://testnet.mirrornode.hedera.com/api/v1/accounts?account.id=gte:0.0.34096250
 
			
//			// Check the new account's balance
//			AccountBalance accountBalance = new AccountBalanceQuery().setAccountId(acctId).execute(AccountService.client);
//			System.out.println("The new account balance is: " + accountBalance.hbars); // 10 ℏ
		  
 
			
//			AccountService.deleteAccount(AccountService.client, acctId, AccountService.treasuryId, AccountService.treasuryKey);
			
		} catch (Exception e) {
			System.out.println(e);
		}
	}
}