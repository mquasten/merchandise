package de.mq.merchandise.controller;

import java.util.HashMap;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import de.mq.merchandise.model.User;

@Component("message")
public class MessageControllerImpl  extends HashMap<String,String> {
	
	private static final long serialVersionUID = 1L;
	
	private final MessageSource messageSource;
	
	private final ApplicationContext ctx;
	
	@Autowired
	public MessageControllerImpl(final MessageSource messageSource, final ApplicationContext ctx){
		this.messageSource=messageSource;
		this.ctx=ctx;
	}
	

	@Override
	public final String get(final Object key) {
		
		return messageSource.getMessage(key.toString(), new Object[] { },  defaultValue(key) , new Locale( ctx.getBean(User.class).getLanguage()));
	}


	String defaultValue(final Object key) {
		return key + "?";
	}


	
	
	
	

}
