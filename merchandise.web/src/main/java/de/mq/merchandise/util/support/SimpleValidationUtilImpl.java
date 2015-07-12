package de.mq.merchandise.util.support;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.server.UserError;
import com.vaadin.ui.AbstractComponent;

import de.mq.merchandise.util.ValidationUtil;



@Component
class SimpleValidationUtilImpl implements ValidationUtil {
	private final Validator validator;
	private final MessageSource messageSource;
	@Autowired
	SimpleValidationUtilImpl(final Validator validator, final MessageSource messageSource) {
		this.validator=validator;
		this.messageSource=messageSource;
	}
	
	/*
	 * (non-Javadoc)
	 * @see de.mq.merchandise.util.ValidationUtil#validate(java.lang.Object, com.vaadin.data.fieldgroup.FieldGroup, java.util.Locale)
	 */
	@Override
	public final <T> boolean  validate(final T source, final FieldGroup fieldGroup, final Locale locale) {
		final Map<String,Object> ids = new HashMap<>();
		fieldGroup.getBoundPropertyIds().forEach(id -> ids.put(id.toString().toUpperCase(), id));
		ids.values().forEach( p -> ((AbstractComponent) fieldGroup.getField(p)).setComponentError(null));
		final Set<ConstraintViolation<T>>  results =  validator.validate(source);
	
		final Set<Entry<Object,String>> errors = results.stream().filter(c -> ids.containsKey(c.getPropertyPath().toString().toUpperCase())).map(c -> new AbstractMap.SimpleEntry<Object,String>(ids.get(c.getPropertyPath().toString().toUpperCase()), c.getMessage())).collect(Collectors.toSet());
	
		errors.forEach(e -> ((AbstractComponent) fieldGroup.getField(e.getKey())).setComponentError(new UserError(messageSource.getMessage(e.getValue(), null, locale))));
	
	
		return errors.isEmpty();
		
	}
	
	@Override
	public final void reset(final FieldGroup fieldGroup) {
		fieldGroup.getBoundPropertyIds().forEach(p -> ((AbstractComponent) fieldGroup.getField(p)).setComponentError(null) );
	}

}
