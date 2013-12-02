package de.mq.merchandise.opportunity.support;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import de.mq.merchandise.contact.Address;
import de.mq.merchandise.opportunity.support.EntityContext.State;

public class OpportunityIndexPostgreSqlRepositoryImpl implements OpportunityIndexRepository {
	
	static final String UPDATE_SQL_TS="update OpportunityFullTextSearchIndex set  searchVector = to_tsvector(:ts) where id = :id";
	
	static final String UPDATE_SQL_GIS= "update OpportunityGeoLocationIndex set  geometry = ST_GeometryFromText(:point) where id = :id";
	
	@PersistenceContext
	private EntityManager entityManager;
	
	OpportunityIndexPostgreSqlRepositoryImpl(final EntityManager entityManager){
		this.entityManager=entityManager;
	}
	public OpportunityIndexPostgreSqlRepositoryImpl(){
		super();
	}

	@Override
	public Map<Long, String> revisionsforIds(final Collection<EntityContext> ids) {
		final Map<Long,String> revisions =  new HashMap<>();
		for(EntityContext entityContext : ids){
			revisions.put(entityContext.reourceId(), "" + entityContext.created().getTime());
		}
		return revisions;
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
		final OpportunityIndexPostgreSqlAO ao = (OpportunityIndexPostgreSqlAO) entityContext.reference(RevisionAware.class);
		final Query updateTsQuery = entityManager.createQuery(UPDATE_SQL_TS);
		updateTsQuery.setParameter("id", tsIndex.id());
		
		updateTsQuery.setParameter("ts", ao.getTS());
		
		if( updateTsQuery.executeUpdate() !=1 ) {
			entityContext.assign(State.Conflict);
			return;
		}
		
		for(final Entry<Address, String> entry : ao.getPoints().entrySet()) {
			final OpportunityIndex  gisIndex = entityManager.merge(new OpportunityGeoLocationIndexImpl(opportunity, entry.getKey() ));
			final Query updateGISQuery = entityManager.createQuery(UPDATE_SQL_GIS);
			updateGISQuery.setParameter("id", gisIndex.id());
			updateGISQuery.setParameter("point", entry.getValue());
			if(updateGISQuery.executeUpdate() != 1){
				entityContext.assign(State.Conflict);
				return;
			}
			
		} 
		entityContext.assign(State.Ok); 
		
	}
	
	
	

}
