package de.mq.merchandise.model.support;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import de.mq.mapping.util.proxy.AOProxyFactory;
import de.mq.mapping.util.proxy.BeanResolver;
import de.mq.mapping.util.proxy.support.ModelRepositoryBuilderImpl;
import de.mq.merchandise.util.EntityUtil;


@Component
class WebProxyFactoryImpl implements WebProxyFactory  {

	
	private final AOProxyFactory proxyFactory;
	
	private final BeanResolver beanResolver;
	
	
	@Autowired
	WebProxyFactoryImpl(final AOProxyFactory proxyFactory, final BeanResolver beanResolver){
		this.proxyFactory=proxyFactory;
		this.beanResolver=beanResolver;
	}
	
	





	 /* (non-Javadoc)
	 * @see de.mq.merchandise.web.model.support.WebProxyFactoty#webModell(java.lang.Class, java.lang.Class)
	 */
	@Override
	public <Web> Web webModell(final Class<? extends Web> webClass, final Class<?> domainClass) {
		return webModell(webClass, EntityUtil.create(domainClass));
	}







	@Override
	public <Web> Web webModell(Class<? extends Web> webClass, Object domain) {
		// TODO Auto-generated method stub
		return proxyFactory.createProxy(webClass, new ModelRepositoryBuilderImpl().withBeanResolver(beanResolver).withDomain(domain).build());
	}
	

}