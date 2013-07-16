package de.mq.merchandise.controller;

import java.util.HashSet;
import java.util.Set;

import de.mq.merchandise.customer.Customer;
import de.mq.merchandise.opportunity.ClassificationService;
import de.mq.merchandise.opportunity.support.ActivityClassification;
import de.mq.merchandise.opportunity.support.ActivityClassificationAO;
import de.mq.merchandise.opportunity.support.ActivityClassificationImpl;
import de.mq.merchandise.opportunity.support.ActivityClassificationTreeAO;
import de.mq.merchandise.opportunity.support.Classification;
import de.mq.merchandise.opportunity.support.ClassificationTreeAO;
import de.mq.merchandise.opportunity.support.Opportunity;
import de.mq.merchandise.opportunity.support.OpportunityAO;
import de.mq.merchandise.opportunity.support.OpportunityModelAO;
import de.mq.merchandise.opportunity.support.OpportunityService;
import de.mq.merchandise.opportunity.support.ProductClassification;
import de.mq.merchandise.opportunity.support.ProductClassificationTreeAO;

class OpportunityControllerImpl {

	private final OpportunityService opportunityService;
	
	private final ClassificationService classificationService;

	OpportunityControllerImpl(final OpportunityService opportunityService, final ClassificationService classificationService) {
		this.opportunityService = opportunityService;
		this.classificationService = classificationService;
	}

	void opportunities(final OpportunityModelAO opportunityModelAO, final Customer customer) {
		opportunityModelAO.setOpportunities(opportunityService.opportunities(customer, opportunityModelAO.getPattern() + "%", opportunityModelAO.getPaging().getPaging()));
		updateSelection(opportunityModelAO);
	}

	void updateSelection(final OpportunityModelAO opportunityModelAO) {
		if (opportunityModelAO.getSelected() == null) {
			return;
		}
		for (final OpportunityAO opportunityAO : opportunityModelAO.getOpportunities()) {
			if (opportunityAO.getOpportunity().equals(opportunityModelAO.getSelected().getOpportunity())) {
				return;
			}
		}

		opportunityModelAO.setSelected(null);
	}
	
	String create(final ActivityClassificationTreeAO activityClassificationTreeAO, final ProductClassificationTreeAO productClassificationTreeAO) {
		System.out.println("create");
	/*	final Set<Classification> results = new HashSet<>();
		for(int i=0; i < 10; i++){
			ActivityClassification parent = new ActivityClassificationImpl("Test " +i , null);
			results.add(parent);
			results.add(new ActivityClassificationImpl("Child " + i +".1", parent));
			results.add(new ActivityClassificationImpl("Child " + i +".2", parent));
		} */
		
		activityClassificationTreeAO.setClassifications(classificationService.activityClassifications());
		activityClassificationTreeAO.notifyClassificationsChanged();
		
		productClassificationTreeAO.setClassifications(classificationService.productClassCollections());
		productClassificationTreeAO.notifyClassificationsChanged();
		return "opportunity";
	}

	

	void save(final Opportunity opportunity) {
		System.out.println(opportunity.activityClassifications().size());
	}

	void onActivityNodeSelect(final ActivityClassification activityClassification, final OpportunityAO opportunityAO) {
		final Opportunity opportunity = opportunityAO.getOpportunity();
		if (opportunity.activityClassifications().contains(activityClassification)) {
			opportunity.removeClassification(activityClassification);
		} else {
			opportunity.assignClassification(activityClassification);
		}

		opportunityAO.notifyActivityClassificationChanged();
	}
	
	void onProductNodeSelect(final ProductClassification productClassification, final OpportunityAO opportunityAO) {
		final Opportunity opportunity = opportunityAO.getOpportunity();
		if (opportunity.productClassifications().contains(productClassification)) {
			opportunity.removeClassification(productClassification);
		} else {
			opportunity.assignClassification(productClassification);
		}

		opportunityAO.notifyProductClassificationChanged();
	}

}
