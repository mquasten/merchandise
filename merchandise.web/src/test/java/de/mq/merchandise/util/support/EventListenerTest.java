package de.mq.merchandise.util.support;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.beans.BeanInstantiationException;
import org.springframework.beans.BeanUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.test.util.ReflectionTestUtils;

import de.mq.merchandise.ResultNavigation;
import de.mq.merchandise.subject.support.SubjectModel;
import de.mq.merchandise.subject.support.SubjectService;
import de.mq.merchandise.subject.support.TestConstants;

public class EventListenerTest {
	
	private static final String SUBJECTS_LIST_METHOD = "subjects";
	private static final String COUNT_SUBJECTS_METHOD = "countSubjects";
	private static final String CHANGE_SUBJECT_METHOD = "subject";
	private static final String TARGETS_FIELDS_NAME = "targets";
	private static final String METHODS_FIELD_NAME = "methods";
	private final SubjectModel.EventType[] types  = new SubjectModel.EventType[]{SubjectModel.EventType.SubjectChanged, SubjectModel.EventType.CountPaging, SubjectModel.EventType.ListPaging };
	private final ApplicationContext applicationContext = Mockito.mock(ApplicationContext.class);
	private final SimpleEventListenerImpl  eventListener = new SimpleEventListenerImpl(); 
	private final SubjectService subjectService = Mockito.mock(SubjectService.class);
	
	
	@Test
	public final void setApplicationContext() throws BeanInstantiationException, NoSuchMethodException, SecurityException {
		final Map<String,Object> beans = new HashMap<>();
		final Object controller = BeanUtils.instantiateClass(TestConstants.SUBJECT_CONTROLLER_CLASS.getDeclaredConstructor(SubjectService.class), subjectService);
		beans.put(controller.getClass().getSimpleName(), controller);
		
		Mockito.when(applicationContext.getBeansWithAnnotation(Controller.class)).thenReturn(beans);
		eventListener.setApplicationContext(applicationContext);
		
		
		@SuppressWarnings("unchecked")
		final Map<Object,Method> methods = (Map<Object, Method>) ReflectionTestUtils.getField(eventListener, METHODS_FIELD_NAME);
		@SuppressWarnings("unchecked")
		final Map<Object,Method> targets = (Map<Object, Method>) ReflectionTestUtils.getField(eventListener, TARGETS_FIELDS_NAME);
		
		final Method[] results = new Method[] {controller.getClass().getDeclaredMethod(CHANGE_SUBJECT_METHOD, Long.class),controller.getClass().getDeclaredMethod(COUNT_SUBJECTS_METHOD, SubjectModel.class), controller.getClass().getDeclaredMethod(SUBJECTS_LIST_METHOD, SubjectModel.class, ResultNavigation.class)};
		
		Arrays.asList(types).stream().forEach(type -> Assert.assertTrue(methods.containsKey(type)));
		Arrays.asList(types).stream().forEach(type -> Assert.assertEquals(controller, targets.get(type)));
		IntStream.range(0, 3).forEach(i-> Assert.assertEquals(results[i], methods.get(types[i])));
		
	}

}
