package de.mq.merchandise.opportunity.support;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.http.MediaType;
import org.springframework.web.client.ResourceAccessException;

import de.mq.merchandise.customer.support.CustomerImpl;
import de.mq.merchandise.opportunity.ResourceOperations;
import de.mq.merchandise.util.EntityUtil;


public class DocumentFileRepositoryTest {
	
	private static final long ID = 19680528L;

	private static final String DIR_PATH = String.format(DocumentFileRepositoryMock.DOCUMENT_FOLDER,DocumentRepository.OPPORTUNITIES_ENTITY, ID);

	private static final String DIR_PATH2 = String.format(DocumentFileRepositoryMock.DOCUMENT_FOLDER,DocumentRepository.SUBJECTS_ENTITY, ID);
	
	private static final String DOCUMENT_NAME = "kylie.jpg";

	private final ResourceOperations resourceOperations = Mockito.mock(ResourceOperations.class);
	
	private final DocumentRepository documentRepository = new DocumentFileRepositoryMock(resourceOperations);
	
	private Opportunity opportunity = Mockito.mock(Opportunity.class);
	
	private CommercialSubject subject = Mockito.mock(CommercialSubject.class);
	
	private InputStream inputStream = Mockito.mock(InputStream.class);
	
	private final File dir = Mockito.mock(File.class);
	
	private final File file = Mockito.mock(File.class);
	
	private OutputStream outputStream = Mockito.mock(OutputStream.class);
	final String FILE_PATH = String.format(DocumentFileRepositoryMock.DOCUMENT_FILE, DocumentRepository.OPPORTUNITIES_ENTITY, ID, DOCUMENT_NAME);
	
	@Before
	public final void setup() {
		Mockito.when(opportunity.id()).thenReturn(ID);
		Mockito.when(subject.id()).thenReturn(ID);
		
	}
	
	
	@Test
	public final void assign() {
		Mockito.when(resourceOperations.file(DIR_PATH)).thenReturn(dir);
		Mockito.when(resourceOperations.outputStream(FILE_PATH)).thenReturn(outputStream);
		documentRepository.assign(opportunity, DOCUMENT_NAME, inputStream, MediaType.IMAGE_JPEG);
		
		Mockito.verify(dir).mkdirs();
		Mockito.verify(resourceOperations).copy(inputStream, outputStream);
	}
	
	
	@Test
	public final void assignSubject() {
		Mockito.when(resourceOperations.file(DIR_PATH2)).thenReturn(dir);
		Mockito.when(resourceOperations.outputStream(FILE_PATH)).thenReturn(outputStream);
		
		documentRepository.assign(subject, DOCUMENT_NAME, inputStream, MediaType.IMAGE_JPEG);
	}
	
	@Test(expected=InvalidDataAccessApiUsageException.class)
	public final void assignWrongEntity()  {
		documentRepository.assign(EntityUtil.create(CustomerImpl.class), DOCUMENT_NAME, inputStream, MediaType.IMAGE_JPEG);
	}
	
	@Test
	public final void assignDirectoryExists() {
		Mockito.when(dir.exists()).thenReturn(true);
		Mockito.when(resourceOperations.file(DIR_PATH)).thenReturn(dir);
		Mockito.when(resourceOperations.outputStream(FILE_PATH)).thenReturn(outputStream);
		documentRepository.assign(opportunity, DOCUMENT_NAME, inputStream, MediaType.IMAGE_JPEG);
		
		Mockito.verify(dir, Mockito.never()).mkdirs();
		Mockito.verify(resourceOperations).copy(inputStream, outputStream);
	}
	
	@Test(expected=ResourceAccessException.class)
	public final void assignIOException() throws IOException {
		Mockito.when(dir.exists()).thenReturn(true);
		Mockito.doThrow(new IOException()).when(outputStream).close();
		Mockito.when(resourceOperations.file(DIR_PATH)).thenReturn(dir);
		Mockito.when(resourceOperations.outputStream(FILE_PATH)).thenReturn(outputStream);
	   
		documentRepository.assign(opportunity, DOCUMENT_NAME, inputStream, MediaType.IMAGE_JPEG);
	
	
	}
	
	@Test
	public final void delete() {

	
		Mockito.when(resourceOperations.file(FILE_PATH)).thenReturn(file);
		Mockito.when(file.exists()).thenReturn(true);
		documentRepository.delete(opportunity, DOCUMENT_NAME);
		Mockito.verify(resourceOperations).file(FILE_PATH);
		Mockito.verify(file).delete();
	
	
	}
	
	@Test
	public final void deleteNotExists() {
		Mockito.when(resourceOperations.file(FILE_PATH)).thenReturn(file);
		
		documentRepository.delete(opportunity, DOCUMENT_NAME);
		Mockito.verify(resourceOperations).file(FILE_PATH);
		Mockito.verify(file, Mockito.never()).delete();
		
	}

}
