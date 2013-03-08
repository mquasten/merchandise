package de.mq.merchandise.util;

import java.util.HashSet;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;


import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import de.mq.merchandise.customer.Customer;

public class ValidationServiceTest {
	
	private final Validator validator = Mockito.mock(Validator.class);
	private final Customer customer = Mockito.mock(Customer.class);
	private final Set<ConstraintViolation<Customer>> constraintViolations = new HashSet<>();
	@SuppressWarnings("unchecked")
	private final ConstraintViolation<Customer> constraintViolation = Mockito.mock(ConstraintViolation.class);
	private final ValidationService validationService = new BeanValidationServiceImpl(validator);
	
	@Before
	public final void setup() {
		Mockito.when(validator.validate(customer)).thenReturn(constraintViolations);
	}
	
	@Test(expected=ConstraintViolationException.class)
	public final void validateConstraintsViolated() {
		
		
		constraintViolations.add(constraintViolation);
		
		validationService.validate(customer);
		Mockito.verify(validator).validate(customer);
		System.out.println(validator);
	}
	
	@Test
	public final void validateNoContraintsViolated() {
		validationService.validate(customer);
		Mockito.verify(validator).validate(customer);
	}

}
