package de.mq.merchandise.controller;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

import javax.imageio.ImageIO;

import org.primefaces.event.FileUploadEvent;

import de.mq.merchandise.opportunity.support.DocumentModelAO;
import de.mq.merchandise.opportunity.support.DocumentsAware;



class DocumentControllerImpl {
	
	static final int MAX_HEIGHT = 800;
	static final int MAX_WIDTH = 1650;
	static final String URL_ROOT="http://localhost:5984/%s"; 

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
			return "         " ;
		}
		
		if(url.trim().toLowerCase().startsWith("http")){
			return url;
		}
		System.out.println(String.format(URL_ROOT, url));
		return String.format(URL_ROOT, url);
	}
	
	
	String assign(final DocumentModelAO documentModelAO, final DocumentsAware document) {
		documentModelAO.setSelected(null);
		documentModelAO.setDocument(document);
		System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!1");
		return "uploadDocument.xhtml";
	}
	
	
	void removeAttachement(final DocumentModelAO documentModelAO , final String name ) {
		documentModelAO.getDocument().removeDocument(name);
		documentModelAO.setSelected(null);
	}
	
	void addLink(final DocumentModelAO documentModelAO) {
		
		if (documentModelAO.getSelected() == null){
			return;
		}
		
		if (documentModelAO.getSelected().trim().length() == 0 ) {
			return;
		}
		
		final String name = documentModelAO.getSelected().trim().replaceFirst("(http|HTTP).*[.]", "");
		
		documentModelAO.getDocument().assignWebLink(name);
		
		documentModelAO.setSelected(null);
		
		
	}
	
	
	String size(final String name , final DocumentModelAO documentModelAO){
		if(name.toLowerCase().endsWith("jpg")|| name.toLowerCase().endsWith("jepg")||name.toLowerCase().endsWith("png")||name.toLowerCase().endsWith("tif")||name.toLowerCase().endsWith("gif") ){
				documentModelAO.setWidth(null);
				documentModelAO.setHeight(null);
		}else {
			documentModelAO.setWidth(MAX_WIDTH);
			documentModelAO.setHeight(MAX_HEIGHT);
		}
				
		
			
	   return "showDocument.xhtml?faces-redirect=true&selectMode=true";
	}


}
