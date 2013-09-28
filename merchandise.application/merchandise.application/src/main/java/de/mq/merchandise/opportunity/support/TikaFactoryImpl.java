package de.mq.merchandise.opportunity.support;

import org.apache.tika.Tika;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration 
public class TikaFactoryImpl {
	
	@Bean()
	public Tika tika() {
		return new Tika();
		
	}
	

}
