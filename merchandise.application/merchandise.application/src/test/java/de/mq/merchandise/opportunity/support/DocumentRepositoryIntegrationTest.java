package de.mq.merchandise.opportunity.support;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.util.ReflectionTestUtils;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"/documentRepository.xml"})
public class DocumentRepositoryIntegrationTest {
	
	@Autowired
	private DocumentRepository documentRepository; 
	
	@Test
	public final void revision() {
		Assert.assertNotNull(documentRepository);

		final Opportunity opportunity = new OpportunityImpl();
		ReflectionTestUtils.setField(opportunity, "id", 4711L);
		final String rev = documentRepository.revisionFor(opportunity);
		Assert.assertNotNull(rev);
		for (int i = 0; i < 10; i++) {
			Assert.assertEquals(rev, documentRepository.revisionFor(opportunity));
		}

	}

}
