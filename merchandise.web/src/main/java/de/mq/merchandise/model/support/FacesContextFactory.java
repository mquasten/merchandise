package de.mq.merchandise.model.support;

import javax.faces.context.FacesContext;

import org.primefaces.context.RequestContext;

public interface FacesContextFactory {

	FacesContext facesContext();
	RequestContext requestContext();

}