package de.mq.merchandise.opportunity.support;

import java.util.ArrayList;
import java.util.Collection;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import de.mq.mapping.util.proxy.AOProxyFactory;
import de.mq.mapping.util.proxy.BeanResolver;
import de.mq.mapping.util.proxy.support.BeanConventionCGLIBProxyFactory;
import de.mq.mapping.util.proxy.support.ModelRepositoryBuilderImpl;
import de.mq.mapping.util.proxy.support.SimpleReflectionBeanResolverImpl;
import de.mq.merchandise.util.EntityUtil;
import de.mq.merchandise.util.Paging;
import de.mq.merchandise.util.SimplePagingImpl;

public class CommercialSubjectsModelMappingTest {
	
	private static final int NUMBER_OF_PAGES = 100;

	private static final String PATTERN = "Pattern";

	private final AOProxyFactory proxyFactory = new  BeanConventionCGLIBProxyFactory();
	
	private final BeanResolver beanResolver =  new SimpleReflectionBeanResolverImpl(); 
	
	final Collection<CommercialSubject>  results = new ArrayList<>();
	final CommercialSubjectImpl commercialSubject = EntityUtil.create(CommercialSubjectImpl.class);
	final Paging paging = new SimplePagingImpl(50, "id");
	@Before
	public final void setup() {
		EntityUtil.setId(commercialSubject, 19680528L);
		results.add(commercialSubject);
	}
	
	@Test
	public final void toWeb() {
		
		
		
		final CommercialSubjectAO commercialSubjectAO =  Mockito.mock(CommercialSubjectAO.class);
		final CommercialSubjectsModelAO web = proxyFactory.createProxy(CommercialSubjectsModelAO.class, new ModelRepositoryBuilderImpl().withMapEntry("commercialSubjects", results).withBeanResolver(beanResolver).withMapEntry("paging", proxyFactory.createProxy(PagingAO.class, new ModelRepositoryBuilderImpl().withDomain(paging).withBeanResolver(beanResolver).build())).withMapEntry("selected", commercialSubjectAO).withMapEntry("pattern", PATTERN).build());
	
	    Assert.assertEquals(commercialSubjectAO, web.getSelected());
	    Assert.assertEquals(PATTERN, web.getPattern());
	    Assert.assertEquals(paging, web.getPaging().getPaging());
	    Assert.assertEquals(1, web.getCommercialSubjects().size());
	    Assert.assertEquals(""+ commercialSubject.id(), web.getCommercialSubjects().get(0).getId());
	}
	@Test
	public final void toDomain() {
		final CommercialSubjectAO selected =  Mockito.mock(CommercialSubjectAO.class);
		final CommercialSubjectsModelAO web = proxyFactory.createProxy(CommercialSubjectsModelAO.class, new ModelRepositoryBuilderImpl().withBeanResolver(beanResolver).withMapEntry("paging", proxyFactory.createProxy(PagingAO.class, new ModelRepositoryBuilderImpl().withDomain(paging).withBeanResolver(beanResolver).build())).build());;
	    web.getPaging().setCurrentPage(NUMBER_OF_PAGES);
	    web.setPattern(PATTERN);
	    web.setCommercialSubjects(results);
	    web.setSelected(selected);
	    Assert.assertEquals(NUMBER_OF_PAGES, web.getPaging().getPaging().currentPage());
	    web.getPaging().getPaging().assignRowCounter(60);
	    Assert.assertEquals(2, web.getPaging().getCurrentPage());
	    
	    Assert.assertEquals(1, web.getCommercialSubjects().size());
	    Assert.assertEquals(commercialSubject,  web.getCommercialSubjects().get(0).getCommercialSubject());
	    Assert.assertEquals(selected, web.getSelected());
	
	}

}
