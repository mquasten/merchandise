package de.mq.merchandise.contact.support;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.*;



@Target( { METHOD, FIELD, ANNOTATION_TYPE })
@Retention(RUNTIME)
@Constraint(validatedBy = CheckAddressWithCoordinatesAwareValidator.class)
@Documented
public @interface CheckAddressWithCoordinatesAware {
	
	    String message() default "Missing at least one address,  with geo coordinates";

	    Class<?>[] groups() default {};

	    Class<? extends Payload>[] payload() default {};
}

