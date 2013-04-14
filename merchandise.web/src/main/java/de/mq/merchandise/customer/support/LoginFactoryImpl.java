
package de.mq.merchandise.customer.support;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import de.mq.mapping.util.proxy.AOProxyFactory;
import de.mq.mapping.util.proxy.BeanResolver;
import de.mq.mapping.util.proxy.support.ModelRepositoryBuilderImpl;

@Configuration
public class LoginFactoryImpl {
	
	
	@Autowired
	private AOProxyFactory proxyFactory;  
	
	@Autowired
	private BeanResolver beanResolver; 
	
	
	@Bean(name="login")
	@Scope("view")
	public LoginAO login() {
		  return   proxyFactory.createProxy(LoginAO.class, new ModelRepositoryBuilderImpl().withBeanResolver(beanResolver).build());
	}


}
