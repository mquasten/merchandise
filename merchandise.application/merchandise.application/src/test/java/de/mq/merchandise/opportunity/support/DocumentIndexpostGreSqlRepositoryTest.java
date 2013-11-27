package de.mq.merchandise.opportunity.support;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.junit.Test;
import org.mockito.Mockito;

public class DocumentIndexpostGreSqlRepositoryTest {
	
	private static final String INDEX_ID = "ID";

	private static final long ID = 19680528L;

	private EntityManager entityManager = Mockito.mock(EntityManager.class);
	
	private DocumentIndexRepository documentIndexRepository = new DocumentIndexpostGreSqlRepositoryImpl(entityManager);
	
	
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
		Mockito.when(entityManager.createNamedQuery(DocumentIndexpostGreSqlRepositoryImpl.INDEX_BY_OPPORTUNITY_ID, OpportunityIndex.class)).thenReturn(indexQuery);
		final List<OpportunityIndex>  indexes = new ArrayList<>();
		final OpportunityIndex opportunityIndex = Mockito.mock(OpportunityIndex.class);
		Mockito.when(opportunityIndex.id()).thenReturn(INDEX_ID);
		indexes.add(opportunityIndex);
		Mockito.when(indexQuery.getResultList()).thenReturn(indexes);
		final OpportunityImpl opportunity = Mockito.mock(OpportunityImpl.class);
		Mockito.when(entityManager.find(OpportunityImpl.class, ID)).thenReturn(opportunity);
		
		final OpportunityFullTextSearchIndexImpl tsIndex =new OpportunityFullTextSearchIndexImpl(opportunity);
		Mockito.when(entityManager.merge(Mockito.any(OpportunityFullTextSearchIndexImpl.class))).thenReturn(tsIndex);
		
		final Query updateTsQuery  = Mockito.mock(Query.class);
		Mockito.when(entityManager.createQuery(DocumentIndexpostGreSqlRepositoryImpl.UPDATE_SQL_TS)).thenReturn(updateTsQuery);
		
		documentIndexRepository.updateDocuments(entityContexts);
		
		
		Mockito.verify(indexQuery).setParameter("id", ID);
		Mockito.verify(entityManager).remove(opportunityIndex);
		Mockito.verify(entityManager).find(OpportunityImpl.class, ID);
		Mockito.verify(entityManager).merge(Mockito.any(OpportunityFullTextSearchIndexImpl.class));
		Mockito.verify(entityManager).createQuery(DocumentIndexpostGreSqlRepositoryImpl.UPDATE_SQL_TS);
		Mockito.verify(updateEntityContext).reference(RevisionAware.class);
		
		Mockito.verify(updateTsQuery).setParameter("id", tsIndex.id());
		Mockito.verify(updateTsQuery).setParameter("ts", ao.getTS());
		Mockito.verify(updateTsQuery).executeUpdate();
	}

}
