package de.mq.merchandise.opportunity.support;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.http.MediaType;


public class DocumentServiceTest {
	
	
	private static final byte[] DOCUMENT_CONTENT = "KinkyKylie".getBytes();

	private static final String DOCUMENT_NAME = "kylie-doll.jpg";

	private static final long ID = 19680528L;

	private final DocumentRepository documentRepository = Mockito.mock(DocumentRepository.class);
	
	private final DocumentEntityRepository documentEntityRepository = Mockito.mock(DocumentEntityRepository.class);
	
	private final DocumentService documentService = new DocumentServiceImpl(documentRepository, documentEntityRepository);
	
	private final DocumentsAware document = Mockito.mock(DocumentsAware.class);
	
	final DocumentsAware documentFromDatabase = Mockito.mock(DocumentsAware.class);
	
	@Before
	public final void setup() {
		Mockito.when(document.id()).thenReturn(ID);
		Mockito.when(documentEntityRepository.forId(ID, document.getClass())).thenReturn(documentFromDatabase);
	}
	
	@Test
	public final void upload() {
		final InputStream inputStream = Mockito.mock(InputStream.class);
		
		documentService.upload(document, DOCUMENT_NAME, inputStream, MediaType.IMAGE_JPEG_VALUE);
		
		Mockito.verify(documentRepository).assign(documentFromDatabase, DOCUMENT_NAME, inputStream, MediaType.IMAGE_JPEG);
		Mockito.verify(documentFromDatabase).assignDocument(DOCUMENT_NAME);
		Mockito.verify(documentEntityRepository).save(documentFromDatabase);
		
	}
	
	@Test
	public final void addLink() {
		documentService.assignLink(document, DOCUMENT_NAME);
		
		Mockito.verify(documentFromDatabase).assignWebLink(DOCUMENT_NAME);
		Mockito.verify(documentEntityRepository).save(documentFromDatabase);
		
	}
	
	@Test
	public final void delete() {
		documentService.delete(document, DOCUMENT_NAME);
		
		Mockito.verify(documentRepository).delete(documentFromDatabase, DOCUMENT_NAME);
		Mockito.verify(documentFromDatabase).removeDocument(DOCUMENT_NAME);
		Mockito.verify(documentEntityRepository).save(documentFromDatabase);
	}
	
	@Test
	public final void document() {
		Mockito.when(documentRepository.document(documentFromDatabase, DOCUMENT_NAME)).thenReturn(DOCUMENT_CONTENT);
		Assert.assertArrayEquals(DOCUMENT_CONTENT, documentService.document(ID, document.getClass(), DOCUMENT_NAME));
	}
	
	@Test
	//like a prayer ...
	public final void documentTryGuessType() {
		Mockito.when(documentEntityRepository.forId(ID)).thenReturn(documentFromDatabase);
		final Map<String,String> documents = new HashMap<>();
		documents.put(DOCUMENT_NAME, "dontLetMeGetMe");
		Mockito.when(documentFromDatabase.documents()).thenReturn(documents);
		Mockito.when(documentRepository.document(documentFromDatabase, DOCUMENT_NAME)).thenReturn(DOCUMENT_CONTENT);
		
		Assert.assertArrayEquals(DOCUMENT_CONTENT, documentService.document(ID));
	}
	
	@Test(expected=InvalidDataAccessApiUsageException.class)
	public final void documentTryGuessTypeDocumentNotAware() {
		Mockito.when(documentEntityRepository.forId(ID)).thenReturn(documentFromDatabase);
		Mockito.when(documentFromDatabase.documents()).thenReturn(new HashMap<String, String>());
		
		
		Assert.assertArrayEquals(DOCUMENT_CONTENT, documentService.document(ID));
	}

}
