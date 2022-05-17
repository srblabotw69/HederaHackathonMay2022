package HederaApp;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.hedera.hashgraph.sdk.AccountId;
import com.hedera.hashgraph.sdk.AccountInfo;
import com.hedera.hashgraph.sdk.ContractFunctionParameters;
import com.hedera.hashgraph.sdk.ContractId;
import com.hedera.hashgraph.sdk.TokenId;
import com.hedera.hashgraph.sdk.TokenRelationship;

import Hedera.AccountService;
import Hedera.FileHandling;
import Hedera.JSONToken;
import Hedera.SmartContractService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

@WebServlet("/FileUploadServlet")
@MultipartConfig(fileSizeThreshold = 1024 * 1024 * 10, // 10 MB
		maxFileSize = 1024 * 1024 * 50, // 50 MB
		maxRequestSize = 1024 * 1024 * 100) // 100 MB
public class FileUploadServlet extends HttpServlet {

	private static final long serialVersionUID = 205242440643911308L;

	/**
	 * Directory where uploaded files will be saved, its relative to the web
	 * application directory.
	 */
//    private static final String UPLOAD_DIR = "uploads";
	private static final String UPLOAD_DIR_PATH = "/Users/sspc/eclipse-workspace/Hedera/lib/gcp";

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			/////////////////////////////////////////////////////
			// Upload file to local server from JSP
			/////////////////////////////////////////////////////

//    	// gets absolute path of the web application
//      String applicationPath = request.getServletContext().getRealPath("");
//      // constructs path of the directory to save uploaded file
//      String uploadFilePath = applicationPath + File.separator + UPLOAD_DIR;

			// creates the save directory if it does not exists
			File fileSaveDir = new File(UPLOAD_DIR_PATH);
			if (!fileSaveDir.exists()) {
				fileSaveDir.mkdirs();
			}
			System.out.println("Upload File Directory: " + fileSaveDir.getAbsolutePath());

			String absfilePath = null;
			String fileName = null;
			String fileHash = null;
			byte[] fileHashByte = null;
			String fileHashMsg = null;
			String fileuploadMsg = null;

			// Get all the parts from request and write it to the file on server
			for (Part part : request.getParts()) {

				System.out.println("part: " + part.toString());
				fileName = getFileName(part);

				if (fileName != "") {
					System.out.println(UPLOAD_DIR_PATH + File.separator + fileName);
					part.write(UPLOAD_DIR_PATH + File.separator + fileName);

					absfilePath = UPLOAD_DIR_PATH + File.separator + fileName;
					File file = new File(absfilePath);

					/////////////////////////////////////////////////////
					// Create file hash
					/////////////////////////////////////////////////////

//					fileHash = FileHandling.createFileHash(file, "SHA-256");
//					fileHashMsg = "The file hash is: " + fileHash;
					
					fileHashByte = FileHandling.createFileHashByte(file, "SHA-256");
					fileHashMsg = "The file hash is: " + fileHashByte.toString();

					/////////////////////////////////////////////////////
					// Store file hash
					/////////////////////////////////////////////////////

 //				   sc: remove this since it is not compatiable with google file upload
					
//					SmartContractServiceERC1400_createContract smtContractERC1400_create = new SmartContractServiceERC1400_createContract(
//							fileHashByte);
					
					 
					/////////////////////////////////////////////////////
					// Upload file to GCP
					/////////////////////////////////////////////////////

					FileHandling.uploadToGoogleCloudPlatform(file);
					fileuploadMsg = "The file " + fileName + " have been uploaded to Google Cloud Platform.";
				}
			}

			/////////////////////////////////////////////////////
			// Connect to DeveloperPortal account
			/////////////////////////////////////////////////////

			AccountService.connectToDeveloperPortal();
			AccountId treasuryId = AccountService.treasuryId;

			/////////////////////////////////////////////////////
			// OPTION 2: Use existing account
			/////////////////////////////////////////////////////

			AccountId accId = AccountId.fromString("0.0.34717281");
			// PrivateKey accKey =
			// PrivateKey.fromString("3030020100300706052b8104000a042204200ccad0888e6f4ffcea487faae34880fb81e62485d3530e8f6386d32893cc8f1a");

			/////////////////////////////////////////////////////
			// Get account info
			/////////////////////////////////////////////////////

			AccountInfo accountInfo = AccountService.getAccountInfo(AccountService.client, accId);
			System.out.println("accountInfo: " + accountInfo.toString() + '\n');

//		TokenId tokenRelationships = null;

			List<JSONToken> tokenDetails = new ArrayList<JSONToken>();

			List<JSONToken> products = new ArrayList<JSONToken>();

//		Map<TokenId, TokenRelationship> accountTokenInfoMap = accountInfo.tokenRelationships;
//		System.out.println("All tokens associated with this account:");
//        for (TokenId key: accountTokenInfoMap.keySet()) {
//        	json.put("tokenID", key);
//            System.out.println(key);
//	        }
//        System.out.println("");

			Map<TokenId, TokenRelationship> accountTokenInfoMap2 = accountInfo.tokenRelationships;
			System.out.println("All tokens details associated with this account:");
			String substrTokenId, substrTokenIdWithoutLabel, substrTokenSym = null;

			for (TokenRelationship value : accountTokenInfoMap2.values()) {
				substrTokenId = value.toString().substring(18, 38);
				substrTokenSym = value.toString().substring(47, 60);
				substrTokenIdWithoutLabel = value.toString().substring(26, 38);

				String lastChr = substrTokenId.substring(substrTokenId.length() - 1);
				int numOfDocs = Integer.valueOf(lastChr);
				tokenDetails.add(new JSONToken(substrTokenIdWithoutLabel, substrTokenSym, numOfDocs));

				products.add(new JSONToken(substrTokenIdWithoutLabel));

				System.out.println(substrTokenIdWithoutLabel);
				System.out.println(substrTokenId.toString() + " " + substrTokenSym.toString());
				System.out.println("numOfDocs: " + numOfDocs);
			}
			System.out.println("");

			/////////////////////////////////////////////////////
			// Servlet post response
			/////////////////////////////////////////////////////
			Object data = fileuploadMsg + " <br><br> " + fileHashMsg;
			request.setAttribute("data", data);

//		request.setAttribute("accountInfo", accountInfo);
			request.setAttribute("treasuryId", treasuryId);
			request.setAttribute("accId", accId);
			request.setAttribute("products", products);
			request.setAttribute("tokenDetails", tokenDetails);
			request.setAttribute("fileHashMsg", fileHashMsg);
			request.setAttribute("fileuploadMsg", fileuploadMsg);

			request.getRequestDispatcher("hedera/HederaMain.jsp").forward(request, response);

//        request.setAttribute("message", fileName + " File uploaded successfully!");
////      getServletContext().getRequestDispatcher("/hedera/response.jsp").forward(
////                request, response);
//        getServletContext().getRequestDispatcher("/hedera/HederaMain.jsp").forward(
//                request, response);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Utility method to get file name from HTTP header content-disposition
	 */
	private String getFileName(Part part) {
		String contentDisp = part.getHeader("content-disposition");
		System.out.println("content-disposition header= " + contentDisp);
		String[] tokens = contentDisp.split(";");
		for (String token : tokens) {
			if (token.trim().startsWith("filename")) {
				return token.substring(token.indexOf("=") + 2, token.length() - 1);
			}
		}
		return "";
	}
}
