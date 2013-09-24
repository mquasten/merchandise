package de.mq.merchandise.opportunity.support;

import javax.persistence.EntityManager;

import junit.framework.Assert;

import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.dao.InvalidDataAccessApiUsageException;

public class DocumentEntityRepositoryTest {
	
	
	private static final Class<Opportunity> CLAZZ = Opportunity.class;

	private static final long ID = 19680528L;

	private EntityManager entityManager = Mockito.mock(EntityManager.class);
	
	private DocumentEntityRepository documentEntityRepository = new DocumentEntityRepositoryImpl(entityManager);
	
	private final Opportunity opportunity = Mockito.mock(Opportunity.class);
	
	@Test
	public final void forId() {
		Mockito.when(entityManager.find(CLAZZ, ID)).thenReturn( opportunity);
		
		Assert.assertEquals(opportunity, documentEntityRepository.forId(ID, CLAZZ));
	}
	
	@Test(expected=InvalidDataAccessApiUsageException.class)
	public final void forIdNotFound() {
		documentEntityRepository.forId(ID, CLAZZ);
	}
	
	@Test
	public final void save() {
		documentEntityRepository.save(opportunity);
		Mockito.verify(entityManager).merge(opportunity);
	}

}
