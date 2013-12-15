package de.mq.merchandise.controller;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;

import de.mq.merchandise.model.support.WebProxyFactory;
import de.mq.merchandise.opportunity.ResourceOperations;

public class DocumentControllerFactoryTest {
	
	private DocumentControllerFactoryImpl documentControllerFactory = new DocumentControllerFactoryImpl();
	
	private WebProxyFactory webProxyFactory = Mockito.mock(WebProxyFactory.class);
	
	
	
	private ResourceOperations resourceOperations = Mockito.mock(ResourceOperations.class);
	
	private ArgumentCaptor<DocumentControllerImpl> documentControllerCaptor = ArgumentCaptor.forClass(DocumentControllerImpl.class);
	
	@SuppressWarnings("rawtypes")
	private ArgumentCaptor<Class> classCaptor = ArgumentCaptor.forClass(Class.class);
	
	private DocumentController documentController = Mockito.mock(DocumentController.class);
	
	
	@SuppressWarnings("unchecked")
	@Before
	public final void setup() {
		ReflectionTestUtils.setField(documentControllerFactory, "webProxyFactory", webProxyFactory);
		
		ReflectionTestUtils.setField(documentControllerFactory, "resourceOperations", resourceOperations);
		
		Mockito.when(webProxyFactory.webModell(classCaptor.capture(), documentControllerCaptor.capture())).thenReturn(documentController);
	}
	
	@Test
	public final void documentAware() {
		
		Assert.assertEquals(documentController, documentControllerFactory.documentController());
		Assert.assertEquals(DocumentController.class, classCaptor.getValue());
		
		Assert.assertEquals(resourceOperations, ReflectionTestUtils.getField(documentControllerCaptor.getValue(), "resourceOperations"));
	}

}
