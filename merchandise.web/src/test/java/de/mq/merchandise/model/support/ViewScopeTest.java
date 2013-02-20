package de.mq.merchandise.model.support;

import java.util.HashMap;
import java.util.Map;

import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.config.Scope;

import de.mq.merchandise.model.support.FacesContextFactory;
import de.mq.merchandise.model.support.SimpleViewScopeImpl;


public class ViewScopeTest {
	
	final static String KEY = "managedBeanName";
	private final Object managedBean = Mockito.mock(Object.class);
	private final FacesContextFactory facesContextFactory = Mockito.mock(FacesContextFactory.class);
	private final FacesContext facesContext = Mockito.mock(FacesContext.class);
	private final UIViewRoot  viewRoot = Mockito.mock(UIViewRoot.class);
	@SuppressWarnings("unchecked")
    private final ObjectFactory<Object> objectFactory = Mockito.mock(ObjectFactory.class);
	
	final Map<String,Object> map = new HashMap<>();
	
	@Before
	public final void setup() {
		Mockito.when(viewRoot.getViewMap()).thenReturn(map);
		Mockito.when(facesContext.getViewRoot()).thenReturn(viewRoot);
		Mockito.when(facesContextFactory.facesContext()).thenReturn(facesContext);
	}
	
	@Test
	public final void getExists() {
		
		map.put(KEY, managedBean);
		
		final Scope  scope = new SimpleViewScopeImpl(facesContextFactory);
		
		
		final Object result = scope.get(KEY, objectFactory);
		
		Assert.assertEquals(managedBean, result);
		
		Mockito.verify(facesContext,  Mockito.atLeastOnce()).getViewRoot();
		Mockito.verify(viewRoot).getViewMap();
		Mockito.verify(objectFactory, Mockito.never()).getObject();
		
		
	}
	
	@Test
	public final void getNotExists() {
		final Scope  scope = new SimpleViewScopeImpl(facesContextFactory);
		Mockito.when(objectFactory.getObject()).thenReturn(managedBean);
		Assert.assertTrue(map.isEmpty());
		final Object result = scope.get(KEY, objectFactory);
		
		Assert.assertEquals(managedBean, result);
		Mockito.verify(facesContext,  Mockito.atLeastOnce()).getViewRoot();
		Mockito.verify(viewRoot).getViewMap();
		Mockito.verify(objectFactory).getObject();
		
		Assert.assertEquals(1, map.size());
		Assert.assertEquals(KEY, map.keySet().iterator().next());
		Assert.assertEquals(managedBean, map.values().iterator().next());
	}
	
	@Test
	public final void getNoRootView() {
		Mockito.when(facesContext.getViewRoot()).thenReturn(null);
		Assert.assertNull(new SimpleViewScopeImpl(facesContextFactory).get(KEY, objectFactory));
		
		Mockito.verify(facesContext, Mockito.times(1)).getViewRoot();
		Mockito.verify(objectFactory, Mockito.never()).getObject();
		Mockito.verify(viewRoot, Mockito.never()).getViewMap();
	}
	
	
	@Test
	public final void remove() {
		map.put(KEY, managedBean);
		
		final Scope  scope = new SimpleViewScopeImpl(facesContextFactory);
		
		
		final Object result = scope.remove(KEY);
		Assert.assertEquals(managedBean, result);
		Assert.assertTrue(map.isEmpty());
		
		Mockito.verify(facesContext, Mockito.atLeastOnce()).getViewRoot();
		Mockito.verify(viewRoot).getViewMap();
		
	}
	
	@Test
	public final void removeNoViewRoot() {
		Mockito.when(facesContext.getViewRoot()).thenReturn(null);
		final Scope  scope = new SimpleViewScopeImpl(facesContextFactory);
		Assert.assertNull(scope.remove(KEY));
		
		Mockito.verify(facesContext, Mockito.times(1)).getViewRoot();
		Mockito.verify(viewRoot, Mockito.times(0)).getViewMap();
	}
	
	@Test
	public final void wasteMethods() {
		final Scope  scope = new SimpleViewScopeImpl(facesContextFactory);
		scope.getConversationId();
		scope.resolveContextualObject("dontLetMeGetMe");
		scope.registerDestructionCallback("dontLetMeGetMe", Mockito.mock(Runnable.class));
	}
	

}
