package HederaApp;

import java.io.File;
import java.util.Objects;

import Hedera.AccountService;
import io.github.cdimascio.dotenv.Dotenv;
 
public class Helloh {
	
	final private static String binFileAbsPath = System.getProperty("user.dir")
			+ Objects.requireNonNull(Dotenv.load().get("SOURCE_CODE_PACKAGE_PATH"));
	final private static String filename = Objects.requireNonNull(Dotenv.load().get("CONTRACT_BIN_FILENAME_HELLO"));

    public String getHelloh() throws Exception {
    	System.out.println(Dotenv.load().get("SC_ENV"));
    	
		/////////////////////////////////////////////////////
		// Connect to DeveloperPortal account
		/////////////////////////////////////////////////////
		
		try {
			AccountService.connectToDeveloperPortal();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
    	return  "Hello Contract!!!";
    }
    
    public void getmain()  {

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

			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}
