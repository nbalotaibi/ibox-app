package edu.csupomona.cs585.ibox;
import static org.junit.Assert.*;

import java.awt.List;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.GeneralSecurityException;
import java.util.Collections;
import javax.servlet.AsyncContext;

import org.junit.Test;
import org.mockito.Mockito;
import org.junit.Assert;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.Drive.Files;
import com.google.api.services.drive.DriveScopes;
//import org.json.simple.JSONObject;
import com.google.api.services.drive.model.FileList;

import edu.csupomona.cs585.ibox.sync.GoogleDriveFileSyncManager;

public class GoogleDriveFileSyncManagerIntegrationTest {
	
	private String filePath= "/Users/norahbijad/Documents/workspace/ibox-app/test.txt";
	private File localFile = new File(filePath);
	private String fileName= "test.txt";
	
	Drive Service;
	 static GoogleDriveFileSyncManager googledriveF ;
	   private static final String PATH = "/Users/norahbijad/Documents/workspace/ibox-app/My Project-b916d6928bcb.p12";
	
	   private void setup() throws IOException{
		   GoogleDriveServicesIntegration();		
		   googledriveF = new GoogleDriveFileSyncManager(Service);
		}
	 
	
	public void GoogleDriveServicesIntegration() throws IOException {
	    HttpTransport httpTransport = new NetHttpTransport();
	       JsonFactory jsonFactory = new JacksonFactory();

	       try{
	           GoogleCredential credential = new  GoogleCredential.Builder()
	             .setTransport(httpTransport)
	             .setJsonFactory(jsonFactory)
	             .setServiceAccountId(
	            		 "342028933683-p9lkls1ccg65idrt3sfbjm03pelosn37.apps.googleusercontent.com"  		 
	            		 )
	             .setServiceAccountScopes(Collections.singleton(DriveScopes.DRIVE))
	             .setServiceAccountPrivateKeyFromP12File(
	            		 new File(PATH))
	             .build();

	           Service = new Drive.Builder(httpTransport, 
	        		   jsonFactory, credential).setApplicationName("ibox").build();  
	       }catch(GeneralSecurityException e){
	           e.printStackTrace();
	       }

}


	@Test
	public void testgetFileId_null() throws IOException {
		setup();		
		Assert.assertEquals(googledriveF.getFileId(null), null);
	}    	
	
	@Test
	public void testGetFileId() throws IOException {
		setup();	
		String id1= googledriveF.getFileId(fileName);
		
		String getFileName= localFile.getName();		
		String id2= googledriveF.getFileId(getFileName);
		
		Assert.assertEquals(id1, id2);
	}
	
	@Test(expected = Exception.class)
	public void testdeleteFiles_null() throws IOException{
		setup();
		File localFile = null;
		googledriveF.updateFile(localFile);
	}

	

	

}
