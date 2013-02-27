package de.mq.merchandise.model.support;

import javax.faces.context.FacesContext;

import org.primefaces.context.RequestContext;


public class FacesContextFactoryImpl implements FacesContextFactory {
	
	
	@Override
	public final FacesContext  facesContext() {
		return FacesContext.getCurrentInstance();
		
	}
	
    @Override
	public final RequestContext requestContext() {
		return RequestContext.getCurrentInstance();
	}
	
}
