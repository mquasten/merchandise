package de.mq.merchandise.opportunity.support;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;


import de.mq.merchandise.opportunity.support.Condition.ConditionType;

public class CommercialRelationServiceMock {
	
	public final Collection<CommercialRelation> create(final Opportunity opportunity) {
		final Collection<CommercialRelation> results = new HashSet<>();
		
		final CommercialSubject subject = new CommercialSubjectImpl(null, "Escortservice", "Nicoles special services");
		final CommercialRelation commercialRelation = new CommercialRelationImpl(subject, opportunity);
		final List<String> values = new ArrayList<>();
		values.add("km");
	
		commercialRelation.assign(new ConditionImpl(ConditionType.Unit, values));
		
		ArrayList<String> val = new ArrayList<String>();
		commercialRelation.assign(new ConditionImpl(ConditionType.PricePerUnit,val));
		val.add("498");
		commercialRelation.assign(new ConditionImpl(ConditionType.Quantity, val));
		
		results.add(commercialRelation);
		return results;
		
	}

}
