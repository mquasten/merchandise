package de.mq.merchandise.opportunity.support;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;

import de.mq.merchandise.contact.Address;
import de.mq.merchandise.opportunity.support.EntityContext.State;
import de.mq.merchandise.util.EntityUtil;

public class OpportunityIndexPostgreSqlRepositoryTest {
	
	private static final String POINT = "Point(longitude,latitude)";

	private static final String INDEX_ID = "ID";

	private static final long ID = 19680528L;

	private EntityManager entityManager = Mockito.mock(EntityManager.class);
	
	private OpportunityIndexRepository documentIndexRepository = new OpportunityIndexpostGreSqlRepositoryImpl(entityManager);
	
	
	@Test
	public final void updateDocuments() {
		
		final Collection<EntityContext> entityContexts = new ArrayList<>();
		final EntityContext updateEntityContext = Mockito.mock(EntityContext.class);
		OpportunityIndexPostgreSqlAO ao = Mockito.mock(OpportunityIndexPostgreSqlAO.class);
		Mockito.when(ao.getTS()).thenReturn("tsIndexVector");
		Mockito.when(updateEntityContext.reference(RevisionAware.class)).thenReturn(ao);
		Mockito.when(updateEntityContext.reourceId()).thenReturn(ID);
		entityContexts.add(updateEntityContext);
		@SuppressWarnings("unchecked")
		TypedQuery<OpportunityIndex> indexQuery = Mockito.mock(TypedQuery.class);
		Mockito.when(entityManager.createNamedQuery(OpportunityIndexpostGreSqlRepositoryImpl.INDEX_BY_OPPORTUNITY_ID, OpportunityIndex.class)).thenReturn(indexQuery);
		final List<OpportunityIndex>  indexes = new ArrayList<>();
		final OpportunityIndex opportunityIndex = Mockito.mock(OpportunityIndex.class);
		Mockito.when(opportunityIndex.id()).thenReturn(INDEX_ID);
		indexes.add(opportunityIndex);
		Mockito.when(indexQuery.getResultList()).thenReturn(indexes);
		final OpportunityImpl opportunity = EntityUtil.create(OpportunityImpl.class);
		ReflectionTestUtils.setField(opportunity, "id", ID);
		
		Mockito.when(entityManager.find(OpportunityImpl.class, ID)).thenReturn( opportunity);
		
		final OpportunityFullTextSearchIndexImpl tsIndex =new OpportunityFullTextSearchIndexImpl(opportunity);
		Mockito.when(entityManager.merge(Mockito.isA(OpportunityFullTextSearchIndexImpl.class))).thenReturn(tsIndex);
		
		final Query updateTsQuery  = Mockito.mock(Query.class);
		Mockito.when(entityManager.createQuery(OpportunityIndexpostGreSqlRepositoryImpl.UPDATE_SQL_TS)).thenReturn(updateTsQuery);
		
		Mockito.when(updateTsQuery.executeUpdate()).thenReturn(1);
		
		final Address address = Mockito.mock(Address.class);
		Mockito.when(address.id()).thenReturn(4711L);
		final Map<Address,String> points = new HashMap<>();
		points.put(address, POINT);
		Mockito.when(ao.getPoints()).thenReturn(points);
		final Query updateGisQuery = Mockito.mock(Query.class);
		Mockito.when(entityManager.createQuery(OpportunityIndexpostGreSqlRepositoryImpl.UPDATE_SQL_GIS)).thenReturn(updateGisQuery);
		
		final OpportunityGeoLocationIndexImpl gisIndex =new OpportunityGeoLocationIndexImpl(opportunity, address);
		Mockito.when(entityManager.merge(Mockito.isA(OpportunityGeoLocationIndexImpl.class))).thenReturn(gisIndex);
		
		Mockito.when(updateGisQuery.executeUpdate()).thenReturn(1);
		
		documentIndexRepository.updateDocuments(entityContexts);
		
		
		Mockito.verify(indexQuery).setParameter("id", ID);
		Mockito.verify(entityManager).remove(opportunityIndex);
		Mockito.verify(entityManager).find(OpportunityImpl.class, ID);
		Mockito.verify(entityManager).merge(Mockito.isA(OpportunityFullTextSearchIndexImpl.class));
		Mockito.verify(entityManager).createQuery(OpportunityIndexpostGreSqlRepositoryImpl.UPDATE_SQL_TS);
		Mockito.verify(updateEntityContext).reference(RevisionAware.class);
		
		Mockito.verify(updateTsQuery).setParameter("id", tsIndex.id());
		Mockito.verify(updateTsQuery).setParameter("ts", ao.getTS());
		Mockito.verify(updateTsQuery).executeUpdate();
		
		
		Mockito.verify(entityManager).merge(Mockito.isA(OpportunityGeoLocationIndexImpl.class));
		Mockito.verify(entityManager).createQuery(OpportunityIndexpostGreSqlRepositoryImpl.UPDATE_SQL_GIS);
		
		Mockito.verify(updateGisQuery).setParameter("id", gisIndex.id());
		Mockito.verify(updateGisQuery).setParameter("point", POINT);
		Mockito.verify(updateGisQuery).executeUpdate();
		
		Mockito.verify(updateEntityContext).assign(State.Ok);
	}

}
