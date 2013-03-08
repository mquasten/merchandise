package de.mq.merchandise.util;



import java.util.HashSet;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BeanValidationServiceImpl implements ValidationService {

	private final Validator validator; 
	
	@Autowired
	public BeanValidationServiceImpl(final Validator validator){
		this.validator=validator;
	}
	
	/* (non-Javadoc)
	 * @see de.mq.merchandise.util.ValidationService#validate(java.lang.Object, java.lang.Class)
	 */
	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public final  void validate(final Object entity, Class<?> ... groups) {
		final Set<ConstraintViolation<Object>> constraintViolations = validator.validate(entity);
		if( constraintViolations.isEmpty()){
			return;
		}
		throw new ConstraintViolationException(new HashSet(constraintViolations));
	}
	
	

}
