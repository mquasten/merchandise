package de.mq.merchandise.opportunity.support;


import de.mq.merchandise.BasicEntity;
import de.mq.merchandise.customer.Customer;

public interface CommercialSubject extends BasicEntity, DocumentsAware {
	
	
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



	String name();

	String description();

	Customer customer();

}