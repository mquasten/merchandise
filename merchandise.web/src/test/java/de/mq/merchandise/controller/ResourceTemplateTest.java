package de.mq.merchandise.controller;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import junit.framework.Assert;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.primefaces.model.UploadedFile;
import org.springframework.web.client.ResourceAccessException;

public class ResourceTemplateTest {
	
	
	private static final String ONE_PIXEL_FILE_PATH = "src/test/resources/1X1-pixel.png";
	private static final String ONE_PIXEL_FILE_URL = "file:"+ONE_PIXEL_FILE_PATH;
	private static final String UPLOADED_FILE = "test.png";
	private ResourceOperations resourceOperations = new ResourceTemplateImpl();
	
	
	@Test
	public final void readImage() {
		final BufferedImage image = resourceOperations.readImage(ONE_PIXEL_FILE_URL);
		Assert.assertNotNull(image);
		Assert.assertEquals(1, image.getWidth());
		Assert.assertEquals(1, image.getHeight());
	}
	
	@Test(expected=ResourceAccessException.class)
	public final void readImageNotFound() {
		resourceOperations.readImage("file:src/test/resources/dontLetMeGetMe.png");
	}
	
	
	@Test
	public final void uplaod() throws IOException{
		final UploadedFile uploadedFile = Mockito.mock(UploadedFile.class);
		final InputStream is = ClassLoader.class.getResourceAsStream("/1X1-pixel.png");

	    

		Mockito.when(uploadedFile.getInputstream()).thenReturn(is);
		
		resourceOperations.uploadFile(uploadedFile, UPLOADED_FILE);
		
		Assert.assertTrue(FileUtils.contentEquals(new File(ONE_PIXEL_FILE_PATH), new File( ResourceTemplateImpl.TARGET_FOLDER, UPLOADED_FILE)));
		
	
	}
	
	@Test(expected=ResourceAccessException.class)
	public final void uploadIoException() throws IOException {
		final UploadedFile uploadedFile = Mockito.mock(UploadedFile.class);
		final InputStream is = Mockito.mock(InputStream.class);

	    Mockito.when(is.read((byte[])Mockito.any())).thenThrow(new IOException());

		Mockito.when(uploadedFile.getInputstream()).thenReturn(is);
		
		resourceOperations.uploadFile(uploadedFile, UPLOADED_FILE);
	}

	@Before
	@After
    public void cleanupFile() {
		try {
			FileUtils.forceDelete(new File( ResourceTemplateImpl.TARGET_FOLDER, UPLOADED_FILE));
		} catch (IOException e) {
			// System.out.println(e.getMessage());
		}
	}

}
