package HederaApp;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

//import com.google.cloud.storage.BlobId;
//import com.google.cloud.storage.BlobInfo;
//Imports the Google Cloud client library

//import com.google.cloud.storage.Storage;
//import com.google.cloud.storage.StorageOptions;
import com.hedera.hashgraph.sdk.AccountId;
import com.hedera.hashgraph.sdk.AccountInfo;
import com.hedera.hashgraph.sdk.TokenId;
import com.hedera.hashgraph.sdk.TokenRelationship;

import Hedera.AccountService;
import Hedera.FileHandling;
import Hedera.JSONToken;

@WebServlet("/GoogleCloudPlatformServlet")
public class FileUploadApp extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
 


		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}