package aws.Uploadaws;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.InvalidParameterSpecException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.apache.commons.io.IOUtils;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;

public class AmazonS3Example {
    	
    	private static final String SUFFIX = "/";
    	
    	public static void main(String[] args) throws FileNotFoundException, IOException, InvalidKeyException, NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, InvalidParameterSpecException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException {
    		// credentials object identifying user for authentication
    		// user must have AWSConnector and AmazonS3FullAccess for 
    		// this example to work
    		String folderName="testData";
    		String keyName="testData/new.des";
    		String path="/home/jacky/";
    		String file="new";
    		AWSCredentials credentials = new BasicAWSCredentials(
    				"AKIAIGDJ4WVTVAUIYSYA", 
    				"g9vo9c16XWWNygmxK2AHPLo4Pt+NqmKhtUNG3rHR");
    		
    		
    		System.out.println("Creating Connection....");
    		// create a client connection based on credentials
    		AmazonS3 s3client = new AmazonS3Client(credentials);
    		System.out.println("Connection Established..");
    		// create bucket - name must be unique for all S3 users
    		String bucketName = "springjack";
    		//s3client.createBucket(bucketName);
    		
    		// list buckets
    		for (Bucket bucket : s3client.listBuckets()) {
    			System.out.println(" - " + bucket.getName());
    		}
    		
    		// create folder into bucket
    		//createFolder(bucketName,folderName, s3client);
    		//downloadFile(bucketName,keyName,folderName,s3client,file+".des");
    		uploadFile(bucketName, folderName, s3client,file,path);
    		
    		
    		
    	}
    	
    	public static void uploadFile(String bucketName, String folderName, AmazonS3 client,String file,String path) throws InvalidKeyException, NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, InvalidParameterSpecException, IllegalBlockSizeException, BadPaddingException, IOException {
    	
    		// upload file to folder and set it to public
    		System.out.println("Start Uploading...");
    		String fileName = folderName + SUFFIX + file+".des";
    		new EncryptionClass().Encrypt(file,path);
    		client.putObject(new PutObjectRequest(bucketName, fileName, 
    				new File(path+file))
    				.withCannedAcl(CannedAccessControlList.PublicRead));
    		System.out.println("Uploaded Successfully!!!");
    		
    	}
    	
    	
    	public static void createFolder(String bucketName, String folderName, AmazonS3 client) {
    		// create meta-data for your folder and set content-length to 0
    		ObjectMetadata metadata = new ObjectMetadata();
    		metadata.setContentLength(0);
    		// create empty content
    		InputStream emptyContent = new ByteArrayInputStream(new byte[0]);
    		// create a PutObjectRequest passing the folder name suffixed by /
    		PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName,
    				folderName + SUFFIX, emptyContent, metadata);
    		// send request to S3 to create folder
    		client.putObject(putObjectRequest);
    		System.out.println("Folder Create Succesfully !!!");
    	}
    	
    	public static void downloadFile(String bucketName,String keyName, String folderName, AmazonS3 client,String file) throws FileNotFoundException, IOException, InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException, InvalidKeySpecException
    	{
    		System.out.println("Start Downloading...");
    		GetObjectRequest request = new GetObjectRequest(bucketName,
    				keyName);
    		S3Object object = client.getObject(request);
    		S3ObjectInputStream objectContent = object.getObjectContent();
    		IOUtils.copy(objectContent, new FileOutputStream("/home/jacky/Downloads/new.des"));
    		System.out.println("Downloaded Complete!!!");
    		EncryptionClass.Decrypt(file);
    	}
    }