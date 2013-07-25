package de.mq.merchandise.controller;

import java.lang.reflect.Field;

import org.springframework.util.ReflectionUtils;

import de.mq.merchandise.customer.Customer;
import de.mq.merchandise.opportunity.ClassificationService;
import de.mq.merchandise.opportunity.support.ActivityClassification;
import de.mq.merchandise.opportunity.support.ActivityClassificationTreeAO;
import de.mq.merchandise.opportunity.support.CommercialRelation;
import de.mq.merchandise.opportunity.support.CommercialRelationServiceMock;
import de.mq.merchandise.opportunity.support.ConditionTreeAO;
import de.mq.merchandise.opportunity.support.KeyWordModelAO;
import de.mq.merchandise.opportunity.support.Opportunity;
import de.mq.merchandise.opportunity.support.OpportunityAO;
import de.mq.merchandise.opportunity.support.OpportunityModelAO;
import de.mq.merchandise.opportunity.support.OpportunityService;
import de.mq.merchandise.opportunity.support.ProductClassification;
import de.mq.merchandise.opportunity.support.ProductClassificationTreeAO;
import de.mq.merchandise.util.EntityUtil;

class OpportunityControllerImpl {

	private final OpportunityService opportunityService;
	
	private final ClassificationService classificationService;
	
	final CommercialRelationServiceMock commercialRelationServiceMock = new CommercialRelationServiceMock(); 

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
	
	
	void conditions(final OpportunityAO opportunityAO) {
		
		final Opportunity opportunity = opportunityAO.getOpportunity();
		
		try {
			final Field field = ReflectionUtils.findField(opportunity.getClass(), "commercialRelations");
			field.setAccessible(true);
			field.set(opportunity, commercialRelationServiceMock.create(opportunity));
			opportunityAO.notifyConditionsChanged();
		} catch (final IllegalArgumentException | IllegalAccessException e) {
			throw new IllegalArgumentException(e);
		}
		
	}
	
	String create(final ActivityClassificationTreeAO activityClassificationTreeAO, final ProductClassificationTreeAO productClassificationTreeAO) {
		System.out.println("create");
		activityClassificationTreeAO.setClassifications(classificationService.activityClassifications());
		activityClassificationTreeAO.notifyClassificationsChanged();
		
		productClassificationTreeAO.setClassifications(classificationService.productClassCollections());
		productClassificationTreeAO.notifyClassificationsChanged();
		
		
		return "opportunity";
	}

	

	void save(final Opportunity opportunity) {
		System.out.println("save opportunity with activities:" +opportunity.activityClassifications().size());
		System.out.println("save opportunity with product:" +opportunity.productClassifications().size());
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
	
	void addKeyWord(final Opportunity opportunity, final KeyWordModelAO keyWordModel){
		opportunity.assignKeyWord(keyWordModel.getKeyWord());
		keyWordModel.setKeyWord(null);
	}
	
	void deleteKeyWord(final Opportunity opportunity, final KeyWordModelAO keyWordModel) {
		opportunity.removeKeyWord(keyWordModel.getSelectedKeyWord());
		keyWordModel.setSelectedKeyWord(null);
	}

}
