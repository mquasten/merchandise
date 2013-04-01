package de.mq.merchandise.model.support;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.faces.context.FacesContext;


public class SimpleConversationImpl implements Conversation {

	
	private final FacesContextFactory facesContextFactory;
	
	
	public SimpleConversationImpl(final FacesContextFactory facesContextFactory) {
		this.facesContextFactory = facesContextFactory;
	}

	
	
	static UUID DEFAULT_ID = UUID.nameUUIDFromBytes("You've been a very bad plagiarism from spring ! A very very bad bad plagiarism Java EE".getBytes());
	static final String KEY_CONVERSATION_ID = "Conversation-ID";
	static final String KEY_TRANSIENT = "transient";
	static final String KEY_TIMEOUT = "transient";
	static final String KEY_CONVERSATION_IN_SESSION_MAP= UUID.nameUUIDFromBytes("EJB will die, hopefully soon, and hopefully not only EJB, most better the complete shit".getBytes()).toString();
	
	@Override
	public void begin() {
		final  Map<String,Object> map = createOrGetModelRepositoryFromSession(facesContextFactory.facesContext());
		map.put(KEY_TRANSIENT , false);
		map.put(KEY_CONVERSATION_ID , DEFAULT_ID);
		
	}

	static  Map<String,Object> createOrGetModelRepositoryFromSession(final FacesContext facesContext) {
		if( ! facesContext.getExternalContext().getSessionMap().containsKey(KEY_CONVERSATION_IN_SESSION_MAP)) {
			facesContext.getExternalContext().getSessionMap().put(KEY_CONVERSATION_IN_SESSION_MAP, new HashMap<>());
		}
		@SuppressWarnings("unchecked")
		final  Map<String,Object>  map =  (Map<String, Object>) facesContext.getExternalContext().getSessionMap().get(KEY_CONVERSATION_IN_SESSION_MAP);
		return map;
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
	public String getId() {
		return getConversationId(facesContextFactory.facesContext());
	}

	static String getConversationId(FacesContext facesContext) {
		final Map<String,Object> modelRepository = createOrGetModelRepositoryFromSession(facesContext);
		return (String) modelRepository.get(KEY_CONVERSATION_ID);
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

	

	static  Boolean isTransient(FacesContext facesContext) {
		final Map<String,Object> map = createOrGetModelRepositoryFromSession(facesContext);
		final Boolean result = (Boolean) map.get(KEY_TRANSIENT);
		if( result == null){
			return true;
		}
		return result;
	}

	@Override
	public boolean isTransient() {
		return isTransient(facesContextFactory.facesContext());
		
	}
	

}

