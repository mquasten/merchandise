package de.mq.merchandise.opportunity.support;

import java.util.Collection;

/**
 * Reads the Classifications from a storage
 * @author mquasten
 *
 */
interface ClassificationRepository {
	
	/**
	 * The name for the named Query for jpa 
	 */
	static final String FIND_ALL_ACTIFITY_CLASSIFICATIONS = "findAllActivitiyClassifications";
	
	/**
	 * The name of the named Query for jpa
	 */
	static final String FIND_ALL_PRODUCT_CLASSIFICATIONS = "findAllProductClassifications";

	/**
	 * All activity classifications that are defined in the database.
	 * @return a collection of activity classifications
	 */
	Collection<ActivityClassification> allActivityClassifications();

	/**
	 * All product classifications that are defined in the database.
	 * @return a collection of product classifications
	 */
	Collection<ProductClassification> allProductClassifications();

}