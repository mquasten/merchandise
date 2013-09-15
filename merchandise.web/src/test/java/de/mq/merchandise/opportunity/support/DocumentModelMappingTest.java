package de.mq.merchandise.opportunity.support;

import java.util.List;

import junit.framework.Assert;

import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;

import de.mq.mapping.util.proxy.AOProxyFactory;
import de.mq.mapping.util.proxy.BeanResolver;
import de.mq.mapping.util.proxy.support.BeanConventionCGLIBProxyFactory;
import de.mq.mapping.util.proxy.support.ModelRepositoryBuilderImpl;
import de.mq.mapping.util.proxy.support.SimpleReflectionBeanResolverImpl;
import de.mq.merchandise.customer.Customer;

public class DocumentModelMappingTest {
	
	
	private static final String FROM_SHOW_URL = "/upLoadattachement.xhml";

	private static final String FROM_UPDLOAD_URL = "/opportunity.xhtml";

	private static final int HEIGHT = 800;

	private static final int WIDTH = 1024;

	private static final String LINK = "kylie.de";

	private static final String SELECTED = "kylie.com";

	private static final String DOCUMENT = "kylie.jpg";

	private final AOProxyFactory proxyFactory = new  BeanConventionCGLIBProxyFactory();
	
	private final BeanResolver beanResolver =  new SimpleReflectionBeanResolverImpl(); 
	
	final Customer customer = Mockito.mock(Customer.class);
	
	private final Opportunity opportunity = new OpportunityImpl(customer, "opportunity name", "opportunity description", Opportunity.Kind.Offer);
	
	@Test
	public final void toWeb() {
		
		
		ReflectionTestUtils.setField(opportunity, "id", 19680528L);
		opportunity.assignDocument(DOCUMENT);
		
	    final DocumentModelAO web = proxyFactory.createProxy(DocumentModelAO.class, new ModelRepositoryBuilderImpl().withMapEntry("document", opportunity).withMapEntry("selected", SELECTED).withMapEntry("link", LINK).withMapEntry("width", WIDTH).withMapEntry("height", HEIGHT).withMapEntry("returnFromUpload", FROM_UPDLOAD_URL).withMapEntry("returnFromShowAttachement", FROM_SHOW_URL). withBeanResolver(beanResolver).build());
	    @SuppressWarnings("unchecked")
	    final List<String> documents = (List<String>) web.getDocuments();
	    Assert.assertEquals(1, documents.size());
	    Assert.assertEquals(DOCUMENT, documents.get(0));
	    Assert.assertEquals(opportunity, web.getDocument());
	    Assert.assertEquals(SELECTED, web.getSelected());
	    Assert.assertEquals(LINK, web.getLink());
	    Assert.assertEquals(WIDTH, (int) web.getWidth());
	    Assert.assertEquals(HEIGHT, (int) web.getHeight());
	    Assert.assertEquals(FROM_UPDLOAD_URL, web.getReturnFromUpload());
	    Assert.assertEquals(FROM_SHOW_URL, web.getReturnFromShowAttachement());
	
	}
	
	@Test
	public final void toDomain() {
		 final DocumentModelAO web = proxyFactory.createProxy(DocumentModelAO.class, new ModelRepositoryBuilderImpl(). withBeanResolver(beanResolver).build());
		 web.setDocument(opportunity);
		 web.setHeight(HEIGHT);
		 web.setWidth(WIDTH);
		 web.setSelected(SELECTED);
		 web.setLink(LINK);
		 web.setReturnFromShowAttachement(FROM_SHOW_URL);
		 web.setReturnFromUpload(FROM_UPDLOAD_URL);
		 
		 
		 Assert.assertEquals(opportunity, web.getDocument());
		 Assert.assertEquals(WIDTH, (int) web.getWidth());
		 Assert.assertEquals(HEIGHT, (int) web.getHeight());
		 
		 Assert.assertEquals(SELECTED, web.getSelected());
		 Assert.assertEquals(LINK, web.getLink());
		
		 Assert.assertEquals(FROM_UPDLOAD_URL, web.getReturnFromUpload());
		 Assert.assertEquals(FROM_SHOW_URL, web.getReturnFromShowAttachement());
		 
		 
		 
	}
	

}
