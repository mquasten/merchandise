package de.mq.merchandise.subject.support;





import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import de.mq.merchandise.ResultNavigation;
import de.mq.merchandise.customer.Customer;
import de.mq.merchandise.subject.Condition;
import de.mq.merchandise.subject.Subject;
import de.mq.merchandise.support.Mapper;

public class SubjectModelControllerTest {
	
	private static final String CONDITION_QUALITY = "Quality";

	private static final String DESC = "PetsForYou";

	private static final String NAME = "PetStore";

	private static final long CONDITION_ID = 4711L;

	private static final long ID = 19680228L;

	private static final Integer ROW_COUNTER = Integer.valueOf(42);
	
	private ResultNavigation paging = Mockito.mock(ResultNavigation.class);

	private final Subject subject = Mockito.mock(Subject.class);
	
	private final SubjectService subjectService = Mockito.mock(SubjectService.class);
	
	@SuppressWarnings("unchecked")
	private final Mapper<Subject,Subject> subjectIntoSubjectMapper = Mockito.mock(Mapper.class);
	@SuppressWarnings("unchecked")
	private final Mapper<Condition,Subject> conditionIntoSubjectMapper = Mockito.mock(Mapper.class);
	
	private final SubjectModelControllerImpl subjectModelController = new SubjectModelControllerImpl(subjectService, subjectIntoSubjectMapper, conditionIntoSubjectMapper);
	
	private final SubjectModel model = Mockito.mock(SubjectModel.class);
	
	@Test
	public final void countSubjects() {
		
		Mockito.when(model.getSearchCriteria()).thenReturn(subject);
		Mockito.when(subjectService.subjects(subject)).thenReturn(ROW_COUNTER);
		Assert.assertEquals(ROW_COUNTER, subjectModelController.countSubjects(model));
	}
	@Test
	public final void subjects() {
		Mockito.when(model.getSearchCriteria()).thenReturn(subject);
		final Collection<Subject> results = new ArrayList<>();
		results.add(subject);
		Mockito.when(subjectService.subjects(subject, paging)).thenReturn(results);
		Assert.assertEquals(results, subjectModelController.subjects(model, paging));
		
	}
	
	@Test
	public final void subject() {
		Mockito.when(subjectService.subject(ID)).thenReturn(subject);
		
		Assert.assertEquals(subject, subjectModelController.subject(ID));
	}
	
	@Test
	public final void  condition() {
		Mockito.when(subject.id()).thenReturn(Optional.of(ID));
		Mockito.when(model.getSubject()).thenReturn(Optional.of(subject));
		Mockito.when(subjectService.subject(ID)).thenReturn(subject);
		final Collection<Condition> conditions = new ArrayList<>();
		final Condition condition = Mockito.mock(Condition.class);
		conditions.add(condition);
		Mockito.when(condition.id()).thenReturn(Optional.of(CONDITION_ID));
		Mockito.when(subject.conditions()).thenReturn(conditions);
		
		Assert.assertEquals(condition, subjectModelController.condition(model, CONDITION_ID));
	}
	
	@Test
	public final void save() {
		final Customer customer = Mockito.mock(Customer.class);
		Mockito.when(subject.customer()).thenReturn(customer);
		final Subject existing = Mockito.mock(Subject.class);
		Mockito.when(existing.customer()).thenReturn(customer);
		Mockito.when(subjectService.subject(ID)).thenReturn(existing);
		subjectModelController.save(ID, subject);
		
		Mockito.verify(subjectIntoSubjectMapper).mapInto(subject, existing);
		Mockito.verify(subjectService).save(existing);
	}
	
	@Test
	public final void saveNew() {
		final Customer customer = Mockito.mock(Customer.class);
		Mockito.when(subject.customer()).thenReturn(customer);
		Mockito.when(subject.name()).thenReturn(NAME);
		Mockito.when(subject.description()).thenReturn(DESC);
		
		subjectModelController.save(null, subject);
		final ArgumentCaptor<Subject> subjectCaptor = ArgumentCaptor.forClass(Subject.class);
		Mockito.verify(subjectService).save(subjectCaptor.capture());
		
		
		Assert.assertTrue(subjectCaptor.getValue() instanceof SubjectImpl);
		Assert.assertEquals(NAME, subjectCaptor.getValue().name());
		Assert.assertEquals(DESC, subjectCaptor.getValue().description());
	}
	
	@Test
	public final void  saveCondition() {
		final Condition condition = Mockito.mock(Condition.class);
		Mockito.when(subjectService.subject(ID)).thenReturn(subject);
		
		Assert.assertEquals(subject, subjectModelController.save(condition, ID));
		
		Mockito.verify(conditionIntoSubjectMapper).mapInto(condition, subject);
		Mockito.verify(subjectService).save(subject);
	}
	
	@Test
	public final void  delete() {
		
		subjectModelController.delete(subject);
		Mockito.verify(subjectService).remove(subject);
	}
	
	@Test
	public final void  deleteCondition() {
		final Condition condition = Mockito.mock(Condition.class);
		Mockito.when(condition.conditionType()).thenReturn(CONDITION_QUALITY);
		
		Mockito.when(subjectService.subject(ID)).thenReturn(subject);
		Assert.assertEquals(subject, subjectModelController.delete(condition, ID));
		Mockito.verify(subject).remove(CONDITION_QUALITY);
		Mockito.verify(subjectService).save(subject);
	}

}
