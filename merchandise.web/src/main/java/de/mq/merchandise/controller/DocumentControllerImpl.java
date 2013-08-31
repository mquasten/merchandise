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
		System.out.println("******************************");
		System.out.println(url);
		System.out.println("******************************");
		if( url == null){
			return "" ;
		}
		
		if(url.trim().toLowerCase().startsWith("http")){
			return url;
		}
		
		return String.format(URL_ROOT, url);
	}
	
	
	void assign(final DocumentModelAO documentModelAO, final DocumentsAware document) {
		documentModelAO.setSelected(null);
		documentModelAO.setDocument(document);
	}
	
	
	void removeAttachement(final DocumentModelAO documentModelAO , final String name ) {
		documentModelAO.getDocument().removeDocument(name);
		documentModelAO.setSelected(null);
	}
	
	void addLink(final DocumentModelAO documentModelAO) {
		
		System.out.println( documentModelAO.getSelected());
		
		if (documentModelAO.getSelected() == null){
			return;
		}
		
		if (documentModelAO.getSelected().trim().length() == 0 ) {
			return;
		}
		
		final String name = documentModelAO.getSelected().trim().replaceFirst("(http|HTTP).*[.]", "");
		
		documentModelAO.getDocument().assignWebLink(name);
		System.out.println(documentModelAO.getDocument().urlForName(name));
		documentModelAO.setSelected(null);
		System.out.println(documentModelAO.getDocument().urlForName(name));
		
	}
	
	
	void size(final String name , final DocumentModelAO documentModelAO){
		System.out.println(name);
		
			try {
				BufferedImage bufferedImage = ImageIO.read(new URL(url(documentModelAO.getDocument(), name)));
				final double scale = Math.max(scale(bufferedImage.getHeight(), 800), scale(bufferedImage.getWidth(), 1600));
				
				documentModelAO.setWidth(  new Double(bufferedImage.getWidth() /scale).intValue());
				
				documentModelAO.setHeight(new Double(bufferedImage.getHeight()/scale).intValue());
				
				
			} catch (Exception e) {
				documentModelAO.setWidth(1600);
				documentModelAO.setHeight(800);
			}
	
	}


	private double scale(int size, int max) {
		
		
		if(size > max){
			return (double)size/max;
		}
		return 1.0d;
	}

}
