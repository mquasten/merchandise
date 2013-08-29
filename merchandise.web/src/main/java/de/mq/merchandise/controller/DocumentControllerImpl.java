package de.mq.merchandise.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.primefaces.event.FileUploadEvent;

import de.mq.merchandise.opportunity.support.DocumentsAware;
import de.mq.merchandise.opportunity.support.Opportunity;
import de.mq.merchandise.opportunity.support.OpportunityAO;



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
	
	
	void addAttachement(final OpportunityAO opportunityAO, final Opportunity selected, final String name ) {
		
		opportunityAO.setOpportunity(selected);
		System.out.println("******************************");
		System.out.println(opportunityAO.getOpportunity().id());
		System.out.println("******************************");
		opportunityAO.getOpportunity().assignDocument(name);
		
	}
	
	
	String url(final  DocumentsAware documentAware, final String name) {
		
		System.out.println("url:" +String.format(URL_ROOT, documentAware.urlForName(name)));
		return String.format(URL_ROOT, documentAware.urlForName(name));
	}

}
