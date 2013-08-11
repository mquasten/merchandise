package de.mq.merchandise.controller;

import junit.framework.Assert;

import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;

import de.mq.merchandise.model.support.WebProxyFactory;
import de.mq.merchandise.opportunity.ClassificationService;
import de.mq.merchandise.opportunity.support.OpportunityService;

public class OpportunityControllerFactoryTest {
	
	private final  WebProxyFactory webProxyFactory = Mockito.mock(WebProxyFactory.class);
	private final OpportunityControllerFactoryImpl opportunityControllerFactoryImpl = new OpportunityControllerFactoryImpl();
	private OpportunityController result = Mockito.mock(OpportunityController.class);
	
	private OpportunityService opportunityService = Mockito.mock(OpportunityService.class);
	private ClassificationService classificationService = Mockito.mock(ClassificationService.class);
	
	@SuppressWarnings("unchecked")
	@Test
	public final void createProxy() {
		ReflectionTestUtils.setField(opportunityControllerFactoryImpl, "webProxyFactory", webProxyFactory);
		
		ReflectionTestUtils.setField(opportunityControllerFactoryImpl, "opportunityService", opportunityService);
		ReflectionTestUtils.setField(opportunityControllerFactoryImpl, "classificationService", classificationService);
		@SuppressWarnings("rawtypes")
		final ArgumentCaptor<Class> clazzCaptor = ArgumentCaptor.forClass(Class.class);
		final ArgumentCaptor<OpportunityController> opportunityControllerArgumentCaptor = ArgumentCaptor.forClass(OpportunityController.class);
        Mockito.when(webProxyFactory.webModell(clazzCaptor.capture(), opportunityControllerArgumentCaptor.capture())).thenReturn(result);
      
      
        Assert.assertEquals(result, opportunityControllerFactoryImpl.opportunityController());
        
        Assert.assertEquals(OpportunityController.class, clazzCaptor.getValue());
        
       Assert.assertEquals(opportunityService, ReflectionTestUtils.getField(opportunityControllerArgumentCaptor.getValue(), "opportunityService"));
       
       Assert.assertEquals(classificationService, ReflectionTestUtils.getField(opportunityControllerArgumentCaptor.getValue(), "classificationService"));
		
	}
	

}
