package de.mq.merchandise.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

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
		return String.format(URL_ROOT, documentAware.urlForName(name));
	}
	
	
	void assign(final DocumentModelAO documentModelAO, final DocumentsAware document) {
		documentModelAO.setSelected(null);
		documentModelAO.setDocument(document);
	}
	
	
	void removeAttachement(final DocumentsAware document , final String name ) {
		document.removeDocument(name, DocumentsAware.DocumentType.Link);
	}

}
