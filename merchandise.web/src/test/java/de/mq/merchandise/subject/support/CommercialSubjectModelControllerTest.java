package de.mq.merchandise.subject.support;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import de.mq.merchandise.ResultNavigation;
import de.mq.merchandise.customer.Customer;
import de.mq.merchandise.subject.Condition;
import de.mq.merchandise.subject.Subject;
import de.mq.merchandise.support.Mapper;

public class CommercialSubjectModelControllerTest {

	private static final String INPUT_VALUE = "inputValue";
	private static final String CONDITION_TYPE = "conditiontype";
	private static final String COMMERCIAL_SUBJECT_NAME = "PetStore";
	private static final long ID = 19680528L;
	private static final Number ROW_COUNT = 42L;
	private final CommercialSubjectService commercialSubjectService = Mockito.mock(CommercialSubjectService.class);
	private final SubjectService subjectService = Mockito.mock(SubjectService.class);

	@SuppressWarnings("unchecked")
	private final Mapper<CommercialSubject, CommercialSubject> commercialSubject2CommercialSubjectMapper = Mockito.mock(Mapper.class);
	@SuppressWarnings("unchecked")
	private final Mapper<CommercialSubjectItem, CommercialSubject> commercialSubjectItemIntoCommercialSubjectMapper = Mockito.mock(Mapper.class);
	private final CommercialSubjectModelControllerImpl controller = new CommercialSubjectModelControllerImpl(commercialSubjectService, subjectService, commercialSubject2CommercialSubjectMapper, commercialSubjectItemIntoCommercialSubjectMapper);

	private final CommercialSubjectModel commercialSubjectModel = Mockito.mock(CommercialSubjectModel.class);

	private final CommercialSubject commercialSubject = Mockito.mock(CommercialSubject.class);

	private Collection<CommercialSubject> commercialSubjects = new ArrayList<>();
	private Collection<Subject> subjects = new ArrayList<>();

	private final ResultNavigation paging = Mockito.mock(ResultNavigation.class);

	private final Subject subject = Mockito.mock(Subject.class);

	private final Customer customer = Mockito.mock(Customer.class);
	private CommercialSubjectItem commercialSubjectItem = Mockito.mock(CommercialSubjectItem.class);

	private final Condition condition = Mockito.mock(Condition.class);

	@Before
	public final void setup() {
		commercialSubjects.add(commercialSubject);
		subjects.add(subject);
		Mockito.when(commercialSubject.customer()).thenReturn(customer);
		Mockito.when(commercialSubjectModel.getSearch()).thenReturn(commercialSubject);
		Mockito.when(commercialSubject.name()).thenReturn(COMMERCIAL_SUBJECT_NAME);
		Mockito.when(commercialSubjectService.commercialSubjects(commercialSubjectModel.getSearch())).thenReturn(ROW_COUNT);
		Mockito.when(commercialSubjectService.commercialSubjects(commercialSubject, paging)).thenReturn(commercialSubjects);
		Mockito.when(commercialSubjectService.commercialSubject(ID)).thenReturn(commercialSubject);

		Mockito.when(commercialSubjectService.commercialSubject(ID)).thenReturn(commercialSubject);
		Mockito.when(customer.id()).thenReturn(Optional.of(ID));
		Mockito.when(subjectService.subjects(customer)).thenReturn(subjects);

		Mockito.when(commercialSubjectItemIntoCommercialSubjectMapper.mapInto(commercialSubjectItem, commercialSubject)).thenReturn(commercialSubject);

		Mockito.when(commercialSubject.id()).thenReturn(Optional.of(ID));
		Mockito.when(commercialSubjectItem.id()).thenReturn(Optional.of(ID));
		Mockito.when(subject.id()).thenReturn(Optional.of(ID));

		Mockito.when(commercialSubjectItem.subject()).thenReturn(subject);

		Mockito.when(subjectService.subject(ID)).thenReturn(subject);
		Mockito.when(commercialSubject.commercialSubjectItems()).thenReturn(Arrays.asList(commercialSubjectItem));
		Mockito.when(commercialSubjectModel.getCommercialSubject()).thenReturn(Optional.of(commercialSubject));
		Mockito.when(commercialSubjectModel.getCommercialSubjectItem()).thenReturn(Optional.of(commercialSubjectItem));

		Mockito.when(commercialSubject.commercialSubjectItem(subject)).thenReturn(Optional.of(commercialSubjectItem));

		Mockito.when(commercialSubjectModel.getConditions()).thenReturn(Arrays.asList(condition));
		Mockito.when(condition.id()).thenReturn(Optional.of(ID));
		Mockito.when(condition.conditionType()).thenReturn(CONDITION_TYPE);
		Mockito.when(commercialSubjectModel.getInputValue()).thenReturn(INPUT_VALUE);
	}

	@Test
	public final void countCommercialSubjects() {
		Assert.assertEquals(ROW_COUNT, controller.countCommercialSubjects(commercialSubjectModel));
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
	public final void saveNew() {
		controller.save(null, commercialSubject);
		final ArgumentCaptor<CommercialSubject> commercialSubjectCaptor = ArgumentCaptor.forClass(CommercialSubject.class);

		Mockito.verify(commercialSubjectService).save(commercialSubjectCaptor.capture());

		Assert.assertEquals(COMMERCIAL_SUBJECT_NAME, commercialSubjectCaptor.getValue().name());
		Assert.assertEquals(customer, commercialSubjectCaptor.getValue().customer());
	}

	@Test
	public final void delete() {
		controller.delete(commercialSubject);
		Mockito.verify(commercialSubjectService).remove(commercialSubject);
	}

	@Test
	public final void subjects() {
		Assert.assertEquals(subjects, controller.subjects(customer));
	}

	@Test
	public final void saveItem() {
		Assert.assertEquals(commercialSubject, controller.saveItem(commercialSubjectItem, ID));

		Mockito.verify(commercialSubjectItemIntoCommercialSubjectMapper).mapInto(commercialSubjectItem, commercialSubject);
		Mockito.verify(commercialSubjectService).save(commercialSubject);
	}

	@Test
	public final void commericalSubjectItem() {

		Assert.assertEquals(commercialSubjectItem, controller.commericalSubjectItem(commercialSubjectModel, ID));
	}

	@Test
	public final void deleteItem() {

		Assert.assertEquals(commercialSubject, controller.delete(commercialSubjectItem, ID));

		Mockito.verify(commercialSubject).remove(subject);
		Mockito.verify(commercialSubjectService).save(commercialSubject);
	}

	@Test
	public final void conditionChanged() {
		CommercialSubjectItemConditionImpl result = controller.conditionChanged(null, ID);
		Assert.assertTrue(result.condition().id().isPresent());
		Assert.assertEquals(ID, (long) result.condition().id().get());

	}

	@Test
	public final void addInputValue() {
		Mockito.when(condition.conditionDataType()).thenReturn(ConditionDataType.String);
		
		Mockito.when(commercialSubjectModel.getCondition(ID)).thenReturn(condition);
		Mockito.when(commercialSubjectModel.convertConditionValue(INPUT_VALUE, ID)).thenReturn(INPUT_VALUE);
		
		
		Assert.assertEquals(commercialSubjectItem, controller.addInputValue(commercialSubjectModel, ID));
		Mockito.verify(commercialSubjectItem).assign(CONDITION_TYPE, INPUT_VALUE);
		Mockito.verify(commercialSubjectService).save(commercialSubject);
	}

	@Test
	public final void removeInputValue() {
		Mockito.when(condition.conditionDataType()).thenReturn(ConditionDataType.String);
		Mockito.when(commercialSubjectModel.getCondition(ID)).thenReturn(condition);
		Mockito.when(commercialSubjectModel.convertConditionValue(INPUT_VALUE, ID)).thenReturn(INPUT_VALUE);
		
		
		Assert.assertEquals(commercialSubjectItem, controller.deleteInputValue(commercialSubjectModel, ID, INPUT_VALUE));

		Mockito.verify(commercialSubjectItem).remove(CONDITION_TYPE, INPUT_VALUE);
		Mockito.verify(commercialSubjectService).save(commercialSubject);
	}

}
