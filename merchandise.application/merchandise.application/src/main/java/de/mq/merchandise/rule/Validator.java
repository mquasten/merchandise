package de.mq.merchandise.rule;

import java.util.Locale;

import org.springframework.context.MessageSource;

public interface Validator<T> extends ParameterNamesAware<T> {
	
	boolean validate(final T object);
		
	String message(final MessageSource messagesource, final Locale locale);

}
