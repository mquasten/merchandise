package de.mq.merchandise.opportunity.support;

import java.util.Collection;

import de.mq.merchandise.BasicEntity;
import de.mq.merchandise.contact.Address;
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
	
	Kind kind();

	Collection<ActivityClassification> activityClassifications();

	void assignClassification(ActivityClassification classification);

	void removeClassification(ActivityClassification classification);

	Collection<String> keyWords();

	void assignKeyWord(String keyWord);

	void removeKeyWord(String keyWord);

	Collection<ProductClassification> productClassifications();

	void assignClassification(ProductClassification classification);

	void removeClassification(ProductClassification classification);

	
	
	void assignConditions(final CommercialSubject commercialSubject, final Condition ...  conditions);

	
	Collection<CommercialRelation> commercialRelations();
    
	
	void remove(final CommercialSubject commercialSubject);

	void remove(CommercialSubject commercialSubject, ConditionType conditionType);

	Collection<Address> addresses();

	void assign(Address address);

	Condition condition(CommercialSubject subject, ConditionType conditionType);
 
	

}