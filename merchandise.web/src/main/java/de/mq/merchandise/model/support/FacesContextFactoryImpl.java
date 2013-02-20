package de.mq.merchandise.model.support;

import javax.faces.context.FacesContext;


public class FacesContextFactoryImpl implements FacesContextFactory {
	
	/* (non-Javadoc)
	 * @see de.mq.merchandise.web.model.support.FacesContextFactory#facesContext()
	 */
	@Override
	public final FacesContext  facesContext() {
		return FacesContext.getCurrentInstance();
		
	}
    
	
	
}
