package de.mq.merchandise.opportunity.support;

import junit.framework.Assert;

import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.dao.InvalidDataAccessApiUsageException;

import de.mq.merchandise.BasicRepository;
import de.mq.merchandise.util.BasicService;
import de.mq.merchandise.util.BasicServiceImpl;

public class BasicServiceTest {
	
	private static final long ID = 19680528L;
	
	@SuppressWarnings("unchecked")
	private final BasicRepository<CommercialSubject, Long> basicRepository = Mockito.mock(BasicRepository.class);
	
	private final BasicService<CommercialSubject> basicService = new BasicServiceImpl<>(basicRepository);
	
	private  final CommercialSubject commercialSubject = Mockito.mock(CommercialSubject.class);
	
	@Test
	public final void createOrUpdate() {
		Mockito.when(basicRepository.save(commercialSubject)).thenReturn(commercialSubject);
		Assert.assertEquals(commercialSubject, basicService.createOrUpdate(commercialSubject));
		Mockito.verify(basicRepository).save(commercialSubject);
	}
	
	@Test
	public final void delete() {
		basicService.delete(commercialSubject);
		Mockito.verify(basicRepository).delete(commercialSubject.id());
	}
	
	@Test
	public final void subject() {
		Mockito.when(basicRepository.forId(ID)).thenReturn(commercialSubject);
		Assert.assertEquals(commercialSubject, basicService.read(ID));
		Mockito.verify(basicRepository).forId(ID);
	}
	
	@Test(expected=InvalidDataAccessApiUsageException.class)
	public final void subjectNotFound() {
		basicService.read(ID);
	}


}
