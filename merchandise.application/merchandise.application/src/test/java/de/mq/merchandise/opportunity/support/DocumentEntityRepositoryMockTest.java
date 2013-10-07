package de.mq.merchandise.opportunity.support;

import junit.framework.Assert;

import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.test.util.ReflectionTestUtils;

import de.mq.merchandise.util.EntityUtil;

public class DocumentEntityRepositoryMockTest {
	
	
	private static final long ID = 19680528L;
	
	
	private final OpportunityRepository opportunityRepository = Mockito.mock(OpportunityRepository.class);
	private final CommercialSubjectRepository commercialSubjectRepository = Mockito.mock(CommercialSubjectRepository.class);
	
	private DocumentEntityRepository documentEntityRepository = new DocumentEntityRepositoryMock(opportunityRepository, commercialSubjectRepository);
	private final Opportunity opportunity =  EntityUtil.create(OpportunityImpl.class);
	private final CommercialSubject subject =  EntityUtil.create(CommercialSubjectImpl.class);
	
	@Test
	public final void forIdOpportunitiy() {
		
		    Mockito.when(opportunityRepository.forId(ID)).thenReturn(opportunity);
		
			Assert.assertEquals(opportunity, documentEntityRepository.forId(ID, OpportunityImpl.class));
			Mockito.verify(opportunityRepository).forId(ID);
	}
	
	@Test
	public final void forIdOSubject() {
		
		    Mockito.when(commercialSubjectRepository.forId(ID)).thenReturn(subject);
		
			Assert.assertEquals(subject, documentEntityRepository.forId(ID, CommercialSubjectImpl.class));
			Mockito.verify(commercialSubjectRepository).forId(ID);
	}
	
	
	@Test(expected=InvalidDataAccessApiUsageException.class)
	public final void forIdRepositoryNotFound() {
		documentEntityRepository.forId(ID, Opportunity.class);
	}

	
	@Test
	public final void saveOpportunity() {
		ReflectionTestUtils.setField(opportunity, "id", ID);
		documentEntityRepository.save(opportunity);
		Mockito.verify(opportunityRepository).save(opportunity);
		
		
		
	}
	
	@Test
	public final void saveSubject() {
		ReflectionTestUtils.setField(subject, "id", ID);
		documentEntityRepository.save(subject);
		Mockito.verify(commercialSubjectRepository).save(subject);
	}
	
	@Test
	public final void defaultconstructor() {
		Assert.assertNotNull(new DocumentEntityRepositoryMock());
	}

}
