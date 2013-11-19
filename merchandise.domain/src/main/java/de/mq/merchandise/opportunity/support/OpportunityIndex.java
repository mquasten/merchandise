package de.mq.merchandise.opportunity.support;

import java.io.Serializable;




/**
 * Assigns some special postgress fields to an opportunity.
 * the fields are for geocoding and full text search and they are only used in bulk opperations.
 * The index is assigned to an opportunity
 * @author mquasten
 *
 */
interface OpportunityIndex extends Serializable {

/**
 * The opportunity related to the index
 * @return the opportunity for the inde
 */
 Opportunity opportunity();
 
 /**
  * The id as a uuid string
  */
 String id();
 

}