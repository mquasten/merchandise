package de.mq.merchandise.controller;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.primefaces.event.FileUploadEvent;

import de.mq.merchandise.model.support.FacesContextFactory;
import de.mq.merchandise.opportunity.support.DocumentModelAO;
import de.mq.merchandise.opportunity.support.DocumentsAware;



class DocumentControllerImpl {
	
	
	private final ResourceOperations resourceOperations; 
	
	
	
	
	static final String UPLOAD_DOCUMENT_URL_REDIRECT = "uploadDocument.xhtml?faces-redirect=true&selectMode=true";
	static final String SHOW_DOCUMENT_URL_REDIRECT = "showDocument.xhtml?faces-redirect=true&selectMode=true";
	static final int MAX_HEIGHT = 800;
	static final int MAX_WIDTH = 1625;
	static final String URL_ROOT="http://localhost:5984/%s"; 

	
	private final FacesContextFactory facesContextFactory;
	
	DocumentControllerImpl(final FacesContextFactory facesContextFactory, final ResourceOperations resourceOperations) {
		this.facesContextFactory=facesContextFactory;
		this.resourceOperations=resourceOperations;
	}
	
	
	void handleFileUpload(final FileUploadEvent event) {

		File targetFolder = new File("c:\\tmp");
		try (final InputStream inputStream = event.getFile().getInputstream(); final OutputStream out = new FileOutputStream(new File(targetFolder, event.getFile().getFileName()))) {
			System.out.println(event.getFile().getFileName());
			int read = 0;
			byte[] bytes = new byte[1024];

			while ((read = inputStream.read(bytes)) != -1) {
				out.write(bytes, 0, read);
			}

			out.flush();
			
			System.out.println(event.getFile().getFileName() + "... finished");

		} catch (final IOException ex) {
			throw new IllegalArgumentException(ex);
		}

	}
	
	
	void addAttachement(final DocumentsAware document,  final String name ) {
		document.assignDocument(name);
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
		System.out.println(document);
		System.out.println(document.getClass());
		
		
		documentModelAO.setSelected(null);
		documentModelAO.setDocument(document);
		return UPLOAD_DOCUMENT_URL_REDIRECT;
	}
	
	
	void removeAttachement(final DocumentModelAO documentModelAO , final String name ) {
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
		
		
		documentModelAO.getDocument().assignWebLink(name);
		
		documentModelAO.setLink(null);
		
		
	}
	
	
	String showAttachement(final DocumentModelAO documentModelAO)  {
		
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
		}
			
	
	   return SHOW_DOCUMENT_URL_REDIRECT;
	}
	
	
	String cancelUpLoad(final String page){
		return page;
		
	}


}
