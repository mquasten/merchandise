package de.mq.merchandise.opportunity.support;

import de.mq.merchandise.BasicEntity;

/**
 * Assigns some special postgress fields to an opportunity.
 * the fields are for geocoding and full text search and they are only used in bulk opperations.
 * The index is assigned to an opportunity
 * @author mquasten
 *
 */
public interface OpportunityIndex extends BasicEntity{

/**
 * The opportunity related to the index
 * @return the opportunity for the inde
 */
 Opportunity opportunity();

}