package de.mq.merchandise.opportunity.support;

import java.util.Collection;

import de.mq.merchandise.BasicEntity;
import de.mq.merchandise.customer.Customer;

public interface Opportunity extends BasicEntity, DocumentsAware{
	
     enum Kind {
		ProductOrService,
		Offer,
		Tender;
	}

	String name();

	String description();

	Customer customer();

	Collection<Classification> classifications();

	void assignClassification(Classification classification);

	void removeClassification(Classification classification);

	Collection<String> keyWords();

	void assignKeyWord(String keyWord);

	void removeKeyWord(String keyWord);

}