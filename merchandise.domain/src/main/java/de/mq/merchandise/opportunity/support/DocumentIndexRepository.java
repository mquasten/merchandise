package de.mq.merchandise.opportunity.support;

import java.util.Collection;
import java.util.Map;

public interface DocumentIndexRepository {
	
	public final static String INDEX_BY_OPPORTUNITY_ID = "indexByOpportunityId";

	Map<Long,String> revisionsforIds(final Collection<EntityContext> ids);
	
	void updateDocuments(final Collection<EntityContext> entityContext);

}