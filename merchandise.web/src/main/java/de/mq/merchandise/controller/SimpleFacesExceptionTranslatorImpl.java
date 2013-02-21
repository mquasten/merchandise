package de.mq.merchandise.controller;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.primefaces.context.RequestContext;
import org.springframework.stereotype.Component;

import de.mq.mapping.util.proxy.AOProxyFactory;
import de.mq.mapping.util.proxy.Action;
import de.mq.mapping.util.proxy.ModelRepository;

@Component
public class SimpleFacesExceptionTranslatorImpl implements Action {

	@Override
	public Object execute(final Class<?> result, final String bundle, final ModelRepository modelRepository) throws Exception {
		final FacesMessage facesMessage = new FacesMessage("Scheiss addresse...");
		facesMessage.setSeverity(FacesMessage.SEVERITY_ERROR);
		FacesContext.getCurrentInstance().addMessage(null, facesMessage);  
		RequestContext.getCurrentInstance().addCallbackParam("validationFailed", true);
		
		
		
		if( result.equals(java.lang.Void.class)){
			return null;
		}
		return modelRepository.beanResolver().getBeanOfType(AOProxyFactory.class).createProxy(result, modelRepository);
	}

}
