package de.mq.merchandise.model.support;

import java.util.HashMap;
import java.util.Map;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.config.Scope;

import de.mq.merchandise.customer.support.CustomerAO;

public class ConversationScopeTest {
	
	private static final String CUSTOMER_KEY = "customer";
	
	private final FacesContextFactory facesContextFactory = Mockito.mock(FacesContextFactory.class);
	private final FacesContext facesContext = Mockito.mock(FacesContext.class);
	
	private final ExternalContext externalContext = Mockito.mock(ExternalContext.class);
	
	private final Map<String,Object> sessionMap = new HashMap<>();
	private final Scope scope = new SimpleConversationScopeImpl(facesContextFactory);
	
	final CustomerAO customer = Mockito.mock(CustomerAO.class);
	
	@SuppressWarnings("unchecked")
	private final ObjectFactory<Object> objectFactory = Mockito.mock(ObjectFactory.class);
	
	@Before
	public void setup() {
		sessionMap.clear();
		Mockito.when(facesContextFactory.facesContext()).thenReturn(facesContext);
		Mockito.when(facesContext.getExternalContext()).thenReturn(externalContext);
		Mockito.when(externalContext.getSessionMap()).thenReturn(sessionMap);
		
	}
	
	@Test
	public void getCreateNewEntry() {
		
        Mockito.when(objectFactory.getObject()).thenReturn(customer);
		final Map<String, Object> map = new HashMap<>();
		map.put(AbstractConversation.KEY_TRANSIENT, false);
		
		sessionMap.put(AbstractConversation.KEY_CONVERSATION_IN_SESSION_MAP, map);
		Assert.assertEquals(customer, scope.get(CUSTOMER_KEY, objectFactory));
		final Map<String,Object> results = getResultMap();
		Assert.assertTrue(results.containsKey(CUSTOMER_KEY));
		Assert.assertEquals(customer, results.get(CUSTOMER_KEY));
		
	}

	
	@SuppressWarnings("unchecked")
	private Map<String, Object> getResultMap() {
		return (Map<String, Object>) sessionMap.get(AbstractConversation.KEY_CONVERSATION_IN_SESSION_MAP);
	}
	
	@Test
	public final void getExistingFromConversation() {
		
		final Map<String, Object> map = new HashMap<>();
		map.put(AbstractConversation.KEY_TRANSIENT, false);
		map.put(CUSTOMER_KEY, customer);
		sessionMap.put(AbstractConversation.KEY_CONVERSATION_IN_SESSION_MAP, map);
		
		Assert.assertEquals(customer, scope.get(CUSTOMER_KEY, objectFactory));
		Mockito.verifyZeroInteractions(objectFactory);
		
	}
	
	@Test
	public final void getFromRequestCreateItInMap() {
		final Map<String,Object> requestMap = new HashMap<>();
		Mockito.when(externalContext.getRequestMap()).thenReturn(requestMap);
		Mockito.when(objectFactory.getObject()).thenReturn(customer);
		Assert.assertEquals(customer, scope.get(CUSTOMER_KEY, objectFactory));
		Assert.assertTrue(requestMap.containsKey(CUSTOMER_KEY));
		Assert.assertEquals(customer, requestMap.get(CUSTOMER_KEY));
	}
	
	@Test
	public final void getFromRequestExistsInMap(){
		final Map<String,Object> requestMap = new HashMap<>();
		requestMap.put(CUSTOMER_KEY, customer);
		Mockito.when(externalContext.getRequestMap()).thenReturn(requestMap);
		Assert.assertEquals(customer, scope.get(CUSTOMER_KEY, objectFactory));
		Mockito.verifyZeroInteractions(objectFactory);
	}
	
	@Test
	public final void removeFromConversation() {
		Mockito.when(objectFactory.getObject()).thenReturn(customer);
		final Map<String, Object> map = new HashMap<>();
		map.put(AbstractConversation.KEY_TRANSIENT, false);
		map.put(CUSTOMER_KEY, customer);
		sessionMap.put(AbstractConversation.KEY_CONVERSATION_IN_SESSION_MAP, map);
		
		Assert.assertTrue(getResultMap().containsKey(CUSTOMER_KEY));
		scope.remove(CUSTOMER_KEY);
		Assert.assertFalse(getResultMap().containsKey(CUSTOMER_KEY));
		
	}
	
	@Test
	public final void removeFromRequest() {
		final Map<String,Object> requestMap = new HashMap<>();
		requestMap.put(CUSTOMER_KEY, customer);
		Mockito.when(externalContext.getRequestMap()).thenReturn(requestMap);
		
		Assert.assertFalse(requestMap.isEmpty());
		scope.remove(CUSTOMER_KEY);
		Assert.assertTrue(requestMap.isEmpty());
	}
	
	@Test
	public final void getConversationId() {
		final Map<String, Object> map = new HashMap<>();
		map.put(AbstractConversation.KEY_TRANSIENT, false);
		map.put(AbstractConversation.KEY_CONVERSATION_ID, AbstractConversation.DEFAULT_ID);
		sessionMap.put(AbstractConversation.KEY_CONVERSATION_IN_SESSION_MAP, map);
		
		Assert.assertEquals(AbstractConversation.DEFAULT_ID, scope.getConversationId());
	}
	
	
	@Test
	public final void unusedMethodsCoverageOnly() {
		scope.registerDestructionCallback("whatEver", null);
		scope.resolveContextualObject("whatEver");
	}

}
