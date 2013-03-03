
package de.mq.merchandise.controller;

import java.lang.reflect.Field;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.ReflectionUtils.FieldCallback;

import de.mq.merchandise.model.support.WebProxyFactory;

@Configuration
public class RegistrationWizardControllerFactory {
	
	
	@Autowired
	private WebProxyFactory webProxyFactory;
	
	@Autowired
	private ApplicationContext applicationContext;
	
	
	protected RegistrationWizardControllerFactory() {
		
	}
	
	
	
	
	@Bean()
	@Scope("singleton")
	public RegistrationWizardControllerImpl registrationWizardController() {
		
		  final RegistrationWizardControllerImpl controller = new RegistrationWizardControllerImpl();
		  inject(controller);
		  
		  final RegistrationWizardControllerImpl result =  webProxyFactory.webModell(RegistrationWizardControllerImpl.class, controller);
	      inject(result);
			
			
	       
		  return result;
	}




	private void inject(final Object result) {
		ReflectionUtils.doWithFields(result.getClass(), new FieldCallback(){

			@Override
			public void doWith(final Field field) throws IllegalArgumentException, IllegalAccessException {
			
				if(! field.isAnnotationPresent(Autowired.class)) {
					return;
				}
				
				
				field.setAccessible(true);
				
				if (field.getType().isInstance(applicationContext)){
					field.set(result, applicationContext);
					return;
				}
				field.set(result, applicationContext.getBean(field.getType()));
			}});
	}

}
