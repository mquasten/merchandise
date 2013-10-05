package de.mq.merchandise.opportunity.support;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import junit.framework.Assert;

import org.junit.Test;

import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.test.util.ReflectionTestUtils;

import de.mq.merchandise.util.EntityUtil;

public class DocumentEntityRepositoryMockTest {
	
	
	private static final long ID = 19680528L;
	private DocumentEntityRepository documentEntityRepository = new DocumentEntityRepositoryMock();
	private final Opportunity opportunity =  EntityUtil.create(OpportunityImpl.class);
	
	@Test
	public final void forId() {
		final Map <UUID,DocumentsAware> storedDocuments = new HashMap<>();
		final UUID uuid = new UUID(ID, OpportunityImpl.class.hashCode());
		
		
		storedDocuments.put(uuid, opportunity);
		ReflectionTestUtils.setField(documentEntityRepository, "storedDocuments", storedDocuments);
		
		
		Assert.assertEquals(opportunity, documentEntityRepository.forId(19680528L, OpportunityImpl.class));
	}
	
	
	@Test(expected=InvalidDataAccessApiUsageException.class)
	public final void forIdNotFound() {
		documentEntityRepository.forId(ID, OpportunityImpl.class);
	}

	
	@Test
	public final void save() {
		ReflectionTestUtils.setField(opportunity, "id", ID);
		documentEntityRepository.save(opportunity);
		
		@SuppressWarnings("unchecked")
		final Map<UUID,DocumentsAware> storedDocuments = (Map<UUID, DocumentsAware>) ReflectionTestUtils.getField(documentEntityRepository, "storedDocuments");
	    Assert.assertEquals(1, storedDocuments.size()) ;
	    
	    Assert.assertEquals(new UUID(ID, OpportunityImpl.class.hashCode()), storedDocuments.keySet().iterator().next());
	    Assert.assertEquals(opportunity, storedDocuments.values().iterator().next());
	}
}
