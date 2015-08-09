package de.mq.merchandise.subject.support;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
















import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;


import de.mq.merchandise.ResultNavigation;
import de.mq.merchandise.support.Mapper;

public class CommercialSubjectServiceTest {
	
	private static final long ID = 19680528L;
	private static final int COUNTER = 42;
	private static final String NAME = "Name";
	private static final String NAME_FIELD = "name";
	private final  CommercialSubjectRepository commercialSubjectRepository=Mockito.mock(CommercialSubjectRepository.class); 
	@SuppressWarnings("unchecked")
	private final Mapper<CommercialSubject, Map<String, Object>> mapper = Mockito.mock(Mapper.class);
	private CommercialSubjectService commercialSubjectService = new CommercialSubjectServiceImpl(commercialSubjectRepository, mapper);
	
	private CommercialSubject commercialSubject = Mockito.mock(CommercialSubject.class);
	
	private ResultNavigation resultNavigation = Mockito.mock(ResultNavigation.class);
	
	
	
	@SuppressWarnings("unchecked")
	@Test
	public final void commercialSubjects() {

		final List<CommercialSubject> results = new ArrayList<>();
		results.add(commercialSubject);
		final Map<String,Object> queryMap =  new HashMap<>();
		queryMap.put(NAME_FIELD, NAME);
		Mockito.when(mapper.mapInto(Mockito.any(CommercialSubject.class),  Mockito.any(Map.class))).thenReturn(queryMap);
		Mockito.when(commercialSubjectRepository.commercialSubjectsForCustomer(queryMap, resultNavigation)).thenReturn(results);
		
		final Collection<CommercialSubject> commercialSubjects = commercialSubjectService.commercialSubjects(commercialSubject, resultNavigation);
		Assert.assertEquals(1, commercialSubjects.size());
		Assert.assertTrue(commercialSubjects.stream().findAny().isPresent());
		Assert.assertEquals(commercialSubject, commercialSubjects.stream().findAny().get());
	}
	
	
	@SuppressWarnings("unchecked")
	@Test
	public final void commercialSubjectsConter() {
		final Map<String,Object> queryMap =  new HashMap<>();
		queryMap.put(NAME_FIELD, NAME);
		Mockito.when(mapper.mapInto(Mockito.any(CommercialSubject.class),  Mockito.any(Map.class))).thenReturn(queryMap);
		Mockito.when(commercialSubjectRepository.countCommercialSubjectsForCustomer(queryMap)).thenReturn(COUNTER);
		
		Assert.assertEquals(COUNTER, commercialSubjectService.commercialSubjects(commercialSubject));
	}
	
	@Test
	public final void save() {
		commercialSubjectService.save(commercialSubject);
		Mockito.verify(commercialSubjectRepository).save(commercialSubject);
	}
	
	
	@Test
	public final void commercialSubject() {
		Mockito.when(commercialSubjectRepository.commercialSubject(ID)).thenReturn(commercialSubject);
		Assert.assertEquals(commercialSubject, commercialSubjectService.commercialSubject(ID));
	
	}
	
	@Test
	public final void remove() {
		commercialSubjectService.remove(commercialSubject);
		Mockito.verify(commercialSubjectRepository).remove(commercialSubject);
	}

}
