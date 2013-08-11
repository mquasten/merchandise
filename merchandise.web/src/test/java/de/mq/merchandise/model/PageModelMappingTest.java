package de.mq.merchandise.model;

import org.junit.Test;

import junit.framework.Assert;
import de.mq.mapping.util.proxy.AOProxyFactory;
import de.mq.mapping.util.proxy.BeanResolver;
import de.mq.mapping.util.proxy.support.BeanConventionCGLIBProxyFactory;
import de.mq.mapping.util.proxy.support.ModelRepositoryBuilderImpl;
import de.mq.mapping.util.proxy.support.SimpleReflectionBeanResolverImpl;



public class PageModelMappingTest {
	
	private final BeanResolver beanResolver =  new SimpleReflectionBeanResolverImpl();

	private final AOProxyFactory proxyFactory = new  BeanConventionCGLIBProxyFactory();
	
	@Test
	public final void model() {
		
		final PageModelAO web = proxyFactory.createProxy(PageModelAO.class, new ModelRepositoryBuilderImpl().withMapEntry("selectMode", false).withBeanResolver(beanResolver).build());
	    Assert.assertFalse(web.getSelectMode());
	    
	    web.setSelectMode(true);
	    Assert.assertTrue(web.getSelectMode());
	    
	}

}
