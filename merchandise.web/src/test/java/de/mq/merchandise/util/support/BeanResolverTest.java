package de.mq.merchandise.util.support;


import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.context.ApplicationContext;

import de.mq.merchandise.subject.support.SubjectModel;

public class BeanResolverTest {
	
	private final ApplicationContext applicationContext = Mockito.mock(ApplicationContext.class);
	
	private final BeanResolver beanResolver = new SimpleSpringBeanResolver(applicationContext);
	
	private final SubjectModel subjectModel = Mockito.mock(SubjectModel.class);
	
	@Test
	public final void resolve() {
		Mockito.when(applicationContext.getBean(SubjectModel.class)).thenReturn(subjectModel);
		Assert.assertEquals(subjectModel, beanResolver.resolve(SubjectModel.class));
	}
	
	@Test
	public final void resolveFromApplicationContext() {
		Mockito.when(applicationContext.getBean(SubjectModel.class)).thenReturn(subjectModel);
		final Map<Class<?>, Object> beans = new HashMap<>();
		Assert.assertEquals(subjectModel, beanResolver.resolve(beans, SubjectModel.class));
	}
	
	@Test
	public final void resolveFromMap() {
		final Map<Class<?>, Object> beans = new HashMap<>();
		beans.put(SubjectModel.class, subjectModel);
		Assert.assertEquals(subjectModel, beanResolver.resolve(beans, SubjectModel.class));
	}
	
	@Test
	public final void  resolveAll() {
		final Map<String , SubjectModel> beans = new HashMap<>();
		beans.put("beanName", subjectModel);
		Mockito.when(applicationContext.getBeansOfType(SubjectModel.class)).thenReturn(beans);
		final SubjectModel otherSubjectModel = Mockito.mock(SubjectModel.class);
		final Collection<SubjectModel> results = beanResolver.resolveAll(SubjectModel.class, otherSubjectModel);
		Assert.assertEquals(2, results.size());
		Assert.assertTrue(results.contains(subjectModel));
		Assert.assertTrue(results.contains(otherSubjectModel));
	}

}
