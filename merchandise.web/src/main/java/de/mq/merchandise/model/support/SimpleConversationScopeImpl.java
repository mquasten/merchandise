package de.mq.merchandise.model.support;

import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.config.Scope;


public class SimpleConversationScopeImpl extends AbstractConversation implements Scope {

	public SimpleConversationScopeImpl(final FacesContextFactory facesContextFactory) {
		super(facesContextFactory);
	}

	@Override
	public Object get(String name, ObjectFactory<?> objectFactory) {
		if ( isTransient(facesContextFactory.facesContext()) ) {
			addToRequestMapIfNotExists(name, objectFactory);
			return facesContextFactory.facesContext().getExternalContext().getRequestMap().get(name);
		}
		
		if (! createOrGetModelRepositoryFromSession(facesContextFactory.facesContext()).containsKey(name )){
			createOrGetModelRepositoryFromSession(facesContextFactory.facesContext()).put(name, objectFactory.getObject());	
		}
		return createOrGetModelRepositoryFromSession(facesContextFactory.facesContext()).get(name);
	}

	private void addToRequestMapIfNotExists(String name, ObjectFactory<?> objectFactory) {
		if ( ! facesContextFactory.facesContext().getExternalContext().getRequestMap().containsKey(name) ) {
			facesContextFactory.facesContext().getExternalContext().getRequestMap().put(name, objectFactory.getObject());
		}
	}

	@Override
	public Object remove(final String name) {
		if ( isTransient(facesContextFactory.facesContext()) ) {
			return facesContextFactory.facesContext().getExternalContext().getRequestMap().remove(name);
		}
	    return createOrGetModelRepositoryFromSession(facesContextFactory.facesContext()).remove(name);
	}

	@Override
	public void registerDestructionCallback(String name, Runnable callback) {
		// TODO Auto-generated method stub
	}

	@Override
	public Object resolveContextualObject(String key) {
		// TODO Auto-generated method stub
		return null;
	}

	

	@Override
	public String getConversationId() {
		return super.getId();
	} 

}
