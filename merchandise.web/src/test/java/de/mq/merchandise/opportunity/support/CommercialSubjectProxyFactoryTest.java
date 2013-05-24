package de.mq.merchandise.opportunity.support;

import java.util.Map;

import junit.framework.Assert;


import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.test.util.ReflectionTestUtils;

import de.mq.mapping.util.proxy.AOProxyFactory;
import de.mq.mapping.util.proxy.BeanResolver;
import de.mq.mapping.util.proxy.ModelRepository;
import de.mq.merchandise.util.SimplePagingImpl;

public class CommercialSubjectProxyFactoryTest {

	private final BeanResolver beanResolver = Mockito.mock(BeanResolver.class);

	private final AOProxyFactory aoProxyFactory = Mockito.mock(AOProxyFactory.class);

	private final CommercialSubjectProxyFactoryImpl commercialSubjectProxyFactory = new CommercialSubjectProxyFactoryImpl(aoProxyFactory, beanResolver);

	@SuppressWarnings("unchecked")
	@Test
	public final void createSubjectsModel() {
		final PagingAO pagingAO = Mockito.mock(PagingAO.class);
		final CommercialSubjectsModelAO commercialSubjectsModelAO = Mockito.mock(CommercialSubjectsModelAO.class);
		final boolean[] likeAVirgin = { true };

		Mockito.when(aoProxyFactory.createProxy(Mockito.any(Class.class), Mockito.any(ModelRepository.class))).thenAnswer(new Answer<Object>() {

			@SuppressWarnings("rawtypes")
			@Override
			public Object answer(InvocationOnMock invocation) throws Throwable {
				Assert.assertEquals(beanResolver, ((ModelRepository) invocation.getArguments()[1]).beanResolver());
				if (likeAVirgin[0]) {
					likeAVirgin[0] = false;
					Assert.assertEquals(PagingAO.class, invocation.getArguments()[0]);
					Assert.assertEquals(SimplePagingImpl.class, ((Map) ReflectionTestUtils.getField(invocation.getArguments()[1], "modelItems")).values().iterator().next().getClass());
					Mockito.when(commercialSubjectsModelAO.getPaging()).thenReturn(pagingAO);
					return pagingAO;
				}
				Assert.assertEquals(CommercialSubjectsModelAO.class, invocation.getArguments()[0]);
				Assert.assertEquals(pagingAO, (((Map) ReflectionTestUtils.getField(invocation.getArguments()[1], "modelItems")).values().iterator().next()));
				return commercialSubjectsModelAO;
			}
		});

		final CommercialSubjectsModelAO result = commercialSubjectProxyFactory.commercialSubjectsModel();
		Assert.assertEquals(commercialSubjectsModelAO, result);
		Assert.assertEquals(pagingAO, result.getPaging());

	}
	@Test
	public final void defaultConstructor() {
		Assert.assertNotNull(new CommercialSubjectProxyFactoryImpl());
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Test
	public final void commercialSubject() {
		final CommercialSubjectAO commercialSubjectAO = Mockito.mock(CommercialSubjectAO.class);
		final ArgumentCaptor<Class> classArgumentCaptor = ArgumentCaptor.forClass(Class.class);
		final ArgumentCaptor<ModelRepository> modelRepositoryCaptor= ArgumentCaptor.forClass(ModelRepository.class);
		Mockito.when(aoProxyFactory.createProxy(classArgumentCaptor.capture(), modelRepositoryCaptor.capture())).thenReturn(commercialSubjectAO);
		
		Assert.assertEquals(commercialSubjectAO, commercialSubjectProxyFactory.commercialSubject());
		Assert.assertEquals(CommercialSubjectAO.class, classArgumentCaptor.getValue());
		Assert.assertTrue(((Map<?,CommercialSubject>)ReflectionTestUtils.getField(modelRepositoryCaptor.getValue(), "modelItems")).values().iterator().next() instanceof CommercialSubject) ;
		Assert.assertEquals(beanResolver, modelRepositoryCaptor.getValue().beanResolver());
		
	}

}
