package de.mq.merchandise.customer.support;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import de.mq.merchandise.customer.CustomerBuilderFactory;
import de.mq.merchandise.model.support.WebProxyFactory;

@Configuration
public class CustomerProxyFactoryImpl {
	
	@Autowired
	private  WebProxyFactory webProxyFactory;
	
	@Bean(name="customer")
	@Scope("view")
	public CustomerAO customer() {
	   return webProxyFactory.webModell(CustomerAO.class, CustomerImpl.class);
	}
	
	@Bean(name="customerBuilderFactory")
	@Scope("singleton")
	public CustomerBuilderFactory customerBuilderFactory() {
		return new CustomerBuilderFactoryImpl();
	}
	

}
