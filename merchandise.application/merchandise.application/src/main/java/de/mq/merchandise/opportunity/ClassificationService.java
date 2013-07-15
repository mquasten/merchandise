package de.mq.merchandise.opportunity;

import java.util.Collection;

import de.mq.merchandise.opportunity.support.ActivityClassification;
import de.mq.merchandise.opportunity.support.ProductClassification;

public interface ClassificationService {

	Collection<ActivityClassification> activityClassifications();

	Collection<ProductClassification> productClassCollections();

}