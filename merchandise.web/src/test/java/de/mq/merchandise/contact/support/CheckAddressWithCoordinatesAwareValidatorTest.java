package de.mq.merchandise.contact.support;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

public class CheckAddressWithCoordinatesAwareValidatorTest {
	
	private final ConstraintValidator<CheckAddressWithCoordinatesAware, Collection<?>> constraintValidator = new CheckAddressWithCoordinatesAwareValidator();
	
	@Test
	public final void validateAddressAware(){
		
		
		final List<AddressAO> addresses = new ArrayList<>();
		addresses.add(Mockito.mock(AddressAO.class));
		Assert.assertTrue(constraintValidator.isValid(addresses, Mockito.mock(ConstraintValidatorContext.class)));
	}
	
	
	@Test
	public final void validateAddressNotAware(){
		final List<PostBoxAO> addresses = new ArrayList<>();
		addresses.add(Mockito.mock(PostBoxAO.class));
		Assert.assertFalse(constraintValidator.isValid(addresses, Mockito.mock(ConstraintValidatorContext.class)));
		
	}
	@Test
	public final void initialize() {
		constraintValidator.initialize(Mockito.mock(CheckAddressWithCoordinatesAware.class));
	}

}
