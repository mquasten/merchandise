package de.mq.merchandise.model;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component("user")
@Scope( "session")
public class UserImpl implements User {
	
	private String language="en";

	
	                      
	public  final String getLanguage() {
		return language;
	}

	/* (non-Javadoc)
	 * @see de.mq.merchandise.web.model.User#setLanguage(java.lang.String)
	 */
	@Override
	public final void setLanguage(final String language) {
		this.language = language;
	}
	
	
	
	
	

}
