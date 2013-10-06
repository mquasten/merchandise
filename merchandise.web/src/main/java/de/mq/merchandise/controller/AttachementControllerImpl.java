package de.mq.merchandise.controller;

import java.io.FileNotFoundException;
import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Profile("db")
public class AttachementControllerImpl {
	
	
	
	
	
	@RequestMapping("/{entity}/{id}/{file}.{ext}")
	public String  content(@PathVariable String entity,@PathVariable Long id, @PathVariable String file, @PathVariable String ext, HttpServletResponse response) throws FileNotFoundException, IOException {
		
		return String.format("redirect:http://localhost:5984/%s/%s/%s.%s" , entity, id, file, ext); 
	    
	    
		
	}
	
	
	

}
