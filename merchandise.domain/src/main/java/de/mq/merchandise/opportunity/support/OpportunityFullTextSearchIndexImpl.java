package de.mq.merchandise.opportunity.support;

import javax.persistence.Basic;
import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Table;

@Entity(name="OpportunityFullTextSearchIndex")
@Cacheable(false)
@Table(name="opportunity_ts_index")
public class OpportunityFullTextSearchIndexImpl extends AbstractOpportunityIndex  {

	OpportunityFullTextSearchIndexImpl(final Opportunity opportunity) {
		super(opportunity, 0L);
	}
	
	@SuppressWarnings("unused")
	private OpportunityFullTextSearchIndexImpl() {
		super();
	}
	

	private static final long serialVersionUID = 1L;
	
	@Column(name="search_vector" , insertable=false, updatable=false  ,  columnDefinition="TSVECTOR" )
	@Basic(fetch=FetchType.LAZY)
	private String searchVector;
	
	
	

	
}
