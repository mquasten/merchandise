package de.mq.merchandise.opportunity.support;

import java.util.Map;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;

import de.mq.mapping.util.proxy.AOProxyFactory;
import de.mq.mapping.util.proxy.BeanResolver;
import de.mq.mapping.util.proxy.ModelRepository;

public class DocumentModelProxyFactoryTest {
	
	private DocumentModelProxyFactoryImpl documentModelProxyFactory = new DocumentModelProxyFactoryImpl();
	
	private final AOProxyFactory proxyFactory = Mockito.mock(AOProxyFactory.class);
	private final BeanResolver beanResolver = Mockito.mock(BeanResolver.class);
	
	private DocumentModelAO documentModelAO = Mockito.mock(DocumentModelAO.class);
	
	
	@SuppressWarnings("rawtypes")
	private ArgumentCaptor<Class> classArgumentCaptor = ArgumentCaptor.forClass(Class.class);
	final ArgumentCaptor<ModelRepository> modelRepositoryArgumentCaptor = ArgumentCaptor.forClass(ModelRepository.class);
	
	@SuppressWarnings("unchecked")
	@Before
	public final void setup() {
		ReflectionTestUtils.setField(documentModelProxyFactory, "proxyFactory", proxyFactory);
		ReflectionTestUtils.setField(documentModelProxyFactory, "beanResolver", beanResolver);
		Mockito.when(proxyFactory.createProxy(classArgumentCaptor.capture(), modelRepositoryArgumentCaptor.capture())).thenReturn(documentModelAO);
		
		
	}
	
	
	@Test
	public final void documentModel() {
		Assert.assertEquals(documentModelAO, documentModelProxyFactory.documentModel());
		Assert.assertEquals(DocumentModelAO.class, classArgumentCaptor.getValue());
		@SuppressWarnings("unchecked")
		Map<String, Object> items = (Map<String, Object>) ReflectionTestUtils.getField(modelRepositoryArgumentCaptor.getValue(), "modelItems");
		Assert.assertEquals(1, items.size());
		
		Assert.assertEquals(OpportunityImpl.class, items.values().iterator().next().getClass());
	}
	
	
	

}