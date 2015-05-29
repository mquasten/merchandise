package de.mq.merchandise.util.support;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;

import javax.persistence.Transient;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.beans.BeanInstantiationException;
import org.springframework.beans.BeanUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.test.util.ReflectionTestUtils;

import de.mq.merchandise.ResultNavigation;
import de.mq.merchandise.subject.Subject;
import de.mq.merchandise.subject.support.SubjectEventQualifier;
import de.mq.merchandise.subject.support.SubjectModel;
import de.mq.merchandise.subject.support.SubjectModel.EventType;
import de.mq.merchandise.subject.support.SubjectService;
import de.mq.merchandise.subject.support.TestConstants;
import de.mq.merchandise.util.Event;

public class EventListenerTest {
	
	private static final String QUALIFIERS_FIELD = "qualifiers";
	private static final String SAVE_METHOD = "save";
	private static final String APPLICATION_CONTEXT_FIELD = "applicationContext";
	private static final Long ID = 19680528L;
	private static final String SUBJECTS_LIST_METHOD = "subjects";
	private static final String COUNT_SUBJECTS_METHOD = "countSubjects";
	private static final String CHANGE_SUBJECT_METHOD = "subject";
	private static final String TARGETS_FIELDS_NAME = "targets";
	private static final String METHODS_FIELD_NAME = "methods";
	private final SubjectModel.EventType[] types  = new SubjectModel.EventType[]{SubjectModel.EventType.SubjectChanged, SubjectModel.EventType.CountPaging, SubjectModel.EventType.ListPaging };
	private final ApplicationContext applicationContext = Mockito.mock(ApplicationContext.class);
	private final SimpleEventListenerImpl  eventListener = new SimpleEventListenerImpl(); 
	private final SubjectService subjectService = Mockito.mock(SubjectService.class);
	private final Subject subject = Mockito.mock(Subject.class);
	@SuppressWarnings("unchecked")
	final Event<EventType, Subject> event = Mockito.mock(Event.class);
	
	@Test
	public final void setApplicationContext() throws BeanInstantiationException, NoSuchMethodException, SecurityException {
		final Map<String,Object> beans = new HashMap<>();
		final Object controller = BeanUtils.instantiateClass(TestConstants.SUBJECT_CONTROLLER_CLASS.getDeclaredConstructor(SubjectService.class), subjectService);
		beans.put(controller.getClass().getSimpleName(), controller);
		
		Mockito.when(applicationContext.getBeansWithAnnotation(Controller.class)).thenReturn(beans);
		eventListener.setApplicationContext(applicationContext);
		
		
	
		final Map<Object,Method> methods = methods();
		
		final Map<Object,Object> targets = targets();
		
		final Method[] results = new Method[] {controller.getClass().getDeclaredMethod(CHANGE_SUBJECT_METHOD, Long.class),controller.getClass().getDeclaredMethod(COUNT_SUBJECTS_METHOD, SubjectModel.class), controller.getClass().getDeclaredMethod(SUBJECTS_LIST_METHOD, SubjectModel.class, ResultNavigation.class)};
		
		Arrays.asList(types).stream().forEach(type -> Assert.assertTrue(methods.containsKey(type)));
		Arrays.asList(types).stream().forEach(type -> Assert.assertEquals(controller, targets.get(type)));
		IntStream.range(0, 3).forEach(i-> Assert.assertEquals(results[i], methods.get(types[i])));
		
	}

	@SuppressWarnings("unchecked")
	private Map<Object, Object> targets() {
		return (Map<Object, Object>) ReflectionTestUtils.getField(eventListener, TARGETS_FIELDS_NAME);
	}

	@SuppressWarnings("unchecked")
	private Map<Object, Method> methods() {
		return (Map<Object, Method>) ReflectionTestUtils.getField(eventListener, METHODS_FIELD_NAME);
	}
	
	@Test
	public final void processEvent() throws BeanInstantiationException, NoSuchMethodException, SecurityException {
		final Object controller = BeanUtils.instantiateClass(TestConstants.SUBJECT_CONTROLLER_CLASS.getDeclaredConstructor(SubjectService.class), subjectService);
		final Method method = controller.getClass().getDeclaredMethod(CHANGE_SUBJECT_METHOD, Long.class);
		
		methods().put(EventType.SubjectChanged, method);
		targets().put(EventType.SubjectChanged,controller);
		
	
		Mockito.when(event.id()).thenReturn(EventType.SubjectChanged);
		final Map<Class<?>, Object> params = new HashMap<>();
		params.put(Long.class, ID);
		Mockito.when(event.parameter()).thenReturn(params);
		
		Mockito.when(subjectService.subject(ID)).thenReturn(subject);
		
		eventListener.processEvent(event);
		
		Mockito.verify(subjectService, Mockito.times(1)).subject(ID);
		Mockito.verify(event,  Mockito.times(1)).assign(subject);
		
	}
	
	@Test
	public final void processEventVoidMethod() throws BeanInstantiationException, NoSuchMethodException, SecurityException {
		final TestControler controller = new TestControler();
		final Method method = controller.getClass().getDeclaredMethod(SAVE_METHOD, Subject.class);
		methods().put(EventType.SubjectChanged, method);
		targets().put(EventType.SubjectChanged,controller);
		
		Mockito.when(event.id()).thenReturn(EventType.SubjectChanged);
		final ApplicationContext applicationContext = Mockito.mock(ApplicationContext.class);
		Mockito.when(applicationContext.getBean(Subject.class)).thenReturn(subject);
		ReflectionTestUtils.setField(eventListener, APPLICATION_CONTEXT_FIELD, applicationContext);
		eventListener.processEvent(event);
		
		Mockito.verify(event,  Mockito.times(0)).assign(Mockito.any());
		Mockito.verify(subject, Mockito.times(1)).id();
	}
	
	@Test
	public final void setApplicationContextAnnotationNotRegisterd() {
		final Map<String,Object> beans = new HashMap<>();
		final Object controller = new TestControler();
		beans.put(controller.getClass().getSimpleName(), controller);
		
		Mockito.when(applicationContext.getBeansWithAnnotation(Controller.class)).thenReturn(beans);
		
		((Collection<?>)ReflectionTestUtils.getField(eventListener, QUALIFIERS_FIELD)).clear();
		
		eventListener.setApplicationContext(applicationContext);
		Assert.assertEquals(0, targets().size());
		Assert.assertEquals(0, methods().size());
	}
	
	
	@Test
	public final void setApplicationContextWrongAnnotation() {
		final Map<String,Object> beans = new HashMap<>();
		final Object controller = new TestControler();
		beans.put(controller.getClass().getSimpleName(), controller);
		
		Mockito.when(applicationContext.getBeansWithAnnotation(Controller.class)).thenReturn(beans);
		
		
		@SuppressWarnings("unchecked")
		Collection<Class<? extends Annotation>> qualifiers = (Collection<Class<? extends Annotation>>) ReflectionTestUtils.getField(eventListener, QUALIFIERS_FIELD);
		qualifiers.clear();
		qualifiers.add(Transient.class);
		
		eventListener.setApplicationContext(applicationContext);
		Assert.assertEquals(0, targets().size());
		Assert.assertEquals(0, methods().size());
	}

}



class TestControler {
	
		@SubjectEventQualifier(EventType.SubjectChanged)
		void save(final Subject subject) {
			subject.id();
		}
		
		@Transient()
		void test(final Subject subject) {
			subject.id();
		}
		
	
}
