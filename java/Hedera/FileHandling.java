package Hedera;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;

public class FileHandling {

	/**
	 * getFileChecksum
	 */
	private static String getFileChecksum(MessageDigest digest, File file) throws IOException {
		// Get file input stream for reading the file content
		FileInputStream fis = new FileInputStream(file);

		// Create byte array to read data in chunks
		byte[] byteArray = new byte[1024];
		int bytesCount = 0;

		// Read file data and update in message digest
		while ((bytesCount = fis.read(byteArray)) != -1) {
			digest.update(byteArray, 0, bytesCount);
		}
		;

		// close the stream; We don't need it now.
		fis.close();

		// Get the hash's bytes
		byte[] bytes = digest.digest();

		// This bytes[] has bytes in decimal format;
		// Convert it to hexadecimal format
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < bytes.length; i++) {
			sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
		}

		// return complete hash
		return sb.toString();
	}

	/**
	 * String hashType parameters: "SHA-256" or "SHA3-256"
	 */
	public static String createFileChecksumHash(File file, String hashType)
			throws IOException, NoSuchAlgorithmException {
		final MessageDigest shaDigest256 = MessageDigest.getInstance(hashType);
		String shaChecksum = getFileChecksum(shaDigest256, file);
		return shaChecksum;
	}

	/**
	 * Create a file hash
	 *
	 * @param "SHA-256" or "SHA3-256"
	 * @return encodedString
	 */
	public static String createFileHash(File file, String hashType) throws IOException, NoSuchAlgorithmException {
		System.out.println("FileSecure.createFileHash():");
		System.out.println("file: " + file.toString());

		byte[] buffer = new byte[8192];
		int count;
		MessageDigest digest = MessageDigest.getInstance(hashType);
//		BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file.getName()));
		BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file.getAbsoluteFile()));

		while ((count = bis.read(buffer)) > 0) {
			digest.update(buffer, 0, count);
		}
		bis.close();

		byte[] hash = digest.digest();
		String encodedString = Base64.getEncoder().encodeToString(hash);

		System.out.println(encodedString + "\n");

		return encodedString;
	}

	/**
	 * Create a file hash and return byte[] hash
	 *
	 * @param "SHA-256" or "SHA3-256"
	 * @return encodedString
	 */
	public static byte[] createFileHashByte(File file, String hashType) throws IOException, NoSuchAlgorithmException {
		System.out.println("FileSecure.createFileHash():");
		System.out.println("file: " + file.toString());

		byte[] buffer = new byte[8192];
		int count;
		MessageDigest digest = MessageDigest.getInstance(hashType);
//		BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file.getName()));
		BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file.getAbsoluteFile()));

		while ((count = bis.read(buffer)) > 0) {
			digest.update(buffer, 0, count);
		}
		bis.close();

		byte[] hash = digest.digest();

//		String encodedString = Base64.getEncoder().encodeToString(hash);
//
//		System.out.println(encodedString + "\n");

		return hash;
	}

	/**
	 * Split file into 1024 KB files
	 */
	public static int splitFile(File f) throws IOException {
		int partCounter = 1; // start from 001, 002, 003, ...
								// you can change it to 0 if you want 000, 001, ...

		int sizeOfFiles = 1024;
		byte[] buffer = new byte[sizeOfFiles];

		String fileName = f.getName();
		int numOfFiles = 0;
		File newFile = null;

		// try-with-resources to ensure closing stream
		try (FileInputStream fis = new FileInputStream(f); BufferedInputStream bis = new BufferedInputStream(fis)) {
			System.out.println("FileService.splitFile(): " + f);
			int bytesAmount = 0;
			while ((bytesAmount = bis.read(buffer)) > 0) {
				// write each chunk of data into separate file with different number in name
				String filePartName = String.format("%s.%03d", fileName, partCounter++);
				newFile = new File(f.getParent(), filePartName);
				numOfFiles = numOfFiles + 1;
				System.out.println("split file: " + newFile);
				try (FileOutputStream out = new FileOutputStream(newFile)) {
					out.write(buffer, 0, bytesAmount);
				}
			}
			System.out.println("numOfFiles: " + numOfFiles + '\n');

		} catch (IOException e) {
			e.printStackTrace();
		}
		return numOfFiles;
	}

	/**
	 * Upload file to Google Cloud Platform
	 */
	public static void uploadToGoogleCloudPlatform(File fileToUpload) throws IOException {
		
		System.out.println("FileHandling.uploadToGoogleCloudPlatform()");

		System.out.println(fileToUpload.getName());

		/////////////////////////////////////////////////////
		// Google Cloud Platform
		/////////////////////////////////////////////////////

		// The ID of your GCP project
		String projectId = "";

		// The ID of your GCS bucket
		String bucketName = "";

		// The ID of your GCS object
		String objectName = fileToUpload.getName();

		// The path to your file to upload
		String filePath = "/Users/sspc/eclipse-workspace/Hedera/lib/gcp/" + fileToUpload.getName();

		Storage storage = StorageOptions.newBuilder().setProjectId(projectId).build().getService();
		BlobId blobId = BlobId.of(bucketName, objectName);
		BlobInfo blobInfo = BlobInfo.newBuilder(blobId).build();
		storage.create(blobInfo, Files.readAllBytes(Paths.get(filePath)));

		System.out.println("File " + filePath + " uploaded to bucket " + bucketName + " as " + objectName);

	}
}
