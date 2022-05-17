package HederaApp;
 
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import java.nio.file.Files;
import java.nio.file.Paths;

public class QuickstartSample2 {
	
  public static void main(String[] args) throws Exception {
	  
	    // The ID of your GCP project
	   String  projectId = "***REMOVED***";

	    // The ID of your GCS bucket
	   String  bucketName = "hederabucket";

	    // The ID of your GCS object
	   String  objectName = "testfile.txt";

	    // The path to your file to upload
	   String  filePath = "/Users/sspc/eclipse-workspace/Hedera/lib/gcp/testfile.txt";
		
 
	   Storage storage = StorageOptions.newBuilder().setProjectId(projectId).build().getService();
	    BlobId blobId = BlobId.of(bucketName, objectName);
	    BlobInfo blobInfo = BlobInfo.newBuilder(blobId).build();
	    storage.create(blobInfo, Files.readAllBytes(Paths.get(filePath)));

	    System.out.println(
	        "File " + filePath + " uploaded to bucket " + bucketName + " as " + objectName);
	  }
 
   
}
 
