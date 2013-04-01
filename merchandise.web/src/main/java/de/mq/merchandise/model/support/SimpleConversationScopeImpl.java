package de.mq.merchandise.model.support;

import java.util.Map;

import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.config.Scope;


public class SimpleConversationScopeImpl implements Scope {
	
	
	private final FacesContextFactory facesContextFactory; 
	
	
	public SimpleConversationScopeImpl(FacesContextFactory facesContextFactory) {
		this.facesContextFactory = facesContextFactory;
	}

	

	@Override
	public Object get(String name, ObjectFactory<?> objectFactory) {
		if ( SimpleConversationImpl.isTransient(facesContextFactory.facesContext()) ) {
			if ( ! facesContextFactory.facesContext().getExternalContext().getRequestMap().containsKey(name) ) {
				facesContextFactory.facesContext().getExternalContext().getRequestMap().put(name, objectFactory.getObject());
			}
			return facesContextFactory.facesContext().getExternalContext().getRequestMap().get(name);
		}
		
		
		if (! getMap().containsKey(name )){
			getMap().put(name, objectFactory.getObject());
			
		}
		return getMap().get(name);
	}

	@Override
	public Object remove(final String name) {
		if ( SimpleConversationImpl.isTransient(facesContextFactory.facesContext()) ) {
			return facesContextFactory.facesContext().getExternalContext().getRequestMap().remove(name);
		}
		
	    return getMap().remove(name);
	  
	   
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
		return (String) getMap().get(SimpleConversationImpl.KEY_CONVERSATION_ID);
		
	}

	private Map<String,Object> getMap() {
		return SimpleConversationImpl.createOrGetModelRepositoryFromSession(facesContextFactory.facesContext());
	}

}
