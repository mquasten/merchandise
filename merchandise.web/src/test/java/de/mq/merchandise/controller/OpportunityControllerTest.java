package de.mq.merchandise.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import de.mq.merchandise.customer.Customer;
import de.mq.merchandise.opportunity.ClassificationService;
import de.mq.merchandise.opportunity.support.ActivityClassification;
import de.mq.merchandise.opportunity.support.ActivityClassificationTreeAO;
import de.mq.merchandise.opportunity.support.Opportunity;
import de.mq.merchandise.opportunity.support.OpportunityAO;
import de.mq.merchandise.opportunity.support.OpportunityModelAO;
import de.mq.merchandise.opportunity.support.OpportunityService;
import de.mq.merchandise.opportunity.support.PagingAO;
import de.mq.merchandise.opportunity.support.ProductClassification;
import de.mq.merchandise.opportunity.support.ProductClassificationTreeAO;
import de.mq.merchandise.util.Paging;

public class OpportunityControllerTest {
	
	private static final String PATTERN = "Kylie";
	private final OpportunityService opportunityService = Mockito.mock(OpportunityService.class);
	private final ClassificationService classificationService = Mockito.mock(ClassificationService.class);
	
	private final OpportunityAO opportunityAO = Mockito.mock(OpportunityAO.class);
	private final Customer customer = Mockito.mock(Customer.class);
	private final OpportunityModelAO opportunityModelAO = Mockito.mock(OpportunityModelAO.class);
	private final PagingAO  pagingAO = Mockito.mock(PagingAO.class);
	private final Paging paging = Mockito.mock(Paging.class);
	
	private final  Collection<Opportunity>  opportunities = new ArrayList<>(); 
	
	private Opportunity opportunity = Mockito.mock(Opportunity.class);
	private final List<OpportunityAO> opportunityAOs = new ArrayList<>();
	
	private final List<ActivityClassification> activities = new ArrayList<>();
	
	private final ActivityClassification activityClassification = Mockito.mock(ActivityClassification.class);
	
	private final List<ProductClassification> products = new ArrayList<>();
	
	
	private final ProductClassification productClassification = Mockito.mock(ProductClassification.class);
	

	
	@Before
	public final void setup() {
		opportunities.clear();
		opportunityAOs.clear();
		opportunities.add(opportunity);
		
		Mockito.when(pagingAO.getPaging()).thenReturn(paging);
		
		Mockito.when(opportunityModelAO.getPattern()).thenReturn(PATTERN);
		Mockito.when(opportunityModelAO.getPaging()).thenReturn(pagingAO);
		
		Mockito.when(opportunityService.opportunities(customer, PATTERN + "%", paging)).thenReturn(opportunities);
		Mockito.when(opportunityAO.getOpportunity()).thenReturn(opportunity);
		Mockito.when(opportunityModelAO.getOpportunities()).thenReturn(opportunityAOs);
		opportunityAOs.add(opportunityAO);
		
		activities.clear();
		activities.add(activityClassification);
		
		Mockito.when(classificationService.activityClassifications()).thenReturn(activities);
		
		products.clear();
		products.add(productClassification);
		Mockito.when(classificationService.productClassCollections()).thenReturn(products);
	}
	
	private final OpportunityControllerImpl opportunityControllerImpl = new OpportunityControllerImpl(opportunityService, classificationService);
	
	@Test
	public final void opportunities() {
		
		Mockito.when(opportunityModelAO.getSelected()).thenReturn(opportunityAO);
		opportunityControllerImpl.opportunities(opportunityModelAO, customer);
		Mockito.verify(opportunityModelAO).setOpportunities(opportunities);
		Mockito.verify(opportunityModelAO, Mockito.times(0)).setSelected(null);
	
		
	}
	
	@Test
	public final void opportunitiesSelectedNotInResult() {
		OpportunityAO mock = Mockito.mock(OpportunityAO.class);
		Mockito.when(opportunityModelAO.getSelected()).thenReturn(mock);
		
		opportunityControllerImpl.opportunities(opportunityModelAO, customer);
		
		Mockito.verify(opportunityModelAO).setOpportunities(opportunities);
		
		Mockito.verify(opportunityModelAO, Mockito.times(1)).setSelected(null);
		
	}
	
	@Test
	public final void nothingSelected() {
		opportunityControllerImpl.opportunities(opportunityModelAO, customer);
		Mockito.verify(opportunityModelAO).setOpportunities(opportunities);
		Mockito.verify(opportunityModelAO, Mockito.times(0)).getOpportunities();
		Mockito.verify(opportunityModelAO, Mockito.times(0)).setSelected(null);
		
	}
	
	@Test
	public final  void create() {
		ActivityClassificationTreeAO activityClassificationTreeAO = Mockito.mock(ActivityClassificationTreeAO.class);
		ProductClassificationTreeAO productClassificationTreeAO = Mockito.mock(ProductClassificationTreeAO.class);
		Assert.assertEquals("opportunity", opportunityControllerImpl.create(activityClassificationTreeAO, productClassificationTreeAO));
		
		Mockito.verify(activityClassificationTreeAO).setClassifications(activities);
		Mockito.verify(productClassificationTreeAO).setClassifications(products);
		
		Mockito.verify(activityClassificationTreeAO).notifyClassificationsChanged();
		Mockito.verify(productClassificationTreeAO).notifyClassificationsChanged();
		
	}
	
	@Test
	public final void onActivityNodeSelectRemove() {
		Mockito.when(opportunity.activityClassifications()).thenReturn(activities);
		opportunityControllerImpl.onActivityNodeSelect(activityClassification, opportunityAO);
		Mockito.verify(opportunity).removeClassification(activityClassification);
		Mockito.verify(opportunityAO).notifyActivityClassificationChanged();
		
	}
	
	@Test
	public final void onActivityNodeSelectAdd() {
		Mockito.when(opportunity.activityClassifications()).thenReturn(new ArrayList<ActivityClassification>());
		opportunityControllerImpl.onActivityNodeSelect(activityClassification, opportunityAO);
		Mockito.verify(opportunity).assignClassification(activityClassification);
		Mockito.verify(opportunityAO).notifyActivityClassificationChanged();
	}
	
    @Test
	public final void onProductNodeSelectRemove() {
		Mockito.when(opportunity.productClassifications()).thenReturn(products);
		opportunityControllerImpl.onProductNodeSelect(productClassification, opportunityAO);
		Mockito.verify(opportunity).removeClassification(productClassification);
		Mockito.verify(opportunityAO).notifyProductClassificationChanged();
		
	}
    
    @Test
	public final void onProductNodeSelectAdd() {
		Mockito.when(opportunity.productClassifications()).thenReturn(new ArrayList<ProductClassification>());
		opportunityControllerImpl.onProductNodeSelect(productClassification, opportunityAO);
		Mockito.verify(opportunity).assignClassification(productClassification);
		Mockito.verify(opportunityAO).notifyProductClassificationChanged();
	}
	
	
	

}
