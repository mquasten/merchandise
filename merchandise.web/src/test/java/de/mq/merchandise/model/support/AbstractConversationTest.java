package de.mq.merchandise.model.support;

import java.util.HashMap;
import java.util.Map;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import de.mq.merchandise.customer.Customer;

public class AbstractConversationTest {
	
	private final FacesContextFactory facesContextFactory = Mockito.mock(FacesContextFactory.class);
	private final FacesContext facesContext = Mockito.mock(FacesContext.class);
	
	private final ExternalContext externalContext = Mockito.mock(ExternalContext.class);
	
	private final Map<String,Object> sessionMap = new HashMap<>();
	private final AbstractConversation conversation = new AbstractConversation(facesContextFactory){};
	
	@Before
	public void setup() {
		sessionMap.clear();
		Mockito.when(facesContextFactory.facesContext()).thenReturn(facesContext);
		Mockito.when(facesContext.getExternalContext()).thenReturn(externalContext);
		Mockito.when(externalContext.getSessionMap()).thenReturn(sessionMap);
		
	}
	
	
	@Test
	public final void createOrGetModelRepositoryFromSessionLikeAVirgin() {
		Assert.assertFalse(sessionMap.containsKey(AbstractConversation.KEY_CONVERSATION_IN_SESSION_MAP));
		final Map<String,Object> result =  conversation.createOrGetModelRepositoryFromSession(facesContext);
		Assert.assertTrue(sessionMap.containsKey(AbstractConversation.KEY_CONVERSATION_IN_SESSION_MAP));
		Assert.assertEquals(HashMap.class, sessionMap.get(AbstractConversation.KEY_CONVERSATION_IN_SESSION_MAP).getClass());
		Assert.assertTrue(((Map<?,?>)sessionMap.get(AbstractConversation.KEY_CONVERSATION_IN_SESSION_MAP)).isEmpty());
		Assert.assertEquals(result, sessionMap.get(AbstractConversation.KEY_CONVERSATION_IN_SESSION_MAP));
	}
	
	@Test
	public final void createOrGetModelRepositoryFromSessionExisting() {
		final Customer customer = Mockito.mock(Customer.class);
		final Map<String,Object> beans = new HashMap<>();
		beans.put("customer", customer);
		sessionMap.put(AbstractConversation.KEY_CONVERSATION_IN_SESSION_MAP, beans);
		Assert.assertEquals(beans, conversation.createOrGetModelRepositoryFromSession(facesContext));
		
	}
	
	@Test
	public final void isTransientDefault() {
		Assert.assertTrue(conversation.isTransient(facesContext));
	}
	
	@Test
	public final void isTransientExistsInSession() {
		final Map<String,Object> beans = new HashMap<>();
		beans.put(AbstractConversation.KEY_TRANSIENT, Boolean.FALSE);
		sessionMap.put(AbstractConversation.KEY_CONVERSATION_IN_SESSION_MAP,beans);
		Assert.assertFalse(conversation.isTransient(facesContext));
		
	}
	
	

}
