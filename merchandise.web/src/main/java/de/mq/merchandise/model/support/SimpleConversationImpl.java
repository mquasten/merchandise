package de.mq.merchandise.model.support;

import java.util.Map;

import javax.faces.context.FacesContext;



public  class SimpleConversationImpl extends AbstractConversation implements Conversation {

	public SimpleConversationImpl(final FacesContextFactory facesContextFactory) {
		super(facesContextFactory);
	}

	
	
	@Override
	public void begin() {
		final  Map<String,Object> map = createOrGetModelRepositoryFromSession(facesContextFactory.facesContext());
		map.put(KEY_TRANSIENT , false);
		map.put(KEY_CONVERSATION_ID , DEFAULT_ID);
		
	}

	  @Override
	public void begin(final String id) {
		final  Map<String,Object> map = createOrGetModelRepositoryFromSession(facesContextFactory.facesContext());
		map.put(KEY_TRANSIENT , false);
		map.put(KEY_CONVERSATION_ID , id);
		
	}

	@Override
	public void end() {
		createOrGetModelRepositoryFromSession(facesContextFactory.facesContext()).clear();
		
	}


	@Override
	public long getTimeout() {
		final Map<String,Object> map = createOrGetModelRepositoryFromSession(facesContextFactory.facesContext());
		final Long timeOut =  (Long) map.get(KEY_TIMEOUT);
		if(timeOut==null){
			return Long.MAX_VALUE;
		}
		return timeOut;
	}

	@Override
	public void setTimeout(long milliseconds) {
		final Map<String,Object> map = createOrGetModelRepositoryFromSession(facesContextFactory.facesContext());
		map.put(KEY_TIMEOUT , milliseconds);
		
	}

	

	@Override
	public boolean isTransient() {
		return isTransient(facesContextFactory.facesContext());
		
	}



	
	
	
	

}

