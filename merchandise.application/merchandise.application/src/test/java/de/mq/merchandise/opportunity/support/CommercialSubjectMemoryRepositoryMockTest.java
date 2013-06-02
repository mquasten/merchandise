package de.mq.merchandise.opportunity.support;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import junit.framework.Assert;

import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;

import de.mq.merchandise.customer.Customer;
import de.mq.merchandise.customer.support.CustomerMemoryReposioryMock;
import de.mq.merchandise.util.Paging;



public class CommercialSubjectMemoryRepositoryMockTest {
	
	private static final Long ID = 19680528L;
	private final CommercialSubjectRepository commercialSubjectRepository = new CommercialSubjectMemoryRepositoryMock(new CustomerMemoryReposioryMock());
	
	
	@Test
	public final void save() {
		
		final CommercialSubject commercialSubject = Mockito.mock(CommercialSubject.class);
		Mockito.when(commercialSubject.hasId()).thenReturn(true);
		Mockito.when(commercialSubject.id()).thenReturn(ID);
		final CommercialSubject result =  commercialSubjectRepository.save(commercialSubject);
		Assert.assertEquals(commercialSubject, result);
		Assert.assertEquals((long) ID, result.id());
		
		@SuppressWarnings("unchecked")
		final Map<Long,CommercialSubject> results = (Map<Long, CommercialSubject>) ReflectionTestUtils.getField(commercialSubjectRepository, "commercialSubjects");
		
		Assert.assertEquals(1, results.size());
		
		Assert.assertEquals(ID, results.keySet().iterator().next());
		Assert.assertEquals(commercialSubject, results.values().iterator().next());
		Assert.assertEquals(commercialSubject.id(), results.values().iterator().next().id());
		
	}
	
	@Test
	public final void saveNew() {
		final CommercialSubject commercialSubject = new CommercialSubjectImpl(Mockito.mock(Customer.class), "name", "description");
		Assert.assertFalse(commercialSubject.hasId());
		
		final CommercialSubject result =  commercialSubjectRepository.save(commercialSubject);
	    Assert.assertTrue(result.id() > 0L);
		Assert.assertEquals(commercialSubject, result);
		
		@SuppressWarnings("unchecked")
		final Map<Long,CommercialSubject> results = (Map<Long, CommercialSubject>) ReflectionTestUtils.getField(commercialSubjectRepository, "commercialSubjects");
		Assert.assertEquals(1, results.size());
		Assert.assertEquals(commercialSubject, results.get(result.id()));
	}
	
	
	@Test
	public final void search() {
		@SuppressWarnings("unchecked")
		final Map<Long,CommercialSubject> results = (Map<Long, CommercialSubject>) ReflectionTestUtils.getField(commercialSubjectRepository, "commercialSubjects");
	    final Customer customer = Mockito.mock(Customer.class);
		for(long i=0; i < 100; i++){
	    	final CommercialSubject commercialSubject = Mockito.mock(CommercialSubject.class);
	    	Mockito.when(commercialSubject.hasId()).thenReturn(true);
	    	Mockito.when(commercialSubject.id()).thenReturn(i+1);
	    	Mockito.when(commercialSubject.name()).thenReturn("product" + numberString(i));
	    	Mockito.when(commercialSubject.description()).thenReturn("product" +  numberString(i) );
	    	Mockito.when(commercialSubject.customer()).thenReturn(customer);
	    	results.put(i, commercialSubject);
	    }
		final ArgumentCaptor<Long> counter = ArgumentCaptor.forClass(Long.class);
		final Paging paging = Mockito.mock(Paging.class);
	
		Mockito.when(paging.maxPages()).thenReturn(10);
		Mockito.when(paging.currentPage()).thenReturn(3);
		Mockito.when(paging.pageSize()).thenReturn(10);
		Mockito.when(paging.firstRow()).thenReturn(20);
		
		
		Collection<CommercialSubject> commercialSubjects = commercialSubjectRepository.forNamePattern(customer, "product%", paging);
		Mockito.verify(paging).assignRowCounter(counter.capture());
		Assert.assertEquals(paging.pageSize(), commercialSubjects.size());
		int i=20;
		for(final CommercialSubject commercialSubject : commercialSubjects){
			Assert.assertEquals("product"+i , commercialSubject.name());
			Assert.assertEquals(i+1, commercialSubject.id());
			i++;
		}
		Assert.assertEquals( 100 , (long) counter.getValue());
	}
	
	@Test
	public final void searchMatch() {
		final Customer customer = Mockito.mock(Customer.class);
		Mockito.when(customer.id()).thenReturn(19680528L);
		final Customer otherCustomer = Mockito.mock(Customer.class);
		Mockito.when(otherCustomer.id()).thenReturn(815L);
		
		
		@SuppressWarnings("unchecked")
		final Map<Long,CommercialSubject> commercialSubjects = (Map<Long, CommercialSubject>) ReflectionTestUtils.getField(commercialSubjectRepository, "commercialSubjects");
	    final CommercialSubject notInResult = Mockito.mock(CommercialSubject.class);
	    Mockito.when(notInResult.id()).thenReturn(1L);
	    Mockito.when(notInResult.hasId()).thenReturn(true);
	    Mockito.when(notInResult.name()).thenReturn("product");
	    Mockito.when(notInResult.customer()).thenReturn(customer);
	    
	    final CommercialSubject wrongCustomer = Mockito.mock(CommercialSubject.class);
	    Mockito.when(wrongCustomer.id()).thenReturn(1L);
	    Mockito.when(wrongCustomer.hasId()).thenReturn(true);
	    Mockito.when(wrongCustomer.name()).thenReturn("Special Escortsecvice");
	    Mockito.when(wrongCustomer.customer()).thenReturn(otherCustomer);
	    
	    
	    final CommercialSubject inResult = Mockito.mock(CommercialSubject.class);
	    Mockito.when(inResult.id()).thenReturn(ID);
	    Mockito.when(inResult.hasId()).thenReturn(true);
	    Mockito.when(inResult.name()).thenReturn("Special Escortsecvice");
	    Mockito.when(inResult.customer()).thenReturn(customer);
	    commercialSubjects.put(1L, notInResult);
	    commercialSubjects.put(ID, inResult);
	    commercialSubjects.put(2L, wrongCustomer);
		
		final Paging paging = Mockito.mock(Paging.class);
		
		Mockito.when(paging.maxPages()).thenReturn(Integer.MAX_VALUE);
		Mockito.when(paging.currentPage()).thenReturn(1);
		Mockito.when(paging.pageSize()).thenReturn(Integer.MAX_VALUE);
		Mockito.when(paging.firstRow()).thenReturn(0);
		
		
		final Collection<CommercialSubject> results = commercialSubjectRepository.forNamePattern(customer, "%Escort%", paging);
		Assert.assertEquals(1, results.size());
		Assert.assertEquals((long) ID, results.iterator().next().id());
		Assert.assertEquals(inResult, results.iterator().next());
		
	}
	
	@Test
	public final void delete() {
		@SuppressWarnings("unchecked")
		final Map<Long,CommercialSubject> commercialSubjects = (Map<Long, CommercialSubject>) ReflectionTestUtils.getField(commercialSubjectRepository, "commercialSubjects");
		final CommercialSubject commercialSubject = Mockito.mock(CommercialSubject.class);
	    Mockito.when(commercialSubject.id()).thenReturn(ID);
	    Mockito.when(commercialSubject.hasId()).thenReturn(true);
	    Mockito.when(commercialSubject.name()).thenReturn("Special Escortsecvice");
	    commercialSubjects.put(ID, commercialSubject);
	    Assert.assertEquals(1, commercialSubjects.size());
	    
	    commercialSubjectRepository.delete(commercialSubject);
	    
	    Assert.assertEquals(0, commercialSubjects.size());
		
	}
	
	@Test(expected=IllegalArgumentException.class)
	public final void deleteMissingId() {
		commercialSubjectRepository.delete(Mockito.mock(CommercialSubject.class));
	}
	
	
	
	private String numberString(long number) {
		if ( number >= 10L ){
			return "" + number;
		}
		return "0"+number; 
	}
	
	@Test
	public final void init() {
		@SuppressWarnings("unchecked")
		final Map<Long,CommercialSubject> commercialSubjects = (Map<Long, CommercialSubject>) ReflectionTestUtils.getField(commercialSubjectRepository, "commercialSubjects");
		Assert.assertTrue(commercialSubjects.isEmpty());
		((CommercialSubjectMemoryRepositoryMock) commercialSubjectRepository).init();
		
		Assert.assertEquals(CommercialSubjectMemoryRepositoryMock.DEFAULTS.length, commercialSubjects.size());
		final Set<String> names = new HashSet<>();
		for(final CommercialSubject commercialSubject : CommercialSubjectMemoryRepositoryMock.DEFAULTS){
			names.add(commercialSubject.name());
		}
		for(final Entry<Long, CommercialSubject> entry : commercialSubjects.entrySet()) {
			Assert.assertEquals((long) entry.getKey(), entry.getValue().id());
			Assert.assertTrue(names.contains(entry.getValue().name()));
			Assert.assertEquals(CustomerMemoryReposioryMock.DEFAULT_CUSTOMER_ID, entry.getValue().customer().id());
			Assert.assertNotSame(0l, entry.getValue().id());
			Assert.assertTrue(entry.getValue().hasId());
			
		}
		
	}
	
	@Test
	public final void forId() {
		final CommercialSubject commercialSubject = Mockito.mock(CommercialSubject.class);
		Mockito.when(commercialSubject.id()).thenReturn(ID);
		@SuppressWarnings("unchecked")
		final Map<Long,CommercialSubject> commercialSubjects = (Map<Long, CommercialSubject>) ReflectionTestUtils.getField(commercialSubjectRepository, "commercialSubjects");
		commercialSubjects.put(ID, commercialSubject);
		
		Assert.assertEquals(commercialSubject, commercialSubjectRepository.forId(ID));
		
	}

}
