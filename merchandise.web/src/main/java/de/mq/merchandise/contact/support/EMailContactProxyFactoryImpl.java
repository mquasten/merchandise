package de.mq.merchandise.contact.support;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import de.mq.merchandise.model.support.WebProxyFactory;

@Configuration
public class EMailContactProxyFactoryImpl {
	
	@Autowired
	private   WebProxyFactory webProxyFactory;
	
	
	
	@Bean(name="emailContact")
	@Scope("view")
	public EMailContactAO email() {
		return webProxyFactory.webModell(EMailContactAO.class, EMailContactImpl.class);
	}

}
