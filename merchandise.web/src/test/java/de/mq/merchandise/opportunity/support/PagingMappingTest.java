package de.mq.merchandise.opportunity.support;

import junit.framework.Assert;

import org.junit.Test;

import de.mq.mapping.util.proxy.AOProxyFactory;
import de.mq.mapping.util.proxy.BeanResolver;
import de.mq.mapping.util.proxy.support.BeanConventionCGLIBProxyFactory;
import de.mq.mapping.util.proxy.support.ModelRepositoryBuilderImpl;
import de.mq.mapping.util.proxy.support.SimpleReflectionBeanResolverImpl;
import de.mq.merchandise.util.Paging;
import de.mq.merchandise.util.SimplePagingImpl;

public class PagingMappingTest {
	private static final int MAX_PAGES = 3;

	private static final int PAGE_SIZE = 50;

	private static final int CURRENT_PAGE = 2;

	private final AOProxyFactory proxyFactory = new  BeanConventionCGLIBProxyFactory();
	
	private final BeanResolver beanResolver =  new SimpleReflectionBeanResolverImpl(); 
	
	private Paging paging = new SimplePagingImpl(PAGE_SIZE, "id");
	
	@Test
	public final void toWeb() {
		paging.assignRowCounter(110);
		paging.assignCurrentPage(CURRENT_PAGE);
		final PagingAO web  = proxyFactory.createProxy(PagingAO.class, new ModelRepositoryBuilderImpl().withBeanResolver(beanResolver).withDomain(paging).build());
	    Assert.assertEquals(CURRENT_PAGE, web.getCurrentPage());
	    Assert.assertEquals(MAX_PAGES, web.getMaxPages());
	    Assert.assertEquals(PAGE_SIZE, web.getPageSize());
	    Assert.assertEquals(paging, web.getPaging());
	}
	@Test
	public final void toDomain() {
		final PagingAO web  = proxyFactory.createProxy(PagingAO.class, new ModelRepositoryBuilderImpl().withBeanResolver(beanResolver).withDomain(paging).build());
		paging.assignRowCounter(110);
		Assert.assertEquals(1, paging.currentPage());
		web.setCurrentPage(CURRENT_PAGE);
		Assert.assertEquals(CURRENT_PAGE, paging.currentPage());
	}

}
