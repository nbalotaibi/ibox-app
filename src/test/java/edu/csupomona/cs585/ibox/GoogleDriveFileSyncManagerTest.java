package edu.csupomona.cs585.ibox;

import com.google.api.client.http.AbstractInputStreamContent;

import com.google.api.services.drive.Drive;
import com.google.api.services.drive.Drive.Files;
import com.google.api.services.drive.Drive.Files.Delete;
import com.google.api.services.drive.Drive.Files.Insert;
import com.google.api.services.drive.Drive.Files.Update;

import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.io.File;
import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;



import edu.csupomona.cs585.ibox.sync.GoogleDriveFileSyncManager;

public class GoogleDriveFileSyncManagerTest {

	private Drive mockService= Mockito.mock(Drive.class);
	private GoogleDriveFileSyncManager googledriveF = new GoogleDriveFileSyncManager(mockService);
	com.google.api.services.drive.model.File googlefile  = new com.google.api.services.drive.model.File();
	private File localFile = Mockito.mock(File.class);

	@Test
	public void testaddFile() throws IOException {
		
		Files mockFile = Mockito.mock(Files.class);

		when(mockService.files()).thenReturn(mockFile);

		Insert mockInsert = Mockito.mock(Insert.class);
		when(
				mockFile.insert(
						isA(com.google.api.services.drive.model.File.class),
						isA(com.google.api.client.http.FileContent.class)))
				.thenReturn(mockInsert);

	
		googlefile.setId("Field ID:test");
		when(mockInsert.execute()).thenReturn(googlefile);
		googlefile.getId();

		googledriveF.addFile(localFile);
		verify(mockService).files();
	}
	
	@Test
	public void testupdateFile() throws IOException {
		
	    when(localFile.getName()).thenReturn("test.txt");

		Files mockFile = mock(Files.class);
		when(mockService.files()).thenReturn(mockFile);

		com.google.api.services.drive.Drive.Files.List mockList = mock(com.google.api.services.drive.Drive.Files.List.class);
		when(mockFile.list()).thenReturn(mockList);
        
		//when get the ID
		googlefile.setId("Field ID:test");
		googlefile.setTitle("test.txt");

		com.google.api.services.drive.model.FileList googleFileList = new com.google.api.services.drive.model.FileList();
		List<com.google.api.services.drive.model.File> items = new ArrayList<com.google.api.services.drive.model.File>();
		items.add(googlefile);
		googleFileList.setItems(items);
		// When the File add
	   Insert mockInsert = mock(Insert.class);
	   when(
		      mockFile.insert(
	                    isA(com.google.api.services.drive.model.File.class),
						isA(com.google.api.client.http.FileContent.class)))
				.thenReturn(mockInsert);
		when(mockList.execute()).thenReturn(googleFileList);

		String fileId = googledriveF.getFileId("test.txt");

		Update updateTest = Mockito.mock(Update.class);
		when(
				mockFile.update(eq(fileId),
						isA(com.google.api.services.drive.model.File.class),
						isA(AbstractInputStreamContent.class))).thenReturn(
				updateTest);

		com.google.api.services.drive.model.File googlefile2 = new com.google.api.services.drive.model.File();
		when(updateTest.execute()).thenReturn(googlefile2);

		googledriveF.updateFile(localFile);
		verify(mockService, times(3)).files();
	}

	@Test (expected = Exception.class)
	public void testupdateFile_null() throws IOException {
	
		when(localFile.getName()).thenReturn(null);
		googledriveF.updateFile(localFile);
	}

	@Test
	public void testdeleteFile() throws IOException {
		 
		when(localFile.getName()).thenReturn("test.txt");

		Files mockFile = mock(Files.class);
		when(mockService.files()).thenReturn(mockFile);
		
		// when get the ID
		com.google.api.services.drive.Drive.Files.List mockList = mock(com.google.api.services.drive.Drive.Files.List.class);
		when(mockFile.list()).thenReturn(mockList);

		
		googlefile.setId("Field ID:test");
		googlefile.setTitle("test.txt");

		com.google.api.services.drive.model.FileList googlefileList = 
				new com.google.api.services.drive.model.FileList();
		List<com.google.api.services.drive.model.File> items = 
				new ArrayList<com.google.api.services.drive.model.File>();
		items.add(googlefile);
		googlefileList.setItems(items);

		when(mockList.execute()).thenReturn(googlefileList);

		String fileId = googledriveF.getFileId("test.txt");
		
		when(localFile.getName()).thenReturn("test.txt");

		Delete deleteMock = Mockito.mock(Delete.class);
		when(mockFile.delete(eq(fileId))).thenReturn(deleteMock);

		googledriveF.deleteFile(localFile);
		verify(mockFile).delete(fileId);
		verify(mockService, times(3)).files();
	}

	@Test (expected = Exception.class)
	public void testdeleteFile_null() throws IOException {
	
		when(localFile.getName()).thenReturn(null);
		googledriveF.deleteFile(localFile);
	}

	
	
	@Test
	public void testgetFileId() throws IOException {
		
		Files mockFile = mock(Files.class);
		when(mockService.files()).thenReturn(mockFile);

		com.google.api.services.drive.Drive.Files.List mockList = 
				mock(com.google.api.services.drive.Drive.Files.List.class);
		when(mockFile.list()).thenReturn(mockList);

		
		googlefile.setId("Field ID:test");
		googlefile.setTitle("test.txt");

		com.google.api.services.drive.model.FileList googleFileList  = 
				new com.google.api.services.drive.model.FileList();
		List<com.google.api.services.drive.model.File> items = 
				new ArrayList<com.google.api.services.drive.model.File>();
		
		items.add(googlefile);
		googleFileList.setItems(items);
        when(mockList.execute()).thenReturn(googleFileList);

		String getId = googledriveF.getFileId("test.txt");
		Assert.assertEquals("Field ID:test", getId);

	}



}
