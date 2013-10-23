package de.mq.merchandise.opportunity.support;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map.Entry;

import junit.framework.Assert;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestOperations;

import de.mq.mapping.util.proxy.AOProxyFactory;
import de.mq.mapping.util.proxy.BeanResolver;
import de.mq.mapping.util.proxy.support.BeanConventionCGLIBProxyFactory;
import de.mq.mapping.util.proxy.support.ModelRepositoryBuilderImpl;
import de.mq.mapping.util.proxy.support.SimpleReflectionBeanResolverImpl;
import de.mq.merchandise.customer.support.CustomerImpl;
import de.mq.merchandise.opportunity.support.Opportunity.Kind;
import de.mq.merchandise.util.EntityUtil;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/documentRepository.xml" })
//@Ignore
public class DocumentIndexRepositoryIntegrationTest {
	
	@Autowired
	RestOperations restOperations;
	
	ObjectMapper mapper = new ObjectMapper();
	
	@Autowired
	ApplicationContext ctx; 

	
	@Test
	public final void revisionsForIds() {
		final DocumentIndexRepository documentIndexRepository = new  DocumentIndexRestRepositoryImpl(restOperations);
		final Collection<EntityContext> ids = new ArrayList<>();
		ids.add(new EntityContextImpl(19680528L, Resource.Opportunity));
		ids.add(new EntityContextImpl(4711L, Resource.Opportunity));
		ids.add(new EntityContextImpl(-1L, Resource.Opportunity));
		for(final Entry<Long,String> entry :  documentIndexRepository.revisionsforIds(ids).entrySet() ) {
			Assert.assertNotNull(entry.getValue());
			if( entry.getKey() == 19680528L){
				continue;
			}
			if( entry.getKey() == 4711L){
				continue;
			}
			Assert.fail("Wrong key:" +  entry.getKey());
		}
		
	}
	
	@Test
	public final void updateDocuments() throws JsonGenerationException, JsonMappingException, IOException {
		final DocumentIndexRepository documentIndexRepository = new  DocumentIndexRestRepositoryImpl(restOperations);
		final AOProxyFactory proxyFactory = new  BeanConventionCGLIBProxyFactory();
		final BeanResolver beanResolver = new SimpleReflectionBeanResolverImpl();
		final Opportunity opportunity = new OpportunityImpl(EntityUtil.create(CustomerImpl.class), "Pets for you", "Nicoles special escort service", Kind.ProductOrService);
		ReflectionTestUtils.setField(opportunity, "id", 4711L);
		
		final OpportunityIndexAO indexAO = proxyFactory.createProxy(OpportunityIndexAO.class, new ModelRepositoryBuilderImpl().withDomain(opportunity).withBeanResolver(beanResolver).build());
		
		Collection<EntityContext> entityContexts = new ArrayList<>();
	
		final EntityContext entityContext = new EntityContextImpl(4711L, Resource.Opportunity);

		entityContext.assign(OpportunityIndexAO.class, indexAO);
		
		entityContexts.add(entityContext);
		entityContexts.add(entityContext);
		
		final Collection<Long> processed = documentIndexRepository.updateDocuments(entityContexts);
		Assert.assertEquals(1, processed.size());
		Assert.assertEquals(4711L, (long) processed.iterator().next());
		
	}
	
	
	

}
