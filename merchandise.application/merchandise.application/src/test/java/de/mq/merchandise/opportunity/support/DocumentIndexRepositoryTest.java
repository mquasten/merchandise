package de.mq.merchandise.opportunity.support;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map.Entry;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestOperations;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/documentRepository.xml" })
public class DocumentIndexRepositoryTest {
	
	@Autowired
	RestOperations restOperations;
	

	
	@Test
	public final void revisionsForIds() {
		final DocumentIndexRepository documentIndexRepository = new DocumentIndexRestRepositoryImpl(restOperations);
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

}
