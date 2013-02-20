package de.mq.merchandise.contact.support;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import de.mq.merchandise.model.support.WebProxyFactory;

@Configuration
public class MessengerContactProxyFactoryImpl {
	
	@Autowired
	private   WebProxyFactory webProxyFactory;
	
	
	
	@Bean(name="messengerContact")
	@Scope("view")
	public MessengerContactAO messenger() {
		
		return webProxyFactory.webModell(MessengerContactAO.class, InstantMessengerContactImpl.class);
	}

}
