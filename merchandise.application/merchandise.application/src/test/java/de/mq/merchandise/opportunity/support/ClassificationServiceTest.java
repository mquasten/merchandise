package de.mq.merchandise.opportunity.support;

import java.util.ArrayList;
import java.util.Collection;

import junit.framework.Assert;

import org.junit.Test;
import org.mockito.Mockito;

import de.mq.merchandise.opportunity.ClassificationService;

public class ClassificationServiceTest {
	
	private ClassificationRepository classificationRepository = Mockito.mock(ClassificationRepository.class);
	
	private ClassificationService classificationService = new ClassificationServiceImpl(classificationRepository);
	
	@Test
	public final void activitityClassifications() {
		final Collection<ActivityClassification> tResult = new ArrayList<>();
		tResult.add(Mockito.mock(ActivityClassification.class));
		Mockito.when(classificationRepository.allActivityClassifications()).thenReturn(tResult);
		Assert.assertEquals(tResult, classificationService.activityClassifications());
	}
	
	@Test
	public final void productClassifications() {
		final Collection<ProductClassification> tResult = new ArrayList<>();
		tResult.add(Mockito.mock(ProductClassification.class));
		Mockito.when(classificationRepository.allProductClassifications()).thenReturn(tResult);
		Assert.assertEquals(tResult, classificationService.productClassCollections());
		
	}

}
