
package de.mq.merchandise.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import de.mq.mapping.util.proxy.BeanResolver;
import de.mq.merchandise.customer.CustomerService;
import de.mq.merchandise.model.support.Conversation;
import de.mq.merchandise.model.support.WebProxyFactory;
import de.mq.merchandise.util.ValidationService;

@Configuration
public class RegistrationWizardControllerFactory {
	
	
	@Autowired
	private WebProxyFactory webProxyFactory;
	
	@Autowired
	private CustomerService customerService;
	
    @Autowired
	private BeanResolver beanResolver;
	
	
    @Autowired
    private ValidationService validationService;
    
    @Autowired
    private Conversation conversation;
	
	
	protected RegistrationWizardControllerFactory() {
		
	}
		
	@Bean(name="registrationWizardController")
	@Scope("singleton")
	public RegistrationWizardControllerImpl registrationWizardController() {
		  return webProxyFactory.webModell(RegistrationWizardControllerImpl.class, new RegistrationWizardControllerImpl(customerService, beanResolver, validationService, conversation));
	}

}
