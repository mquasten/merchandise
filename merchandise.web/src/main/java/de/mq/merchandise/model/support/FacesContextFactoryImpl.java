package de.mq.merchandise.model.support;

import javax.faces.context.FacesContext;

import org.primefaces.context.RequestContext;
import org.primefaces.util.Constants;


public abstract class FacesContextFactoryImpl implements FacesContextFactory {
	
	@Override
	public abstract FacesContext  facesContext(); 
	
	
	
    @Override
	public final  RequestContext requestContext() {
		return  (RequestContext) facesContext().getAttributes().get(Constants.REQUEST_CONTEXT_ATTR);
	}
	
}
