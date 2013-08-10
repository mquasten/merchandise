package de.mq.merchandise.opportunity.support;

import org.junit.Assert;
import org.junit.Test;

import de.mq.mapping.util.proxy.AOProxyFactory;
import de.mq.mapping.util.proxy.BeanResolver;
import de.mq.mapping.util.proxy.support.BeanConventionCGLIBProxyFactory;
import de.mq.mapping.util.proxy.support.ModelRepositoryBuilderImpl;
import de.mq.mapping.util.proxy.support.SimpleReflectionBeanResolverImpl;

public class KeyWordModelTest {
	
	private static final String NEW_SELECTED_KEYWORD = "newSelectedKeyWord";

	private static final String NEW_KEYWORD = "newKeyWord";

	private final BeanResolver beanResolver =  new SimpleReflectionBeanResolverImpl(); 
	
	static final String KEYWORD = "keyWord";
	static final String SELECTED_KEYWORD = "selectedKeyWord";
	
	private final AOProxyFactory proxyFactory = new  BeanConventionCGLIBProxyFactory();
	@Test
	public final void model() {
		final KeyWordModelAO web = proxyFactory.createProxy(KeyWordModelAO.class, new ModelRepositoryBuilderImpl().withBeanResolver(beanResolver).withMapEntry("keyWord", KEYWORD).withMapEntry("selectedKeyWord", SELECTED_KEYWORD).build());
	
	    Assert.assertEquals(KEYWORD, web.getKeyWord());
	    Assert.assertEquals(SELECTED_KEYWORD, web.getSelectedKeyWord());
	    
	    web.setKeyWord(NEW_KEYWORD);
	    web.setSelectedKeyWord(NEW_SELECTED_KEYWORD);
	    
	    Assert.assertEquals(NEW_KEYWORD, web.getKeyWord());
	    Assert.assertEquals(NEW_SELECTED_KEYWORD, web.getSelectedKeyWord());
	    
	    
	}

}
