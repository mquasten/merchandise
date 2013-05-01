package de.mq.merchandise.opportunity.support;

import java.util.Map;

import de.mq.merchandise.BasicEntity;
import de.mq.merchandise.customer.Customer;

public interface CommercialSubject extends BasicEntity {
	
	
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

	void assignDocument(final String name, final DocumentType documentType, final byte[] document);

	void removeDocument(final String name, final DocumentType documentType);

	String name();

	String description();

	Customer customer();

	Map<String, byte[]> documents();

	

}