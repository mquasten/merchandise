package de.mq.merchandise.opportunity.support;

import java.util.Map;



public interface DocumentsAware {
	
	

	Map<String, byte[]> documents();

	void assignWebLink(final String name);
	
	void assignDocument(final String name);

	void removeDocument(final String name);

	
	
	String urlForName(final String name);

}