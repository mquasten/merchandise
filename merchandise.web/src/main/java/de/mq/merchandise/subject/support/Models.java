package de.mq.merchandise.subject.support;

import java.lang.reflect.Field;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.util.ReflectionUtils;

import de.mq.merchandise.customer.CustomerService;
import de.mq.merchandise.subject.Subject;

@Configuration
public class Models {
	
	@Autowired
	private CustomerService customerService;
	
	@Bean
	@Scope("session")
	public Subject subject() {
		final Subject subject = new SubjectImpl(customerService.customer(Optional.of(1L)), "XXX");
		final Field field = ReflectionUtils.findField(subject.getClass(),"name");
		field.setAccessible(true);
		ReflectionUtils.setField(field, subject, null);
		return subject;
		
		
	}

}
