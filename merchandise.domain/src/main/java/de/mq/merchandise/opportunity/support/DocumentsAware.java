package de.mq.merchandise.opportunity.support;

import java.util.Map;



public interface DocumentsAware {
	
	enum DocumentType {
		PDF(".pdf"),
		Link("");
		
		private final String extension;
		DocumentType(final String extension ){
			this.extension=extension;
		}
		
		public final String key(String name) {
			return  name +extension;
		}
		
	}

	Map<String, byte[]> documents();

	void assignDocument(final String name, final DocumentType documentType, final byte[] document);
	
	void assignDocument(final String name);

	void removeDocument(final String name, final DocumentType documentType);

	
	
	String urlForName(final String name);

}