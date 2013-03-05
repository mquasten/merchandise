package de.mq.merchandise.contact.support;

import java.util.Collection;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class CheckAddressWithCoordinatesAwareValidator implements ConstraintValidator<CheckAddressWithCoordinatesAware, Collection<?>>  {

	
	@Override
	public void initialize(final CheckAddressWithCoordinatesAware constraintAnnotation) {
	}

	@Override
	public boolean isValid(final Collection<?> contacts, final ConstraintValidatorContext context) {
		for(final Object contact: contacts){
			
			if (contact instanceof AddressAO) {
				return true;
			}	
		}
		return false;
	}

}
