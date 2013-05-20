package de.mq.merchandise.opportunity.support;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import de.mq.merchandise.model.support.WebProxyFactory;
import de.mq.merchandise.util.Paging;
import de.mq.merchandise.util.SimplePagingImpl;

@Configuration
public class PagingFactoryImpl {
	
	@Autowired
	private final WebProxyFactory webProxyFactory;
	
	PagingFactoryImpl(){
		this.webProxyFactory=null;
	}
	
	PagingFactoryImpl(final WebProxyFactory webProxyFactory) {
		this.webProxyFactory=webProxyFactory;
	}
	
	@Bean(name="paging")
	@Scope("view")
	
	public PagingAO naturalPerson() {
		final Paging paging =  new SimplePagingImpl(10, "id");
		return webProxyFactory.webModell(PagingAO.class, paging);
	}

}
