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
	
	Kind kind();

	Collection<ActivityClassification> activityClassifications();

	void assignClassification(ActivityClassification classification);

	void removeClassification(ActivityClassification classification);

	Collection<String> keyWords();

	void assignKeyWord(String keyWord);

	void removeKeyWord(String keyWord);

	Collection<ProcuctClassification> productClassifications();

	void assignClassification(ProcuctClassification classification);

	void removeClassification(ProcuctClassification classification);

	
	
	void assignConditions(final CommercialSubject commercialSubject, final Condition ...  conditions);

	
	Collection<CommercialRelation> commercialRelations();
    
 
	

}