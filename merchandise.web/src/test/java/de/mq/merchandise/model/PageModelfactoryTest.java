package de.mq.merchandise.model;

import java.util.Collection;
import java.util.Map;

import junit.framework.Assert;

import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;

import de.mq.mapping.util.proxy.AOProxyFactory;
import de.mq.mapping.util.proxy.BeanResolver;
import de.mq.mapping.util.proxy.ModelRepository;


public class PageModelfactoryTest {
	
	
	private final AOProxyFactory proxyFactory = Mockito.mock(AOProxyFactory.class);
	
	private final BeanResolver beanResolver = Mockito.mock(BeanResolver.class);
	
	private final PageModelFactoryImpl  pageModelFactoryImpl  = new PageModelFactoryImpl(proxyFactory, beanResolver);
	private final  PageModelAO pageModelAO = pageModelFactoryImpl.pageModel();
	
	@SuppressWarnings("unchecked")
	@Test
	public final void createProxy() {
		@SuppressWarnings("rawtypes")
		final ArgumentCaptor<Class> classArgumentCaptor =  ArgumentCaptor.forClass(Class.class);
		final ArgumentCaptor<ModelRepository> modelRepositoryArgumentCaptor = ArgumentCaptor.forClass(ModelRepository.class);
		Mockito.when(proxyFactory.createProxy(classArgumentCaptor.capture(), modelRepositoryArgumentCaptor.capture())).thenReturn(pageModelAO);
		
		Assert.assertEquals(pageModelAO, pageModelFactoryImpl.pageModel());
		
		Assert.assertEquals(PageModelAO.class, classArgumentCaptor.getValue());
		ModelRepository modelRepository = modelRepositoryArgumentCaptor.getValue(); 
		
		
		final Collection<Object> items = (Collection<Object>) ((Map<?, ?>) ReflectionTestUtils.getField(modelRepository, "modelItems")).values();
		Assert.assertEquals(1, items.size());
		Assert.assertFalse((Boolean) items.iterator().next());
		
	}
	
	@Test
	public final void defaultKonstrktor() {
		final PageModelFactoryImpl  pageModelFactoryImpl  = new PageModelFactoryImpl();
		Assert.assertNull(ReflectionTestUtils.getField(pageModelFactoryImpl, "beanResolver"));
		Assert.assertNull(ReflectionTestUtils.getField(pageModelFactoryImpl, "proxyFactory"));
	}
	

}
