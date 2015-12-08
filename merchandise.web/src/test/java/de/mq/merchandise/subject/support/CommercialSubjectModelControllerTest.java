package de.mq.merchandise.subject.support;



import java.util.ArrayList;
import java.util.Collection;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;















import de.mq.merchandise.ResultNavigation;
import de.mq.merchandise.customer.Customer;
import de.mq.merchandise.support.Mapper;

public class CommercialSubjectModelControllerTest {
	
	
	private static final String COMMERCIAL_SUBJECT_NAME = "PetStore";
	private static final long ID = 19680528L;
	private static final Number ROW_COUNT = 42L;
	private final CommercialSubjectService commercialSubjectService = Mockito.mock(CommercialSubjectService.class);
	private final  SubjectService subjectService = Mockito.mock(SubjectService.class);
	
	@SuppressWarnings("unchecked")
	private final Mapper<CommercialSubject,CommercialSubject>  commercialSubject2CommercialSubjectMapper = Mockito.mock(Mapper.class);
	@SuppressWarnings("unchecked")
	private final Mapper<CommercialSubjectItem,CommercialSubject> commercialSubjectItemIntoCommercialSubjectMapper = Mockito.mock(Mapper.class);
	private final CommercialSubjectModelControllerImpl controller = new CommercialSubjectModelControllerImpl(commercialSubjectService,subjectService, commercialSubject2CommercialSubjectMapper, commercialSubjectItemIntoCommercialSubjectMapper);
	
	private final CommercialSubjectModel commercialSubjectModel = Mockito.mock(CommercialSubjectModel.class);
	
	private final CommercialSubject commercialSubject = Mockito.mock(CommercialSubject.class);
	
	private Collection<CommercialSubject> commercialSubjects = new ArrayList<>();
	
	private final ResultNavigation paging = Mockito.mock(ResultNavigation.class);
	
	private final Customer customer = Mockito.mock(Customer.class);
	@Before
	public final void setup() {
		commercialSubjects.add(commercialSubject);
		Mockito.when(commercialSubject.customer()).thenReturn(customer);
		Mockito.when(commercialSubjectModel.getSearch()).thenReturn(commercialSubject);
		Mockito.when(commercialSubject.name()).thenReturn(COMMERCIAL_SUBJECT_NAME);
		Mockito.when(commercialSubjectService.commercialSubjects(commercialSubjectModel.getSearch())).thenReturn(ROW_COUNT);
		Mockito.when( commercialSubjectService.commercialSubjects(commercialSubject,paging)).thenReturn(commercialSubjects);
		Mockito.when( commercialSubjectService.commercialSubject(ID)).thenReturn(commercialSubject);
		
		Mockito.when(commercialSubjectService.commercialSubject(ID)).thenReturn(commercialSubject);
	}
	
	@Test
	public final void countCommercialSubjects() {
		Assert.assertEquals(ROW_COUNT,  controller.countCommercialSubjects(commercialSubjectModel));
	}
	
	
	@Test
	public final void commercialSubjects() {
		Assert.assertEquals(commercialSubjects, controller.commercialSubjects(commercialSubjectModel, paging));
	}
	
	@Test
	public final void subject() {
		Assert.assertEquals(commercialSubject, controller.subject(ID));
	}
	@Test
	public final void save() {
		controller.save(ID, commercialSubject);
		
		Mockito.verify(commercialSubject2CommercialSubjectMapper).mapInto(commercialSubject, commercialSubject);

		Mockito.verify(commercialSubjectService).save(commercialSubject);
	}
	
	@Test
	public final void saveNew(){
		controller.save(null, commercialSubject);
		final ArgumentCaptor<CommercialSubject> commercialSubjectCaptor = ArgumentCaptor.forClass(CommercialSubject.class);
		
		Mockito.verify(commercialSubjectService).save(commercialSubjectCaptor.capture());
		
		Assert.assertEquals(COMMERCIAL_SUBJECT_NAME, commercialSubjectCaptor.getValue().name());
		Assert.assertEquals(customer, commercialSubjectCaptor.getValue().customer());
	}
	
	@Test
	public final void  delete() {
		controller.delete(commercialSubject);
		Mockito.verify(commercialSubjectService).remove(commercialSubject);
	}

}
