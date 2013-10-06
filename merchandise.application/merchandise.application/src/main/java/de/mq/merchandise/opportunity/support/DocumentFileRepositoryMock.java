package de.mq.merchandise.opportunity.support;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.ResourceAccessException;

import de.mq.merchandise.BasicEntity;
import de.mq.merchandise.opportunity.ResourceOperations;


@Repository
@Profile("mock")
public class DocumentFileRepositoryMock implements DocumentRepository {
	
	 static final String DOCUMENT_FOLDER = "/tmp/%s/%s";
	
	public static final String DOCUMENT_FILE = DOCUMENT_FOLDER+"/%s";
	
	private final  ResourceOperations resourceOperations;

	
	@Autowired
	public DocumentFileRepositoryMock(final ResourceOperations resourceOperations) {
		this.resourceOperations = resourceOperations;
	}

	@Override
	public void assign(final BasicEntity entity, final String name, final InputStream inputStream, final MediaType mediaType) {
		final File directory = resourceOperations.file(String.format(DOCUMENT_FOLDER, entity(entity), entity.id()));
		if( ! directory.exists() ) {
			directory.mkdirs();
		}
		
		
		try (final OutputStream outputStream =  resourceOperations.outputStream(String.format(DOCUMENT_FILE, entity(entity),  entity.id(), name))) {
			resourceOperations.copy(inputStream, outputStream);
		} catch (final IOException ex) {
			throw new  ResourceAccessException("Unable to close resource entity : " +  entity.id() + " attachement " + name , ex ); 
		}
		
	}

	private String entity(final BasicEntity entity) {
		if (entity instanceof Opportunity) {
			return OPPORTUNITIES_ENTITY;
		}
		
		if ( entity instanceof CommercialSubject){
			return  SUBJECTS_ENTITY;
		}
		
		throw new InvalidDataAccessApiUsageException("Not supported entity type for documents " + entity.getClass());
		
		
	}

	@Override
	public void delete(BasicEntity entity, String name) {
		final File file = resourceOperations.file(String.format(DOCUMENT_FILE,entity(entity), entity.id(), name));
		if( file.exists()){
			file.delete();
		}
	}

}
