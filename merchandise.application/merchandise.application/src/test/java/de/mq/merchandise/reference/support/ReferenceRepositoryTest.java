package de.mq.merchandise.reference.support;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import junit.framework.Assert;

import org.junit.Test;
import org.mockito.Mockito;

import de.mq.merchandise.reference.Reference;
import de.mq.merchandise.reference.Reference.Kind;
import de.mq.merchandise.reference.support.ReferenceRepository;
import de.mq.merchandise.reference.support.ReferenceRepositoryImpl;

public class ReferenceRepositoryTest {
	
	
	@Test
	public final void forKind() {
		final EntityManager entityManager = Mockito.mock(EntityManager.class);
		@SuppressWarnings("unchecked")
		final TypedQuery<Reference> typedQuery = Mockito.mock(TypedQuery.class);
		Mockito.when(entityManager.createNamedQuery(ReferenceRepository.QUERY_FOR_TYPE, Reference.class)).thenReturn(typedQuery);
		Mockito.when(typedQuery.setParameter("referenceType", Kind.Language)).thenReturn(typedQuery);
		final List<Reference> results = new ArrayList<>();
		results.add(Mockito.mock(Reference.class));
		Mockito.when(typedQuery.getResultList()).thenReturn(results);
		final ReferenceRepository referenceRepository = new ReferenceRepositoryImpl(entityManager);
		
		Assert.assertEquals(results,referenceRepository.forType(Kind.Language));
	}
	
	@Test
	public final void defaultContructorCoverageOnly(){
		Assert.assertNotNull(new ReferenceRepositoryImpl());
	}

}
