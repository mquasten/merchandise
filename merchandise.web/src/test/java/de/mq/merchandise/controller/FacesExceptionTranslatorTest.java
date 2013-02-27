package de.mq.merchandise.controller;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import junit.framework.Assert;

import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.primefaces.context.RequestContext;

import de.mq.mapping.util.proxy.Action;
import de.mq.mapping.util.proxy.ModelRepository;
import de.mq.merchandise.model.support.FacesContextFactory;

public class FacesExceptionTranslatorTest {
	
	private static final String ERROR_MESSAGE = "Eine FehlerMeldung";
	private static final String MESSAGE_BUNDLE = "messageBundle";

	@Test
	public final void translate() throws Exception {
		final MessageSouceController messageSourceController = Mockito.mock(MessageSouceController.class);
		final FacesContextFactory facesContextFactory = Mockito.mock(FacesContextFactory.class);
		final Action action = new SimpleFacesExceptionTranslatorImpl(messageSourceController, facesContextFactory);
		final ModelRepository modelRepository = Mockito.mock(ModelRepository.class);
		final FacesContext facesContext = Mockito.mock(FacesContext.class);
		final RequestContext requestContext = Mockito.mock(RequestContext.class);
		Mockito.when(messageSourceController.get(MESSAGE_BUNDLE)).thenReturn(ERROR_MESSAGE);
		Mockito.when(facesContextFactory.facesContext()).thenReturn(facesContext);
		Mockito.when(facesContextFactory.requestContext()).thenReturn(requestContext);
		
		final ArgumentCaptor<FacesMessage> facesMessageArgumentCaptor = ArgumentCaptor.forClass(FacesMessage.class);
		final ArgumentCaptor<String> clientIdArgumentCaptor = ArgumentCaptor.forClass(String.class);
		
		Assert.assertNull(action.execute(Void.class, MESSAGE_BUNDLE, modelRepository));
		
		Mockito.verify(facesContext).addMessage(clientIdArgumentCaptor.capture(), facesMessageArgumentCaptor.capture());
		
		Assert.assertEquals(ERROR_MESSAGE, facesMessageArgumentCaptor.getValue().getSummary());
		Assert.assertEquals(FacesMessage.SEVERITY_ERROR, facesMessageArgumentCaptor.getValue().getSeverity());
		Assert.assertNull(clientIdArgumentCaptor.getValue());
		
		Mockito.verify(requestContext).addCallbackParam(SimpleFacesExceptionTranslatorImpl.VALIDATION_FAILED, true);
	}

}
