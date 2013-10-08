package de.mq.merchandise.controller;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Profile("db")
public class AttachementControllerImpl {
	
	
	
	
	
	static final String URL = "redirect:http://localhost:5984/%s/%s/%s.%s";

	@RequestMapping("/{entity}/{id}/{file}.{ext}")
	public String  content(@PathVariable final String entity,@PathVariable final Long id, @PathVariable final  String file, @PathVariable final String ext)  {
		
		return String.format(URL , entity, id, file, ext); 
	    
	    
		
	}
	
	
	

}
