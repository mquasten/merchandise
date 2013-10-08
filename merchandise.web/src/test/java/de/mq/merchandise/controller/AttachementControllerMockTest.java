package de.mq.merchandise.controller;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import javax.servlet.http.HttpServletResponse;

import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.mock.web.MockHttpServletResponse;

import de.mq.merchandise.opportunity.ResourceOperations;
import de.mq.merchandise.opportunity.support.DocumentFileRepositoryMock;

public class AttachementControllerMockTest {
	
	
	private static final String EXT = "jpg";
	private static final String DOCUMENT = "kylie";
	private static final long ID = 19680528L;
	private static final String ENTITY = "opportunities";
	private final ResourceOperations resourceOperations = Mockito.mock(ResourceOperations.class);
	private final AttachementControllerMock attachementController = new AttachementControllerMock(resourceOperations);
	
	@Test
	public final void content() throws FileNotFoundException, IOException{
		final String path = String.format(DocumentFileRepositoryMock.DOCUMENT_FILE, ENTITY, ID, DOCUMENT+ "." + EXT);
		final HttpServletResponse response = new MockHttpServletResponse();
		final InputStream inputStream = Mockito.mock(InputStream.class);
			
		Mockito.when(resourceOperations.inputStream(path)).thenReturn(inputStream);
		attachementController.content(ENTITY, ID, DOCUMENT, EXT, response);
		
		Mockito.verify(resourceOperations).inputStream(path);
		Mockito.verify(resourceOperations).copy(inputStream, response.getOutputStream());
	
		Mockito.verify(inputStream).close();
		
	}

}
