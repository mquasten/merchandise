package de.mq.merchandise.model.support;

import java.util.Map;

import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.config.Scope;

public class SimpleViewScopeImpl implements Scope {
	
	
	private final FacesContextFactory facesContextFactory;
	
	public SimpleViewScopeImpl(final FacesContextFactory facesContextFactory) {
		this.facesContextFactory=facesContextFactory;
	}

	public final Object get(final String name, final ObjectFactory<?> objectFactory) {
	
		if (facesContextFactory.facesContext().getViewRoot() == null) {
			return null;
		}
		
		final Map<String, Object> viewMap = facesContextFactory.facesContext().getViewRoot().getViewMap();
		
		
		if (!viewMap.containsKey(name)) {
			viewMap.put(name, objectFactory.getObject());
		}
		return viewMap.get(name);
	}

	public final  Object remove(String name) {
		if (facesContextFactory.facesContext().getViewRoot() == null) {
			return null;
		}
		return facesContextFactory.facesContext().getViewRoot().getViewMap().remove(name);

	}

	public final Object resolveContextualObject(final String key) {
		return null;
	}

	public final String getConversationId() {
		return null;
	}

	@Override
	public void registerDestructionCallback(final String arg0, final Runnable arg1) {
		
	}

}
