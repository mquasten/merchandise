package de.mq.merchandise.controller;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;


import org.junit.Assert;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;
import org.springframework.http.MediaType;
import org.springframework.test.util.ReflectionTestUtils;

import com.sun.xml.bind.v2.model.core.ID;

import de.mq.merchandise.model.support.FacesContextFactory;
import de.mq.merchandise.opportunity.ResourceOperations;
import de.mq.merchandise.opportunity.support.DocumentModelAO;
import de.mq.merchandise.opportunity.support.DocumentService;
import de.mq.merchandise.opportunity.support.DocumentsAware;
import de.mq.merchandise.opportunity.support.OpportunityImpl;
import de.mq.merchandise.opportunity.support.Resource;

@SuppressWarnings("unused")
public class DocumentControllerTest {
	


	private static final byte[] CONTENT = "kylie is nice".getBytes();

	private static final long ID = 19680528L;

	private static final String CALL_UPLOAD_FROM = "opportunities.xhtml";

	private static final String WEB_LINK = "http://www.kylie.de";

	private static final String DOCUMENT_NAME = "kylie.jpg";

	private static final String CALL_SHOW_FROM = "document.xhtml";

	
	private final ResourceOperations resourceOperations = Mockito.mock(ResourceOperations.class);
	
	
	private final DocumentService documentService = Mockito.mock(DocumentService.class);
	
	private final DocumentControllerImpl documentController = new DocumentControllerImpl(documentService,resourceOperations);
	private final DocumentsAware documentsAware = Mockito.mock(DocumentsAware.class);
	private final DocumentModelAO documentModelAO = Mockito.mock(DocumentModelAO.class);
	
	
	
	
	@Test
	public final void addAttachement() throws IOException {
		final InputStream is = Mockito.mock(InputStream.class);
		final UploadedFile uploadedFile = Mockito.mock(UploadedFile.class);
		Mockito.when(uploadedFile.getFileName()).thenReturn(DOCUMENT_NAME);
		Mockito.when(uploadedFile.getInputstream()).thenReturn(is);
		documentController.addAttachement(documentsAware, uploadedFile);
		Mockito.verify(documentsAware).assignDocument(DOCUMENT_NAME);
		Mockito.verify(documentService).upload(documentsAware, uploadedFile.getFileName(), uploadedFile.getInputstream() , MediaType.IMAGE_JPEG_VALUE);
	}
	
	
	@Test
	public final void url() {
		String url = String.format("/opportunities/%s/%s", 19680528, DOCUMENT_NAME);
		Mockito.when(documentsAware.urlForName(DOCUMENT_NAME)).thenReturn(url);
		Assert.assertEquals( String.format(DocumentControllerImpl.URL_ROOT, url), documentController.url(documentsAware, DOCUMENT_NAME));
	}
	
	@Test
	public final void urlNotFound() {
		Mockito.when(documentsAware.urlForName(DOCUMENT_NAME)).thenReturn(null);
		Assert.assertNull(documentController.url(documentsAware, DOCUMENT_NAME));
	}
	
	
	
	@Test
	public final void urlWebLink() {
		Mockito.when(documentsAware.urlForName(DOCUMENT_NAME)).thenReturn(WEB_LINK);
		Assert.assertEquals(WEB_LINK, documentController.url(documentsAware, DOCUMENT_NAME));
	}
	
	
	
	@Test
	public final void removeAttachement() {
		Mockito.when(documentModelAO.getDocument()).thenReturn(documentsAware);
		documentController.removeAttachement(documentModelAO, DOCUMENT_NAME);
		Mockito.verify(documentModelAO,Mockito.atLeastOnce()).getDocument();
		Mockito.verify(documentsAware).removeDocument(DOCUMENT_NAME);
		Mockito.verify(documentModelAO).setSelected(null);
		
		
		
	}
	
	@Test
	public final void addLink() {
		Mockito.when(documentModelAO.getLink()).thenReturn(WEB_LINK);
		Mockito.when(documentModelAO.getDocument()).thenReturn(documentsAware);
		documentController.addLink(documentModelAO);
		
		Mockito.verify(documentModelAO, Mockito.atLeast(1)).getDocument();
		Mockito.verify(documentsAware).assignWebLink("kylie.de");
		Mockito.verify(documentModelAO).setLink(null);
		Mockito.verify(documentService).assignLink(documentModelAO.getDocument(), WEB_LINK);
		
		
	}
	
	@Test
	public final void addLinkNull() {
		Mockito.when(documentModelAO.getLink()).thenReturn(null);
		documentController.addLink(documentModelAO);
		Mockito.verify(documentModelAO).getLink();
		Mockito.verifyNoMoreInteractions(documentsAware, documentModelAO);
	}

	
	@Test
	public final void addLinkEmpty() {
		Mockito.when(documentModelAO.getLink()).thenReturn("    ");
		documentController.addLink(documentModelAO);
		Mockito.verify(documentModelAO, Mockito.times(2)).getLink();
		Mockito.verifyNoMoreInteractions(documentsAware, documentModelAO);
	}
	
	
	
	@Test
	public final void calculateSize() throws IOException  {
		final BufferedImage image = Mockito.mock(BufferedImage.class);
		Mockito.when(image.getWidth()).thenReturn(DocumentControllerImpl.MAX_WIDTH*5/3);
		Mockito.when(image.getHeight()).thenReturn(DocumentControllerImpl.MAX_HEIGHT);
		
		initForShowAttachements(image, DOCUMENT_NAME);
		
		documentController.calculateSize(documentModelAO);
		
		Mockito.verify(documentModelAO).setWidth(DocumentControllerImpl.MAX_WIDTH);
		Mockito.verify(documentModelAO).setHeight(DocumentControllerImpl.MAX_HEIGHT);
		//Mockito.verify(documentModelAO).setReturnFromShowAttachement(CALL_SHOW_FROM);
		
		
		Mockito.verify(documentModelAO).setWidth(DocumentControllerImpl.MAX_WIDTH);
		Mockito.verify(documentModelAO).setHeight(DocumentControllerImpl.MAX_HEIGHT*3/5);
		
		
	}
	
	
	@Test
	public final void calculateSizePdf() throws IOException  {
		initForShowAttachements(null, DOCUMENT_NAME.replaceFirst("jpg", "pdf"));
		documentController.calculateSize(documentModelAO);
		
		Mockito.verify(documentModelAO).setWidth(DocumentControllerImpl.MAX_WIDTH);
		Mockito.verify(documentModelAO).setHeight(DocumentControllerImpl.MAX_HEIGHT);

		
		Mockito.verifyZeroInteractions(resourceOperations);
	
	}
	
	@Test
	public final void calculateSizeNotSelected() throws IOException  {
		final BufferedImage image = Mockito.mock(BufferedImage.class);
		initForShowAttachements(image, null);
		documentController.calculateSize(documentModelAO);
		Mockito.verify(documentModelAO, Mockito.times(1)).setWidth(DocumentControllerImpl.MAX_WIDTH);
		Mockito.verify(documentModelAO,  Mockito.times(1)).setHeight(DocumentControllerImpl.MAX_HEIGHT);
	}

	private void initForShowAttachements(final BufferedImage bufferedImage, String documentName)  {
		String url = String.format("/opportunities/%s/%s", 19680528, documentName);
        final FacesContext facesContext = Mockito.mock(FacesContext.class);
		
		final UIViewRoot viewRoot = Mockito.mock(UIViewRoot.class);
		Mockito.when(facesContext.getViewRoot()).thenReturn(viewRoot);
		Mockito.when(viewRoot.getViewId()).thenReturn(CALL_SHOW_FROM);
		
		Mockito.when(documentModelAO.getDocument()).thenReturn(documentsAware);
		Mockito.when(documentModelAO.getSelected()).thenReturn(documentName);
		Mockito.when(documentsAware.urlForName(documentName)).thenReturn(url);
		
		Mockito.when(resourceOperations.readImage(String.format(DocumentControllerImpl.URL_ROOT, url))).thenReturn(bufferedImage);
		
	}
	
	@Test
	public final void calculateSizeNoImage()  {
		initForShowAttachements(null, DOCUMENT_NAME);
		
		documentController.calculateSize(documentModelAO);

		Mockito.verify(documentModelAO).setWidth(DocumentControllerImpl.MAX_WIDTH);
		Mockito.verify(documentModelAO).setHeight(DocumentControllerImpl.MAX_HEIGHT);
		
		
	}
	
	@Test
	public final void showAttachementSmalImage()  {
		final BufferedImage image = Mockito.mock(BufferedImage.class);
		Mockito.when(image.getWidth()).thenReturn(DocumentControllerImpl.MAX_WIDTH/2);
		Mockito.when(image.getHeight()).thenReturn(DocumentControllerImpl.MAX_HEIGHT/2);
		
		initForShowAttachements(image,DOCUMENT_NAME);
		
		documentController.calculateSize(documentModelAO);
		Mockito.verify(documentModelAO).setWidth(DocumentControllerImpl.MAX_WIDTH);
		Mockito.verify(documentModelAO).setHeight(DocumentControllerImpl.MAX_HEIGHT);
		
		
	}
	
	
	@Test
	public final void removeAll() {
		
		final Map<String,String> documents = new HashMap<>();
		documents.put(DOCUMENT_NAME, String.format("/" + Resource.Opportunity.urlPart() + "/%s/%s" , 19680528L , DOCUMENT_NAME));
		Mockito.when(documentsAware.documents()).thenReturn(documents);
		
		documentController.removeAll(documentsAware);
		
		
		Mockito.verify(documentService).delete(documentsAware, DOCUMENT_NAME);
		Mockito.verify(documentsAware).removeDocument(DOCUMENT_NAME);
	}
	
	
	@Test
	public final void init() {
		
		Mockito.when(documentModelAO.getDocument()).thenReturn(documentsAware);
		Mockito.when(documentsAware.id()).thenReturn(ID);
	
		Mockito.when(documentService.read(ID)).thenReturn(documentsAware);
		
		documentController.init(documentModelAO,ID , CALL_UPLOAD_FROM, DOCUMENT_NAME, CALL_SHOW_FROM);
		
		Mockito.verify(documentModelAO, Mockito.times(1)).setReturnFromUpload(CALL_UPLOAD_FROM);

		Mockito.verify(documentModelAO, Mockito.times(1)).setSelected(DOCUMENT_NAME);

		Mockito.verify(documentModelAO, Mockito.times(1)).setReturnFromShowAttachement(CALL_SHOW_FROM);

		Mockito.verify(documentModelAO, Mockito.times(1)).setDocument(documentsAware);
		
	}
	
	
	@Test
	public final void initWithoutId() {
		
		Mockito.when(documentModelAO.getDocument()).thenReturn(documentsAware);
		Mockito.when(documentsAware.id()).thenReturn(ID);
	
		
		
		documentController.init(documentModelAO, null , CALL_UPLOAD_FROM, DOCUMENT_NAME, CALL_SHOW_FROM);
		
		Mockito.verify(documentModelAO, Mockito.times(1)).setReturnFromUpload(CALL_UPLOAD_FROM);

		Mockito.verify(documentModelAO, Mockito.times(1)).setSelected(DOCUMENT_NAME);

		Mockito.verify(documentModelAO, Mockito.times(1)).setReturnFromShowAttachement(CALL_SHOW_FROM);

		Mockito.verify(documentService, Mockito.times(0)).read(Mockito.anyLong());
		
	}
	
	
	@Test
	public final void stream() throws IOException {
		Mockito.when(documentModelAO.getDocument()).thenReturn(documentsAware);
		final Map<String,String> documents = new HashMap<>();
		documents.put(DOCUMENT_NAME, String.format("/" + Resource.Opportunity.urlPart() + "/%s/%s" , 19680528L , DOCUMENT_NAME));
		Mockito.when(documentsAware.documents()).thenReturn(documents);
		Mockito.when(documentsAware.id()).thenReturn(ID);
		
		Mockito.when(documentService.document(ID)).thenReturn(CONTENT);
		
		StreamedContent result = (DefaultStreamedContent) documentController.stream(documentModelAO);
		
		byte[] buffer = new byte[CONTENT.length];
		

		Assert.assertEquals(CONTENT.length ,result.getStream().read(buffer, 0, CONTENT.length));
		Assert.assertArrayEquals(CONTENT,buffer);
		Assert.assertEquals(DOCUMENT_NAME, result.getName());
	}
	
	@Test(expected=IllegalArgumentException.class)
	public final void streamEmptyMap() throws IOException {
		Mockito.when(documentModelAO.getDocument()).thenReturn(documentsAware);
		final Map<String,String> documents = new HashMap<>();
		Mockito.when(documentsAware.documents()).thenReturn(documents);
		Mockito.when(documentsAware.id()).thenReturn(ID);
		
		documentController.stream(documentModelAO);
		
	}


}
