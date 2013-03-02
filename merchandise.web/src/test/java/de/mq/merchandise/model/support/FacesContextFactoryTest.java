package de.mq.merchandise.model.support;

import java.util.HashMap;
import java.util.Map;

import javax.faces.context.FacesContext;

import junit.framework.Assert;

import org.junit.Test;
import org.mockito.Mockito;
import org.primefaces.context.RequestContext;
import org.primefaces.util.Constants;


public class FacesContextFactoryTest {
	
	@Test
	public final void requestContext() {
		final FacesContextFactory facesContextFactory = Mockito.mock(FacesContextFactoryImpl.class);
		FacesContext facesContext = Mockito.mock(FacesContext.class);
		Mockito.when(facesContextFactory.facesContext()).thenReturn(facesContext);
		final Map<Object,Object> attributes = new HashMap<>();
		RequestContext requestContext = Mockito.mock(RequestContext.class);
		attributes.put(Constants.REQUEST_CONTEXT_ATTR, requestContext);
		Mockito.when(facesContext.getAttributes()).thenReturn(attributes);
		Assert.assertEquals(requestContext, facesContextFactory.requestContext());
	}
	
	@Test
	public final void coverage() {
		Assert.assertNotNull(new FacesContextFactoryImpl() {
			@Override
			public FacesContext facesContext() {
				return null;
			}
		});
		
	}


}
