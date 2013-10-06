package de.mq.merchandise.controller;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import org.primefaces.model.UploadedFile;

import de.mq.merchandise.model.support.FacesContextFactory;
import de.mq.merchandise.opportunity.ResourceOperations;
import de.mq.merchandise.opportunity.support.DocumentModelAO;
import de.mq.merchandise.opportunity.support.DocumentService;
import de.mq.merchandise.opportunity.support.DocumentsAware;



class DocumentControllerImpl {
	
	
	private final ResourceOperations resourceOperations; 
	
	
	private final DocumentService documentService;
	
	
	static final String UPLOAD_DOCUMENT_URL_REDIRECT = "uploadDocument.xhtml?faces-redirect=true&selectMode=true";
	static final String SHOW_DOCUMENT_URL_REDIRECT = "showDocument.xhtml?faces-redirect=true&selectMode=true";
	static final int MAX_HEIGHT = 800;
	static final int MAX_WIDTH = 1625;
	static final String URL_ROOT="http://localhost:8080/merchandise.web/attachements/%s"; 

	
	private final FacesContextFactory facesContextFactory;
	
	DocumentControllerImpl(final FacesContextFactory facesContextFactory, final DocumentService documentService, final ResourceOperations resourceOperations) {
		this.facesContextFactory=facesContextFactory;
		this.documentService=documentService;
		this.resourceOperations=resourceOperations;
	}
	
	
	
	
	void addAttachement(final DocumentsAware document, final UploadedFile file ) throws IOException  {
		System.out.println("addAttachement...");
		try(final InputStream inputStream = file.getInputstream()) {
			documentService.upload(document, file.getFileName(), inputStream, file.getContentType());
			document.assignDocument(file.getFileName());
		} 
	}
	
	
	String url(final  DocumentsAware documentAware, final String name) {
		final String url = documentAware.urlForName(name);
		
		if( url == null){
			return null ;
		}
		
		if(url.trim().toLowerCase().startsWith("http")){
			return url;
		}
		return String.format(URL_ROOT, url);
	}
	
	
	String assign(final DocumentModelAO documentModelAO, final DocumentsAware document) {
		
		documentModelAO.setReturnFromUpload(facesContextFactory.facesContext().getViewRoot().getViewId());	
		documentModelAO.setSelected(null);
		documentModelAO.setDocument(document);
		
		return UPLOAD_DOCUMENT_URL_REDIRECT;
	}
	
	
	void removeAttachement(final DocumentModelAO documentModelAO , final String name ) {
		documentService.delete(documentModelAO.getDocument(), name);
		documentModelAO.getDocument().removeDocument(name);
		documentModelAO.setSelected(null);
	}
	
	void addLink(final DocumentModelAO documentModelAO) {
		if (documentModelAO.getLink() == null){
			return;
		}
		
		if (documentModelAO.getLink().trim().length() == 0 ) {
			return;
		}
		
		final String name = documentModelAO.getLink().trim().replaceFirst("(http|HTTP)([:]//){0,1}([wW]{3}[.]){0,1}",  "");
		
		
		documentService.assignLink(documentModelAO.getDocument(), name);
		
		documentModelAO.getDocument().assignWebLink(name);
		
		documentModelAO.setLink(null);
		
		
	}
	
	
	String showAttachement(final DocumentModelAO documentModelAO)   {
		
		documentModelAO.setWidth(MAX_WIDTH);
		documentModelAO.setHeight(MAX_HEIGHT);	
		documentModelAO.setReturnFromShowAttachement(facesContextFactory.facesContext().getViewRoot().getViewId());
	
		final BufferedImage image =resourceOperations.readImage(url(documentModelAO.getDocument(), documentModelAO.getSelected() ));
		if ( image == null ){
			
			return SHOW_DOCUMENT_URL_REDIRECT;
		}
			
		
		if(image.getWidth()> MAX_WIDTH) {
			final double scale = ((double)MAX_WIDTH) / image.getWidth();
			documentModelAO.setWidth((int) (image.getWidth()*scale));
			documentModelAO.setHeight((int) (image.getHeight()*scale));
		} else {
			documentModelAO.setWidth(image.getWidth());
			documentModelAO.setHeight(image.getHeight());
		}
			
	
	   return SHOW_DOCUMENT_URL_REDIRECT;
	}


	
	
	
	String cancelUpLoad(final String page){
		return page;
		
	}


}
