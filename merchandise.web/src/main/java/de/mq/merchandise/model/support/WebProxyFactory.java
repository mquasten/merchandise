package de.mq.merchandise.model.support;

public interface WebProxyFactory {

	<Web> Web webModell(Class<? extends Web> webClass, Class<?> domainClass);
	
	<Web> Web webModell(Class<? extends Web> webClass, Object domain);

}