package de.mq.merchandise.contact.support;

import java.util.Collection;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class CheckLoginAwareValidator implements ConstraintValidator<CheckLoginAware, Collection<?>>  {

	
	@Override
	public void initialize(final CheckLoginAware constraintAnnotation) {
		
		
	}

	@Override
	public boolean isValid(final Collection<?> contacts, final ConstraintValidatorContext context) {
		for(final Object contact: contacts){
			
			if ((contact instanceof PhoneContactAO) &&( ((PhoneContactAO)contact).getLoginContact() )){
				return true;
			}
			if ((contact instanceof MessengerContactAO) &&( ((MessengerContactAO)contact).getLoginContact())) {
				return true;
			}
	  
			if ((contact instanceof EMailContactAO) && ( ((EMailContactAO)contact).getLoginContact())) {
				return true;
			}
		
		}
		return false;
	}

	
	
	
}
