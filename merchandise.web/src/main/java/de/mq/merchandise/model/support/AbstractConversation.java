package de.mq.merchandise.model.support;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.faces.context.FacesContext;

abstract class AbstractConversation {

	static String DEFAULT_ID = UUID.nameUUIDFromBytes("You've been a very bad plagiarism from spring ! A very very bad bad plagiarism Java EE".getBytes()).toString();
	static final String KEY_CONVERSATION_ID = "Conversation-ID";
	static final String KEY_TRANSIENT = "transient";
	static final String KEY_TIMEOUT = "timeout";
	static final String KEY_CONVERSATION_IN_SESSION_MAP = UUID.nameUUIDFromBytes("EJB will die, hopefully soon, and hopefully not only EJB, most better the complete shit".getBytes()).toString();

	protected final FacesContextFactory facesContextFactory; 
	
	protected AbstractConversation(FacesContextFactory facesContextFactory) {
		this.facesContextFactory = facesContextFactory;
	}

	@SuppressWarnings("unchecked")
	protected Map<String,Object> createOrGetModelRepositoryFromSession(final FacesContext facesContext) {
		if( ! facesContext.getExternalContext().getSessionMap().containsKey(KEY_CONVERSATION_IN_SESSION_MAP)) {
			facesContext.getExternalContext().getSessionMap().put(KEY_CONVERSATION_IN_SESSION_MAP, new HashMap<>());
		}
		return (Map<String, Object>) facesContext.getExternalContext().getSessionMap().get(KEY_CONVERSATION_IN_SESSION_MAP);
	}


	protected Boolean isTransient(FacesContext facesContext) {
		final Map<String,Object> map = createOrGetModelRepositoryFromSession(facesContext);
		final Boolean result = (Boolean) map.get(KEY_TRANSIENT);
		if( result == null){
			return true;
		}
		return result;
	}
	
	
	public String getId() {
		return (String) createOrGetModelRepositoryFromSession(facesContextFactory.facesContext()).get(AbstractConversation.KEY_CONVERSATION_ID);
		
	}

}