package de.mq.merchandise.util.support;

import java.lang.reflect.Method;

import java.util.Map;

import net.sf.cglib.proxy.MethodProxy;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.util.ReflectionUtils;

import de.mq.merchandise.subject.Subject;
import de.mq.merchandise.util.Event;
import de.mq.merchandise.util.EventFascadeProxyFactory;

public class CGLibEventFascadeProxyFactoryTest {

	static final String CHANGE_SUBJECT = "changeSubject";
	private static final long ID = 4711L;
	final ApplicationEventPublisher applicationEventPublisher = Mockito.mock(ApplicationEventPublisher.class);
	final EventAnnotationOperations eventAnnotationOperations = Mockito.mock(EventAnnotationOperations.class);
	final Subject subject = Mockito.mock(Subject.class);

	final EventFascadeProxyFactory eventFascadeProxyFactory = new CGLibEventFascadeProxyFactoryImpl(applicationEventPublisher, eventAnnotationOperations);

	@SuppressWarnings("unchecked")
	@Test
	public final void createProxy() {

		final Method method = ReflectionUtils.findMethod(TestFascade.class, CHANGE_SUBJECT, Long.class);
	
		final Qualifier annotation = method.getAnnotation(Qualifier.class);

		Mockito.when(eventAnnotationOperations.valueFromAnnotation(method)).thenReturn(annotation.value());

		Mockito.when(eventAnnotationOperations.isAnnotaionPresent(method)).thenReturn(true);

		Mockito.doAnswer(i -> {

			final Event<String, Subject> event = (Event<String, Subject>) i.getArguments()[0];
			Assert.assertEquals(CHANGE_SUBJECT, event.id());
			final Map<Class<?>, Object> params = event.parameter();
			Assert.assertEquals(1, params.size());
			Assert.assertEquals(Long.class, params.keySet().iterator().next());
			Assert.assertEquals(4711L, params.values().iterator().next());
			event.assign(subject);
			return null;
		}).when(applicationEventPublisher).publishEvent(Mockito.any(Event.class));

		TestFascade proxy = eventFascadeProxyFactory.createProxy(TestFascade.class);

		Assert.assertEquals(subject, proxy.changeSubject(ID));

		Assert.assertTrue(proxy == eventFascadeProxyFactory.createProxy(TestFascade.class));

		Assert.assertTrue(proxy.toString().startsWith(MethodProxy.class.getName()));

	}

}

abstract class TestFascade {
	@Qualifier(CGLibEventFascadeProxyFactoryTest.CHANGE_SUBJECT)
	public abstract Subject changeSubject(Long id);
}