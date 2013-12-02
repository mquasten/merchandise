package de.mq.merchandise.opportunity.support;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import junit.framework.Assert;

import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;

import de.mq.merchandise.contact.Address;
import de.mq.merchandise.opportunity.support.EntityContext.State;
import de.mq.merchandise.util.EntityUtil;

public class OpportunityIndexPostgreSqlRepositoryTest {
	
	private static final String POINT = "Point(longitude,latitude)";

	private static final String INDEX_ID_UPDATE = "ID-UPDATE";
	private static final String INDEX_ID_DELETE = "ID-DELETE";

	private static final long ID_UPDATE = 19680528L;
	private static final long ID_DELETE = 19711020L;

	private EntityManager entityManager = Mockito.mock(EntityManager.class);
	
	private OpportunityIndexRepository documentIndexRepository = new OpportunityIndexPostgreSqlRepositoryImpl(entityManager);
	
	
	@Test
	@SuppressWarnings("unchecked")
	public final void updateDocuments() {
		
		final Collection<EntityContext> entityContexts = new ArrayList<>();
		final EntityContext updateEntityContext = Mockito.mock(EntityContext.class);
        final EntityContext deleteEntityContext = Mockito.mock(EntityContext.class);
        Mockito.when(deleteEntityContext.reourceId()).thenReturn(ID_DELETE);
        Mockito.when(deleteEntityContext.isForDeleteRow()).thenReturn(true);
		OpportunityIndexPostgreSqlAO ao = Mockito.mock(OpportunityIndexPostgreSqlAO.class);
		Mockito.when(ao.getTS()).thenReturn("tsIndexVector");
		Mockito.when(updateEntityContext.reference(RevisionAware.class)).thenReturn(ao);
		Mockito.when(updateEntityContext.reourceId()).thenReturn(ID_UPDATE);
		entityContexts.add(updateEntityContext);
		entityContexts.add(deleteEntityContext);
	
		final TypedQuery<OpportunityIndex> indexQueryForUpdateEntityContext = Mockito.mock(TypedQuery.class);
		
		final TypedQuery<OpportunityIndex> indexQueryForDeleteEntityContext = Mockito.mock(TypedQuery.class);
		
		Mockito.when(entityManager.createNamedQuery(OpportunityIndexPostgreSqlRepositoryImpl.INDEX_BY_OPPORTUNITY_ID, OpportunityIndex.class)).thenReturn(indexQueryForUpdateEntityContext, indexQueryForDeleteEntityContext);
		final List<OpportunityIndex>  indexes = new ArrayList<>();
		final OpportunityIndex opportunityIndexForUpdate = Mockito.mock(OpportunityIndex.class);
		Mockito.when(opportunityIndexForUpdate.id()).thenReturn(INDEX_ID_UPDATE);
		
		final OpportunityIndex opportunityIndexForDelete = Mockito.mock(OpportunityIndex.class);
		Mockito.when(opportunityIndexForDelete.id()).thenReturn(INDEX_ID_DELETE);
		
		
		indexes.add(opportunityIndexForUpdate);
		indexes.add(opportunityIndexForDelete);
		Mockito.when(indexQueryForUpdateEntityContext.getResultList()).thenReturn(indexes);
		final OpportunityImpl opportunity = EntityUtil.create(OpportunityImpl.class);
		ReflectionTestUtils.setField(opportunity, "id", ID_UPDATE);
		
		Mockito.when(entityManager.find(OpportunityImpl.class, ID_UPDATE)).thenReturn( opportunity);
		
		final OpportunityFullTextSearchIndexImpl tsIndex =new OpportunityFullTextSearchIndexImpl(opportunity);
		Mockito.when(entityManager.merge(Mockito.isA(OpportunityFullTextSearchIndexImpl.class))).thenReturn(tsIndex);
		
		final Query updateTsQuery  = Mockito.mock(Query.class);
		Mockito.when(entityManager.createQuery(OpportunityIndexPostgreSqlRepositoryImpl.UPDATE_SQL_TS)).thenReturn(updateTsQuery);
		
		Mockito.when(updateTsQuery.executeUpdate()).thenReturn(1);
		
		final Address address = Mockito.mock(Address.class);
		Mockito.when(address.id()).thenReturn(4711L);
		final Map<Address,String> points = new HashMap<>();
		points.put(address, POINT);
		Mockito.when(ao.getPoints()).thenReturn(points);
		final Query updateGisQuery = Mockito.mock(Query.class);
		Mockito.when(entityManager.createQuery(OpportunityIndexPostgreSqlRepositoryImpl.UPDATE_SQL_GIS)).thenReturn(updateGisQuery);
		
		final OpportunityGeoLocationIndexImpl gisIndex =new OpportunityGeoLocationIndexImpl(opportunity, address);
		Mockito.when(entityManager.merge(Mockito.isA(OpportunityGeoLocationIndexImpl.class))).thenReturn(gisIndex);
		
		Mockito.when(updateGisQuery.executeUpdate()).thenReturn(1);
		
		documentIndexRepository.updateDocuments(entityContexts);
		
		
		Mockito.verify(indexQueryForUpdateEntityContext).setParameter("id", ID_UPDATE);
		Mockito.verify(indexQueryForDeleteEntityContext).setParameter("id", ID_DELETE);
		
		Mockito.verify(entityManager).remove(opportunityIndexForUpdate);
		Mockito.verify(entityManager).remove(opportunityIndexForDelete);
		
		
		
		Mockito.verify(entityManager, Mockito.times(1)).find(OpportunityImpl.class, ID_UPDATE);
		Mockito.verify(entityManager,  Mockito.times(1)).merge(Mockito.isA(OpportunityFullTextSearchIndexImpl.class));
		Mockito.verify(entityManager,  Mockito.times(1)).createQuery(OpportunityIndexPostgreSqlRepositoryImpl.UPDATE_SQL_TS);
		Mockito.verify(updateEntityContext,  Mockito.times(1)).reference(RevisionAware.class);
		
		Mockito.verify(updateTsQuery,  Mockito.times(1)).setParameter("id", tsIndex.id());
		Mockito.verify(updateTsQuery,  Mockito.times(1)).setParameter("ts", ao.getTS());
		Mockito.verify(updateTsQuery,  Mockito.times(1)).executeUpdate();
		
		
		Mockito.verify(entityManager, Mockito.times(1)).merge(Mockito.isA(OpportunityGeoLocationIndexImpl.class));
		Mockito.verify(entityManager,  Mockito.times(1)).createQuery(OpportunityIndexPostgreSqlRepositoryImpl.UPDATE_SQL_GIS);
		
		Mockito.verify(updateGisQuery,  Mockito.times(1)).setParameter("id", gisIndex.id());
		Mockito.verify(updateGisQuery,  Mockito.times(1)).setParameter("point", POINT);
		Mockito.verify(updateGisQuery, Mockito.times(1) ).executeUpdate();
		
		Mockito.verify(updateEntityContext, Mockito.times(1)).assign(State.Ok);
		Mockito.verify(deleteEntityContext, Mockito.times(1)).assign(State.Ok);
		
		
	}
	
	
	@Test
	public final void conflict() {
		final OpportunityImpl opportunity = EntityUtil.create(OpportunityImpl.class);
		ReflectionTestUtils.setField(opportunity, "id", ID_UPDATE);
		@SuppressWarnings("unchecked")
		final TypedQuery<OpportunityIndex> indexQuery = Mockito.mock(TypedQuery.class);
		final Collection<EntityContext> entityContexts = new ArrayList<>();
		final EntityContext tsIndexConflictEntityContext = Mockito.mock(EntityContext.class);
		OpportunityIndexPostgreSqlAO ao = Mockito.mock(OpportunityIndexPostgreSqlAO.class);
		Mockito.when(tsIndexConflictEntityContext.reference(RevisionAware.class)).thenReturn(ao);
		Mockito.when(tsIndexConflictEntityContext.reourceId()).thenReturn(ID_UPDATE);
		entityContexts.add(tsIndexConflictEntityContext);
		
		final EntityContext gisIndexConflictEntityContext = Mockito.mock(EntityContext.class);
		Mockito.when(gisIndexConflictEntityContext.reference(RevisionAware.class)).thenReturn(ao);
		Mockito.when(gisIndexConflictEntityContext.reourceId()).thenReturn(ID_UPDATE);
		entityContexts.add(gisIndexConflictEntityContext);
		
		
		Mockito.when(entityManager.createNamedQuery(OpportunityIndexPostgreSqlRepositoryImpl.INDEX_BY_OPPORTUNITY_ID, OpportunityIndex.class)).thenReturn(indexQuery);
		Mockito.when(entityManager.find(OpportunityImpl.class, ID_UPDATE)).thenReturn( opportunity);
		final OpportunityFullTextSearchIndexImpl tsIndex =new OpportunityFullTextSearchIndexImpl(opportunity);
		Mockito.when(entityManager.merge(Mockito.isA(OpportunityFullTextSearchIndexImpl.class))).thenReturn(tsIndex);
	
		final Query updateTsQuery  = Mockito.mock(Query.class);
		Mockito.when(entityManager.createQuery(OpportunityIndexPostgreSqlRepositoryImpl.UPDATE_SQL_TS)).thenReturn(updateTsQuery);
		Mockito.when(updateTsQuery.executeUpdate()).thenReturn(0,1);
		
		final Map<Address,String> points = new HashMap<>();
		final Address address = Mockito.mock(Address.class);
		points.put(address, POINT);
		Mockito.when(ao.getPoints()).thenReturn(points);
		
		final Query updateGisQuery = Mockito.mock(Query.class);
		Mockito.when(entityManager.createQuery(OpportunityIndexPostgreSqlRepositoryImpl.UPDATE_SQL_GIS)).thenReturn(updateGisQuery);
		
		final OpportunityGeoLocationIndexImpl gisIndex =new OpportunityGeoLocationIndexImpl(opportunity, address);
		Mockito.when(entityManager.merge(Mockito.isA(OpportunityGeoLocationIndexImpl.class))).thenReturn(gisIndex);
		
		documentIndexRepository.updateDocuments(entityContexts);
		Mockito.verify(tsIndexConflictEntityContext, Mockito.times(1)).assign(State.Conflict);
		Mockito.verify(gisIndexConflictEntityContext, Mockito.times(1)).assign(State.Conflict);
	}
	
	@Test
	public final void defaultConstructor() {
		Assert.assertNotNull(EntityUtil.create(OpportunityIndexPostgreSqlRepositoryImpl.class));
	}
	
	@Test
	public final void revisions() {
		final Collection<EntityContext> entityContexts = new ArrayList<>();
		final EntityContext entityContext = Mockito.mock(EntityContext.class);
		Mockito.when(entityContext.reourceId()).thenReturn(ID_UPDATE);
		Mockito.when(entityContext.created()).thenReturn(new Date());
		entityContexts.add(entityContext);
		
		final Map<Long,String> results = documentIndexRepository.revisionsforIds(entityContexts);
		Assert.assertEquals(1, results.size());
		Assert.assertEquals((Long) ID_UPDATE, results.keySet().iterator().next());
		Assert.assertEquals(""+entityContext.created().getTime(), results.values().iterator().next());
	}

}
