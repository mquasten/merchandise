package de.mq.merchandise.opportunity.support;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import de.mq.merchandise.opportunity.support.EntityContext.State;

public class DocumentIndexpostGreSqlRepositoryImpl  implements DocumentIndexRepository {
	
	static final String UPDATE_SQL_TS="update OpportunityFullTextSearchIndex set  searchVector = to_tsvector(coalesce(:name,'') || ' ' || coalesce(:description,'')) where id = :id";
	
	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public Map<Long, String> revisionsforIds(Collection<EntityContext> ids) {
		return new HashMap<>();
	}

	@Override
	public void updateDocuments(final Collection<EntityContext> entityContexts) {
		for(final EntityContext entityContext: entityContexts){
			updateDocument(entityContext);
		}
		
	}

	private void updateDocument(final EntityContext entityContext) {
		final TypedQuery<OpportunityIndex> opportunityQuery = entityManager.createNamedQuery(INDEX_BY_OPPORTUNITY_ID, OpportunityIndex.class);
		opportunityQuery.setParameter("id", entityContext.reourceId());
		for(final OpportunityIndex opportunityIndex : opportunityQuery.getResultList()){
			entityManager.remove(opportunityIndex);
		}
		if(entityContext.isForDeleteRow()){
			entityContext.assign(State.Ok);
			return;
		}
		final Opportunity opportunity= entityManager.find(OpportunityImpl.class, entityContext.reourceId());
		final OpportunityIndex  tsIndex = entityManager.merge(new OpportunityFullTextSearchIndexImpl(opportunity));
		
		final Query updateTsQuery = entityManager.createQuery(UPDATE_SQL_TS);
		updateTsQuery.setParameter("id", tsIndex.id());
		updateTsQuery.setParameter("name", opportunity.name());
		updateTsQuery.setParameter("description", opportunity.description());
		if( updateTsQuery.executeUpdate() !=1 ) {
			throw new IllegalStateException("Row for TSIndex not updated opportunity:" + opportunity.id());
		}
		
		
		entityContext.assign(State.Ok);
		
	}
	
	
	

}
