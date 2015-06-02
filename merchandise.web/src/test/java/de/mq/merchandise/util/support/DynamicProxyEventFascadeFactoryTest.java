package de.mq.merchandise.util.support;

import java.lang.reflect.Method;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.util.ReflectionUtils;


import de.mq.merchandise.subject.Subject;
import de.mq.merchandise.util.Event;
import de.mq.merchandise.util.EventFascadeProxyFactory;

public class DynamicProxyEventFascadeFactoryTest {
	private static final long ID = 4711L;
	static final String CHANGE_SUBJECT = "changeSubject";
	final ApplicationEventPublisher applicationEventPublisher = Mockito.mock(ApplicationEventPublisher.class);
	final EventAnnotationOperations eventAnnotationOperations = Mockito.mock(EventAnnotationOperations.class);
	final Subject subject = Mockito.mock(Subject.class);
	final EventFascadeProxyFactory eventFascadeProxyFactory = new DynamicProxyEventFascadeFactoryImpl(applicationEventPublisher, eventAnnotationOperations);
	
	@Before
	public void setup() {
		final Method method = ReflectionUtils.findMethod(Fascade.class, CHANGE_SUBJECT, Long.class);
	
		final Qualifier annotation = method.getAnnotation(Qualifier.class);

		Mockito.when(eventAnnotationOperations.valueFromAnnotation(method)).thenReturn(annotation.value());

		Mockito.when(eventAnnotationOperations.isAnnotaionPresent(method)).thenReturn(true);
	}
	
	@Test
	public final void createProxy() {
		Mockito.doAnswer(i -> {

			@SuppressWarnings("unchecked")
			final Event<String, Subject> event = (Event<String, Subject>) i.getArguments()[0];
			Assert.assertEquals(CHANGE_SUBJECT, event.id());
			final Map<Class<?>, Object> params = event.parameter();
			Assert.assertEquals(1, params.size());
			Assert.assertEquals(Long.class, params.keySet().iterator().next());
			Assert.assertEquals(ID, params.values().iterator().next());
			event.assign(subject);
			return null;
		}).when(applicationEventPublisher).publishEvent(Mockito.any(Event.class));
		
		final Fascade proxy = eventFascadeProxyFactory.createProxy(Fascade.class);
		Assert.assertEquals(subject, proxy.changeSubject(4711L));
		
		Assert.assertTrue(proxy.toString().startsWith(DynamicProxyEventFascadeFactoryImpl.class.getName()));
		
	}

}

interface Fascade {
	@Qualifier(CGLibEventFascadeProxyFactoryTest.CHANGE_SUBJECT)
	Subject changeSubject(Long id);
}
