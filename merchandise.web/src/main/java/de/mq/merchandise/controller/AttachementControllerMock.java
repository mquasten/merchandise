package de.mq.merchandise.controller;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import de.mq.merchandise.opportunity.ResourceOperations;
import de.mq.merchandise.opportunity.support.DocumentFileRepositoryMock;

@Controller
@Profile("mock")
public class AttachementControllerMock {
	
	
	private final ResourceOperations resourceOperations;
	
	@Autowired
	public AttachementControllerMock(ResourceOperations resourceOperations) {
		this.resourceOperations = resourceOperations;
	}

	@RequestMapping("/{entity}/{id}/{file}.{ext}")
	public void  content(@PathVariable String entity,@PathVariable Long id, @PathVariable String file, @PathVariable String ext, HttpServletResponse response) throws FileNotFoundException, IOException {
		System.out.println("************Controller**************");
	    try ( final InputStream inputStream = resourceOperations.inputStream(String.format(DocumentFileRepositoryMock.DOCUMENT_FILE, entity, id, file+ "." + ext)); final OutputStream outputStream = response.getOutputStream(); ) {
			resourceOperations.copy(inputStream, outputStream);
	    }
	    
		
	}
	
	
	

}
