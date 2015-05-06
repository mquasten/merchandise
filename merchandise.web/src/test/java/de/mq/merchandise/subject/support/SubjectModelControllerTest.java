package de.mq.merchandise.subject.support;





import java.util.ArrayList;
import java.util.Collection;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import de.mq.merchandise.ResultNavigation;
import de.mq.merchandise.subject.Subject;

public class SubjectModelControllerTest {
	
	private static final Integer ROW_COUNTER = Integer.valueOf(42);
	
	private ResultNavigation paging = Mockito.mock(ResultNavigation.class);

	private final Subject subject = Mockito.mock(Subject.class);
	
	private final SubjectService subjectService = Mockito.mock(SubjectService.class);
	
	private final SubjectModelControllerImpl subjectModelController = new SubjectModelControllerImpl(subjectService);
	
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

}
