package de.mq.merchandise.opportunity.support;

import javax.persistence.EntityManager;

import junit.framework.Assert;

import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.dao.InvalidDataAccessApiUsageException;

import de.mq.merchandise.rule.Rule;
import de.mq.merchandise.rule.support.RuleImpl;
import de.mq.merchandise.util.EntityUtil;

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
	
	@Test
	public final void defaultConstructor() {
		Assert.assertNotNull(new DocumentEntityRepositoryImpl());
	}
	
	@Test
	public final void forIdAllOpportunity(){
	
		final Opportunity opportunity = EntityUtil.create(OpportunityImpl.class);
		EntityUtil.setId(opportunity, ID);
		Mockito.when(entityManager.find(OpportunityImpl.class, ID)).thenReturn((OpportunityImpl) opportunity);
		
		Assert.assertEquals(opportunity, documentEntityRepository.forId(ID)); 
	}
	
	@Test
	public final void forIdAllSubject(){
	
		final CommercialSubjectImpl subject = EntityUtil.create(CommercialSubjectImpl.class);
		EntityUtil.setId(opportunity, ID);
		Mockito.when(entityManager.find(CommercialSubjectImpl.class, ID)).thenReturn(subject);
		
		Assert.assertEquals(subject, documentEntityRepository.forId(ID)); 
	}
	
	@Test
	public final void forIdAllRule(){
	
		final Rule rule = EntityUtil.create(RuleImpl.class);
		EntityUtil.setId(rule, ID);
		Mockito.when(entityManager.find(RuleImpl.class, ID)).thenReturn((RuleImpl) rule);
		
		Assert.assertEquals(rule, documentEntityRepository.forId(ID)); 
	}
	
	@Test(expected=InvalidDataAccessApiUsageException.class)
	public final void forIdAllNotFound(){
		documentEntityRepository.forId(ID);
	}
	
	
	@Test
	public final void delete() {
		final Opportunity opportunity = EntityUtil.create(OpportunityImpl.class);
		EntityUtil.setId(opportunity, ID);
		Mockito.when(entityManager.find(OpportunityImpl.class, ID)).thenReturn((OpportunityImpl) opportunity);
		
		documentEntityRepository.delete(ID);
		
		Mockito.verify(entityManager).remove(opportunity);
	}
	
}
