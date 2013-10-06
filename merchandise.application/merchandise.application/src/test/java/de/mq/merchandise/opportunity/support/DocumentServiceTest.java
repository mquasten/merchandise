package de.mq.merchandise.opportunity.support;

import java.io.InputStream;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;


public class DocumentServiceTest {
	
	
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

}
