package de.mq.merchandise.controller;

import javax.faces.application.FacesMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import de.mq.mapping.util.proxy.AOProxyFactory;
import de.mq.mapping.util.proxy.Action;
import de.mq.mapping.util.proxy.ModelRepository;
import de.mq.merchandise.model.support.FacesContextFactory;

@Component
public class SimpleFacesExceptionTranslatorImpl implements Action {
	
	static final String VALIDATION_FAILED = "validationFailed";
	final private MessageSouceController messageSourceController;
	final private FacesContextFactory facesContextFactory;
	
	@Autowired
	public SimpleFacesExceptionTranslatorImpl(final MessageSouceController messageSourceController, final FacesContextFactory facesContextFactory) {
        this.messageSourceController=messageSourceController;		
        this.facesContextFactory=facesContextFactory;
	}

	@Override
	public Object execute(final Class<?> result, final String bundle, final ModelRepository modelRepository) throws Exception {
		
		final FacesMessage facesMessage = new FacesMessage(messageSourceController.get(bundle));
		facesMessage.setSeverity(FacesMessage.SEVERITY_ERROR);
		
		facesContextFactory.facesContext().addMessage(null, facesMessage);
		facesContextFactory.requestContext().addCallbackParam(VALIDATION_FAILED, true);
		
		
		
		if( result.equals(java.lang.Void.class)){
			return null;
		}
		return modelRepository.beanResolver().getBeanOfType(AOProxyFactory.class).createProxy(result, modelRepository);
	}

}
