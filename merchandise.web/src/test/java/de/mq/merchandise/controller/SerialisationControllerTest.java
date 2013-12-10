package de.mq.merchandise.controller;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.springframework.util.StringUtils;

import de.mq.mapping.util.proxy.AOProxyFactory;
import de.mq.mapping.util.proxy.BeanResolver;
import de.mq.mapping.util.proxy.support.BeanConventionCGLIBProxyFactory;
import de.mq.mapping.util.proxy.support.ModelRepositoryBuilderImpl;
import de.mq.mapping.util.proxy.support.SimpleReflectionBeanResolverImpl;
import de.mq.merchandise.opportunity.support.PagingAO;
import de.mq.merchandise.rule.Rule;
import de.mq.merchandise.rule.support.RuleAO;
import de.mq.merchandise.rule.support.RuleModelAO;
import de.mq.merchandise.rule.support.RuleTestConstants;
import de.mq.merchandise.util.EntityUtil;
import de.mq.merchandise.util.SimplePagingImpl;

public class SerialisationControllerTest {
	
private static final String PATTERN = "Can' t get you out of my head, from Kylie";

private static final String RULE_ID = "19680528";

private static final int CURRENT_PAGE = 42;

private static final String[] PROPERTIES = new String[] {"paging.currentPage",  "selected.id", "pattern"};

private static final SimplePagingImpl PAGING = new SimplePagingImpl(10, "name, id");

private static final Rule RULE = RuleTestConstants.rule();



private final BeanResolver beanResolver =  new SimpleReflectionBeanResolverImpl();
	

	private final SerialisationControllerImpl serialisationController = new SerialisationControllerImpl();
	
	
	private final AOProxyFactory proxyFactory = new  BeanConventionCGLIBProxyFactory();
	
	private RuleModelAO ao; 

	
	@Before
	public final void setup() {
		EntityUtil.setId(RULE, null);
		PAGING.assignRowCounter(0);
		ao = proxyFactory.createProxy(RuleModelAO.class, new ModelRepositoryBuilderImpl().withMapEntry("selected", proxyFactory.createProxy(RuleAO.class, new ModelRepositoryBuilderImpl().withDomain(RULE).withBeanResolver(beanResolver).build())).withMapEntry("paging", proxyFactory.createProxy(PagingAO.class,  new ModelRepositoryBuilderImpl().withBeanResolver(beanResolver).withDomain(PAGING).build())).withBeanResolver(beanResolver).build());
	}
	
	@Test
	public final void serialize() throws Exception {
		PAGING.assignRowCounter(500L);
		PAGING.assignCurrentPage(CURRENT_PAGE);
		ao.setPattern(PATTERN);
		ao.getSelected().setId(RULE_ID);
		
		Assert.assertEquals(string("" +CURRENT_PAGE , RULE_ID, PATTERN ), serialisationController.serialize(ao, PROPERTIES));
	}
	
	@Test(expected=IllegalStateException.class)
	public final void serializeWrongProperty() {
		serialisationController.serialize(ao, new String[] {"dontLetMeGetMe"});
	}

	@Test
	public final void serializePropertiesFromAnnotation(){
	
		PAGING.assignRowCounter(500L);
		PAGING.assignCurrentPage(CURRENT_PAGE);
		ao.setPattern(PATTERN);
		ao.getSelected().setId(RULE_ID);
		Assert.assertEquals(string("" +CURRENT_PAGE , RULE_ID ,PATTERN  ), serialisationController.serialize(ao));
	}
	
	private String string(final String ... args){
		return StringUtils.arrayToDelimitedString(args, SerialisationControllerImpl.DELIMITER);
		
	}
	
	@Test
	public final void deserialize(){
	    Assert.assertEquals( 1, (int)  ao.getPaging().getCurrentPage());
	    Assert.assertNull(ao.getSelected().getId());
	    Assert.assertNull(ao.getPattern());
	    
	    serialisationController.deserialize(ao, string("" + CURRENT_PAGE, RULE_ID, PATTERN), PROPERTIES);
	    
	   Assert.assertEquals(PATTERN, ao.getPattern());
	   Assert.assertEquals(CURRENT_PAGE, ao.getPaging().getCurrentPage());
	   Assert.assertEquals(RULE_ID, ao.getSelected().getId());
	}
	
	@Test(expected=IllegalStateException.class)
	public final void deserializeWrongProperty() {
		serialisationController.deserialize(ao, string("" + CURRENT_PAGE, PATTERN, RULE_ID), new String[]{"dontLetMeGetMe"});
	}
	
	@Test
	public final void deserializeFromAnnotation() {
		   Assert.assertEquals( 1, (int)  ao.getPaging().getCurrentPage());
		    Assert.assertNull(ao.getSelected().getId());
		    Assert.assertNull(ao.getPattern());
		    
		    serialisationController.deserialize(ao, string("" + CURRENT_PAGE, RULE_ID, PATTERN));
		    
		   Assert.assertEquals(PATTERN, ao.getPattern());
		   Assert.assertEquals(CURRENT_PAGE, ao.getPaging().getCurrentPage());
		   Assert.assertEquals(RULE_ID, ao.getSelected().getId());
	}
	
}
