package de.mq.merchandise.util.support;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.context.ApplicationContext;
import org.springframework.web.util.UriUtils;

import de.mq.merchandise.subject.Subject;
import de.mq.merchandise.util.support.BeanContainerOperations.BeanFilter;

public class BeanContainerOperationsTest {

	private final ApplicationContext applicationContext = Mockito.mock(ApplicationContext.class);

	private final BeanContainerOperations beanContainerOperations = new BeanContainerTemplate(applicationContext);

	private final BeanFilter<?> beanFilter = Mockito.mock(BeanFilter.class);

	final Subject subject = Mockito.mock(Subject.class);

	@Test
	public void requiredSingelBean() {

		Mockito.when(beanContainerOperations.requiredSingelBean(Subject.class)).thenReturn(subject);

		Assert.assertEquals(subject, beanContainerOperations.requiredSingelBean((Subject.class)));
	}

	@Test
	public void beansForFilter() {
		final Map<String, Subject> beans = new HashMap<>();
		beans.put(subject.getClass().getSimpleName(), subject);
		Mockito.when(applicationContext.getBeansOfType(Subject.class)).thenReturn(beans);
		Mockito.doAnswer(i -> {
			return ((ApplicationContext) i.getArguments()[0]).getBeansOfType(Subject.class).values();
		}).when(beanFilter).filter(Mockito.any(ApplicationContext.class));

		@SuppressWarnings("unchecked")
		final Collection<Subject> results = (Collection<Subject>) beanContainerOperations.beansForFilter(beanFilter);
		Assert.assertEquals(1, results.size());
		Assert.assertEquals(subject, results.stream().findFirst().get());
		
	}

	@Test
	public void resolveMandatoryBeansFromDefaultOrContainer() {
		final Subject defaultSubject = Mockito.mock(Subject.class);
		final Map<Class<?>, Object> beans = new HashMap<>();
		beans.put(Subject.class, defaultSubject);
		Mockito.when(applicationContext.getBean(Subject.class)).thenReturn(subject);
		final Object[] results = beanContainerOperations.resolveMandatoryBeansFromDefaultOrContainer(beans, new Class[] { Subject.class });
		Assert.assertEquals(1, results.length);
		Assert.assertEquals(defaultSubject, results[0]);
	}

	@Test
	public void resolveMandatoryBeansFromDefaultOrContainerNoDefault() {

		Mockito.when(applicationContext.getBean(Subject.class)).thenReturn(subject);
		final Object[] results = beanContainerOperations.resolveMandatoryBeansFromDefaultOrContainer(new HashMap<>(), new Class[] { Subject.class });
		Assert.assertEquals(1, results.length);
		Assert.assertEquals(subject, results[0]);
	}

}
