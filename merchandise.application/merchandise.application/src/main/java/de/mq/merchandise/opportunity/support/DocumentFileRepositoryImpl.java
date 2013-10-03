package de.mq.merchandise.opportunity.support;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.ResourceAccessException;

import de.mq.merchandise.BasicEntity;

@Repository
@Profile("mock")
public class DocumentFileRepositoryImpl implements DocumentRepository {
	
	static final String DOCUMENT_FOLDER = "/tmp/%s";
	
	static final String DOCUMENT_FILE = DOCUMENT_FOLDER+"/%s";
	
	private final  ResourceOperations resourceOperations;

	
	@Autowired
	public DocumentFileRepositoryImpl(final ResourceOperations resourceOperations) {
		this.resourceOperations = resourceOperations;
	}

	@Override
	public void assign(final BasicEntity entity, final String name, final InputStream inputStream, final MediaType mediaType) {
		final File directory = resourceOperations.file(String.format(DOCUMENT_FOLDER, entity.id()));
		if( ! directory.exists() ) {
			directory.mkdirs();
		}
		
		
		try (final OutputStream outputStream =  resourceOperations.outputStream(String.format(DOCUMENT_FILE, entity.id(), name))) {
			resourceOperations.copy(inputStream, outputStream);
		} catch (final IOException ex) {
			throw new  ResourceAccessException("Unable to close resource entity : " +  entity.id() + " attachement " + name , ex ); 
		}
		
	}

	@Override
	public void delete(BasicEntity entity, String name) {
		final File file = resourceOperations.file(String.format(DOCUMENT_FILE, entity.id(), name));
		if( file.exists()){
			file.delete();
		}
	}

}
