package de.mq.merchandise.opportunity.support;

import java.util.ArrayList;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import junit.framework.Assert;

import org.junit.Test;
import org.mockito.Mockito;

public class ClassificationRepositoryTest {
	
	
	
	
	private EntityManager entityManager = Mockito.mock(EntityManager.class);
	
	private ClassificationRepository classificationRepository = new ClassificationRepositoryImpl(entityManager);
	
	
	
	@Test
	public final void activityClassifications() {
		@SuppressWarnings("unchecked")
		final TypedQuery<ActivityClassification> typedQuery = Mockito.mock(TypedQuery.class);
		final List<ActivityClassification> activityClassifications = new ArrayList<>();
		activityClassifications.add(Mockito.mock(ActivityClassification.class));
		Mockito.when(entityManager.createNamedQuery(ClassificationRepository.FIND_ALL_ACTIFITY_CLASSIFICATIONS, ActivityClassification.class)).thenReturn(typedQuery);
		Mockito.when(typedQuery.getResultList()).thenReturn(activityClassifications);
		Assert.assertEquals(activityClassifications, classificationRepository.allActivityClassifications());
	}
	
	@Test
	public final void productClassifications(){
		@SuppressWarnings("unchecked")
		final TypedQuery<ProductClassification> typedQuery = Mockito.mock(TypedQuery.class);
		final List<ProductClassification> productClassifications = new ArrayList<>();
		productClassifications.add(Mockito.mock(ProductClassification.class));
		
		Mockito.when(entityManager.createNamedQuery(ClassificationRepository.FIND_ALL_PRODUCT_CLASSIFICATIONS, ProductClassification.class)).thenReturn(typedQuery);
		Mockito.when(typedQuery.getResultList()).thenReturn(productClassifications);
		
		Assert.assertEquals(productClassifications, classificationRepository.allProductClassifications());
		
	}

}
