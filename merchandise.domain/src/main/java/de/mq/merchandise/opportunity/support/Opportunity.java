package de.mq.merchandise.opportunity.support;



import java.util.AbstractMap.SimpleEntry;
import java.util.Collection;
import java.util.Map.Entry;

import de.mq.merchandise.BasicEntity;
import de.mq.merchandise.customer.Customer;
import de.mq.merchandise.opportunity.support.Condition.ConditionType;


public interface Opportunity extends BasicEntity, DocumentsAware{
	
     enum Kind {
		ProductOrService,
		Offer,
		Tender;
	}

	String name();

	String description();

	Customer customer();

	Collection<ActivityClassification> activityClassifications();

	void assignClassification(ActivityClassification classification);

	void removeClassification(ActivityClassification classification);

	Collection<String> keyWords();

	void assignKeyWord(String keyWord);

	void removeKeyWord(String keyWord);

	Collection<ProcuctClassification> productClassifications();

	void assignClassification(ProcuctClassification classification);

	void removeClassification(ProcuctClassification classification);

	
	
	void assignConditions(CommercialSubject commercialSubject,Condition ...  conditions);

	

	

}