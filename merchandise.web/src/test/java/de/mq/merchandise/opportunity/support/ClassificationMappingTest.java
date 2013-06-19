package de.mq.merchandise.opportunity.support;

import junit.framework.Assert;

import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;

import de.mq.mapping.util.proxy.AOProxyFactory;
import de.mq.mapping.util.proxy.BeanResolver;
import de.mq.mapping.util.proxy.support.BeanConventionCGLIBProxyFactory;
import de.mq.mapping.util.proxy.support.ModelRepositoryBuilderImpl;
import de.mq.mapping.util.proxy.support.SimpleReflectionBeanResolverImpl;

public class ClassificationMappingTest {
	
	private static final String DESCRIPTION = "description";

	private static final String ID = "activity01";

	private static final String PARENT_ID = "parent01";

	private final BeanResolver beanResolver =  new SimpleReflectionBeanResolverImpl();
	
	private final AOProxyFactory proxyFactory = new  BeanConventionCGLIBProxyFactory();
	
	@Test
	public final void toWeb() {
		final Classification parent = new ActivityClassificationImpl();
		ReflectionTestUtils.setField(parent, "id", PARENT_ID);
		
		final Classification classification = new ActivityClassificationImpl();
		ReflectionTestUtils.setField(classification, "id", ID);
		ReflectionTestUtils.setField(classification, DESCRIPTION, DESCRIPTION);
		ReflectionTestUtils.setField(classification, "parent", parent);
		
	    final ActivityClassificationAO web = proxyFactory.createProxy(ActivityClassificationAO.class, new ModelRepositoryBuilderImpl().withDomain(classification).withBeanResolver(beanResolver).build());

	    Assert.assertEquals(ID, web.getId());
	    Assert.assertEquals(DESCRIPTION, web.getDescription());
	    Assert.assertEquals(PARENT_ID, web.getParent().getId());
	    Assert.assertEquals(classification, web.getActivityClassification());
	    
	}
}
