package de.mq.merchandise.controller;

import java.awt.image.BufferedImage;

import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;

import junit.framework.Assert;

import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;

import de.mq.merchandise.model.support.FacesContextFactory;
import de.mq.merchandise.opportunity.support.DocumentModelAO;
import de.mq.merchandise.opportunity.support.DocumentsAware;

public class DocumentControllerTest {
	
	private static final String CALL_UPLOAD_FROM = "opportunities.xhtml";

	private static final String WEB_LINK = "http://www.kylie.de";

	private static final String DOCUMENT_NAME = "kylie.jpg";

	private static final String CALL_SHOW_FROM = "document.xhtml";

	private final FacesContextFactory facesContextFactory = Mockito.mock(FacesContextFactory.class);
	
	private final ResourceOperations resourceOperations = Mockito.mock(ResourceOperations.class);
	
	private final DocumentControllerImpl documentController = new DocumentControllerImpl(facesContextFactory,resourceOperations);
	private final DocumentsAware documentsAware = Mockito.mock(DocumentsAware.class);
	private final DocumentModelAO documentModelAO = Mockito.mock(DocumentModelAO.class);
	
	
	@Test
	public final void constrctor() {
		Assert.assertEquals(facesContextFactory, ReflectionTestUtils.getField(documentController, "facesContextFactory"));
	}
	
	@Test
	public final void addAttachement() {
		documentController.addAttachement(documentsAware, DOCUMENT_NAME);
		Mockito.verify(documentsAware).assignDocument(DOCUMENT_NAME);
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
	public final void assign() {
		
		
		final FacesContext facesContext = Mockito.mock(FacesContext.class);
		final UIViewRoot uiViewRoot = Mockito.mock(UIViewRoot.class);
		Mockito.when(facesContext.getViewRoot()).thenReturn(uiViewRoot);
		Mockito.when(uiViewRoot.getViewId()).thenReturn(CALL_UPLOAD_FROM);
		Mockito.when(facesContextFactory.facesContext()).thenReturn(facesContext);
		Assert.assertEquals(DocumentControllerImpl.UPLOAD_DOCUMENT_URL_REDIRECT, documentController.assign(documentModelAO, documentsAware));
		
		Mockito.verify(documentModelAO).setReturnFromUpload(CALL_UPLOAD_FROM);
		Mockito.verify(documentModelAO).setSelected(null);
		Mockito.verify(documentModelAO).setDocument(documentsAware);
		
		
	}
	
	@Test
	public final void removeAttachement() {
		Mockito.when(documentModelAO.getDocument()).thenReturn(documentsAware);
		documentController.removeAttachement(documentModelAO, DOCUMENT_NAME);
		Mockito.verify(documentModelAO).getDocument();
		Mockito.verify(documentsAware).removeDocument(DOCUMENT_NAME);
		Mockito.verify(documentModelAO).setSelected(null);
		
		
		
	}
	
	@Test
	public final void addLink() {
		Mockito.when(documentModelAO.getLink()).thenReturn(WEB_LINK);
		Mockito.when(documentModelAO.getDocument()).thenReturn(documentsAware);
		documentController.addLink(documentModelAO);
		
		Mockito.verify(documentModelAO).getDocument();
		Mockito.verify(documentsAware).assignWebLink("kylie.de");
		Mockito.verify(documentModelAO).setLink(null);
		
		
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
	public final void cancelUpload() {
		Assert.assertEquals(CALL_UPLOAD_FROM, documentController.cancelUpLoad(CALL_UPLOAD_FROM));
	}
	
	@Test
	public final void showAttachement()  {
		final BufferedImage image = Mockito.mock(BufferedImage.class);
		Mockito.when(image.getWidth()).thenReturn(DocumentControllerImpl.MAX_WIDTH*5/3);
		Mockito.when(image.getHeight()).thenReturn(DocumentControllerImpl.MAX_HEIGHT);
		
		initForShowAttachements(image);
		
		Assert.assertEquals(DocumentControllerImpl.SHOW_DOCUMENT_URL_REDIRECT, documentController.showAttachement(documentModelAO));
		
		Mockito.verify(documentModelAO).setWidth(DocumentControllerImpl.MAX_WIDTH);
		Mockito.verify(documentModelAO).setHeight(DocumentControllerImpl.MAX_HEIGHT);
		Mockito.verify(documentModelAO).setReturnFromShowAttachement(CALL_SHOW_FROM);
		
		
		Mockito.verify(documentModelAO).setWidth(DocumentControllerImpl.MAX_WIDTH);
		Mockito.verify(documentModelAO).setHeight(DocumentControllerImpl.MAX_HEIGHT*3/5);
		
		
	}

	private void initForShowAttachements(final BufferedImage bufferedImage)  {
		String url = String.format("/opportunities/%s/%s", 19680528, DOCUMENT_NAME);
        final FacesContext facesContext = Mockito.mock(FacesContext.class);
		Mockito.when(facesContextFactory.facesContext()).thenReturn(facesContext);
		final UIViewRoot viewRoot = Mockito.mock(UIViewRoot.class);
		Mockito.when(facesContext.getViewRoot()).thenReturn(viewRoot);
		Mockito.when(viewRoot.getViewId()).thenReturn(CALL_SHOW_FROM);
		
		Mockito.when(documentModelAO.getDocument()).thenReturn(documentsAware);
		Mockito.when(documentModelAO.getSelected()).thenReturn(DOCUMENT_NAME);
		Mockito.when(documentsAware.urlForName(DOCUMENT_NAME)).thenReturn(url);
		Mockito.when(resourceOperations.readImage(String.format(DocumentControllerImpl.URL_ROOT, url))).thenReturn(bufferedImage);
		
	}
	
	@Test
	public final void showAttachementNoImage()  {
		initForShowAttachements(null);
		
		Assert.assertEquals(DocumentControllerImpl.SHOW_DOCUMENT_URL_REDIRECT, documentController.showAttachement(documentModelAO));

		Mockito.verify(documentModelAO).setWidth(DocumentControllerImpl.MAX_WIDTH);
		Mockito.verify(documentModelAO).setHeight(DocumentControllerImpl.MAX_HEIGHT);
		Mockito.verify(documentModelAO).setReturnFromShowAttachement(CALL_SHOW_FROM);
		
	}
	
	@Test
	public final void showAttachementSmalImage()  {
		final BufferedImage image = Mockito.mock(BufferedImage.class);
		Mockito.when(image.getWidth()).thenReturn(DocumentControllerImpl.MAX_WIDTH/2);
		Mockito.when(image.getHeight()).thenReturn(DocumentControllerImpl.MAX_HEIGHT/2);
		
		initForShowAttachements(image);
		
		Assert.assertEquals(DocumentControllerImpl.SHOW_DOCUMENT_URL_REDIRECT, documentController.showAttachement(documentModelAO));
		Mockito.verify(documentModelAO).setWidth(DocumentControllerImpl.MAX_WIDTH);
		Mockito.verify(documentModelAO).setHeight(DocumentControllerImpl.MAX_HEIGHT);
		Mockito.verify(documentModelAO).setReturnFromShowAttachement(CALL_SHOW_FROM);
		
	}

}
