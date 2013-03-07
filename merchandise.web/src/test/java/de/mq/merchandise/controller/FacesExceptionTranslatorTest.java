package de.mq.merchandise.controller;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.primefaces.context.RequestContext;

import de.mq.mapping.util.proxy.AOProxyFactory;
import de.mq.mapping.util.proxy.Action;
import de.mq.mapping.util.proxy.BeanResolver;
import de.mq.mapping.util.proxy.ExceptionTranslation;
import de.mq.mapping.util.proxy.ModelRepository;
import de.mq.merchandise.customer.support.CustomerAO;
import de.mq.merchandise.model.support.FacesContextFactory;

public class FacesExceptionTranslatorTest {
	
	private static final String ERROR_MESSAGE = "Eine FehlerMeldung";
	private static final String MESSAGE_BUNDLE = "messageBundle";
	
	private  MessageSouceController messageSourceController;
	private  FacesContextFactory facesContextFactory;
	private  Action action;
	private  ModelRepository modelRepository;
	private  FacesContext facesContext;
	private  RequestContext requestContext;
	
	private ArgumentCaptor<FacesMessage> facesMessageArgumentCaptor;
	private ArgumentCaptor<String> clientIdArgumentCaptor ;
	private ExceptionTranslation exceptionTranslation ;
	
	private final Object args[] = new Object[] { } ; 
	
	private Throwable throwThrowable;
	
	@Before
	public final void setup() {
		messageSourceController = Mockito.mock(MessageSouceController.class);
		facesContextFactory = Mockito.mock(FacesContextFactory.class);
		action = new SimpleFacesExceptionTranslatorImpl(messageSourceController, facesContextFactory);
		modelRepository = Mockito.mock(ModelRepository.class);
		facesContext = Mockito.mock(FacesContext.class);
		requestContext = Mockito.mock(RequestContext.class);
		
		Mockito.when(messageSourceController.get(MESSAGE_BUNDLE)).thenReturn(ERROR_MESSAGE);
		Mockito.when(facesContextFactory.facesContext()).thenReturn(facesContext);
		Mockito.when(facesContextFactory.requestContext()).thenReturn(requestContext);
		
		facesMessageArgumentCaptor = ArgumentCaptor.forClass(FacesMessage.class);
		clientIdArgumentCaptor = ArgumentCaptor.forClass(String.class);
		throwThrowable = Mockito.mock(Throwable.class);
		exceptionTranslation = Mockito.mock(ExceptionTranslation.class);
		Mockito.when(exceptionTranslation.bundle()).thenReturn(MESSAGE_BUNDLE);
		Mockito.when(exceptionTranslation.resultExpression()).thenReturn("");
	}
	

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Test
	public final void translate() throws Exception {
		
		
		
		
		Mockito.when(exceptionTranslation.result()).thenReturn((Class) Void.class);
		Assert.assertNull(action.execute(exceptionTranslation, modelRepository, throwThrowable, args));
		
		Mockito.verify(facesContext).addMessage(clientIdArgumentCaptor.capture(), facesMessageArgumentCaptor.capture());
		
		Assert.assertEquals(ERROR_MESSAGE, facesMessageArgumentCaptor.getValue().getSummary());
		Assert.assertEquals(FacesMessage.SEVERITY_ERROR, facesMessageArgumentCaptor.getValue().getSeverity());
		Assert.assertNull(clientIdArgumentCaptor.getValue());
		
		Mockito.verify(requestContext).addCallbackParam(SimpleFacesExceptionTranslatorImpl.VALIDATION_FAILED, true);
		Mockito.verifyNoMoreInteractions(modelRepository);
		
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Test
	public final void translateWithResult() throws Exception {
		Mockito.when(exceptionTranslation.result()).thenReturn((Class) CustomerAO.class);
		
		final BeanResolver beanResolver = Mockito.mock(BeanResolver.class);
		final AOProxyFactory aoProxyFactory = Mockito.mock(AOProxyFactory.class);
		final CustomerAO customerAO = Mockito.mock(CustomerAO.class);
		Mockito.when(modelRepository.beanResolver()).thenReturn(beanResolver);
		Mockito.when(beanResolver.getBeanOfType(AOProxyFactory.class)).thenReturn(aoProxyFactory);
		Mockito.when(aoProxyFactory.createProxy(CustomerAO.class, modelRepository)).thenReturn(customerAO);
		
		Assert.assertEquals(customerAO, action.execute(exceptionTranslation, modelRepository, throwThrowable, args));
		
		Mockito.verify(facesContext).addMessage(clientIdArgumentCaptor.capture(), facesMessageArgumentCaptor.capture());
		Assert.assertEquals(ERROR_MESSAGE, facesMessageArgumentCaptor.getValue().getSummary());
		Assert.assertEquals(FacesMessage.SEVERITY_ERROR, facesMessageArgumentCaptor.getValue().getSeverity());
		Assert.assertNull(clientIdArgumentCaptor.getValue());
		Mockito.verify(requestContext).addCallbackParam(SimpleFacesExceptionTranslatorImpl.VALIDATION_FAILED, true);
		
		Mockito.verify(aoProxyFactory).createProxy(CustomerAO.class, modelRepository);
		
		Mockito.verify(beanResolver).getBeanOfType(AOProxyFactory.class);
		
		
	}

}
