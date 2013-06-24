package de.mq.merchandise.opportunity.support;

import javax.persistence.EntityManager;

import junit.framework.Assert;

import org.junit.Test;
import org.mockito.Mockito;

import de.mq.merchandise.BasicRepository;

public class BasicRepositoryTest {
	

	private static final long ID = 19680528L;

	

	private EntityManager entityManager = Mockito.mock(EntityManager.class); 
	
	private final BasicRepository<CommercialSubject, Long>  basicRepository = new CommercialSubjectRepositoryImpl(entityManager, null);
	
	
	@Test
	public final void save() {
		final CommercialSubject commercialSubject = Mockito.mock(CommercialSubject.class);
		Mockito.when(entityManager.merge(commercialSubject)).thenReturn(commercialSubject);
		Assert.assertEquals(commercialSubject, basicRepository.save(commercialSubject));
		Mockito.verify(entityManager).merge(commercialSubject);
		
	}
	
	@Test
	public final void delete() {
		final CommercialSubjectImpl commercialSubject = Mockito.mock(CommercialSubjectImpl.class);
		Mockito.when(commercialSubject.hasId()).thenReturn(true);
		Mockito.when(commercialSubject.id()).thenReturn(ID);
		Mockito.when(entityManager.find( CommercialSubjectImpl.class ,ID)).thenReturn( commercialSubject);
		
		basicRepository.delete(commercialSubject.id());
		
		Mockito.verify(entityManager).remove(commercialSubject);
	}
	
	@Test
	public final void deleteNotFound() {
		final CommercialSubject commercialSubject = Mockito.mock(CommercialSubject.class);
		Mockito.when(commercialSubject.hasId()).thenReturn(true);
		Mockito.when(commercialSubject.id()).thenReturn(ID);
		
		basicRepository.delete(commercialSubject.id());
		
		Mockito.verify(entityManager).find(CommercialSubjectImpl.class, ID);
		Mockito.verifyNoMoreInteractions(entityManager);
		
	}
	
	
	@Test
	public final void forId() {
		final CommercialSubjectImpl result = Mockito.mock(CommercialSubjectImpl.class);
		Mockito.when(entityManager.find(CommercialSubjectImpl.class, ID)).thenReturn(result);
		Assert.assertEquals(result, basicRepository.forId(ID));
		Mockito.verify(entityManager).find(CommercialSubjectImpl.class, ID);
	}

}
