package de.mq.merchandise.opportunity.support;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Table;

@Entity(name="OpportunityGeoLocationIndex")
@Table(name="opportunity_gis_index")
public class OpportunityGeoLocationIndexImpl extends AbstractOpportunityIndex {


	
	OpportunityGeoLocationIndexImpl(final Opportunity opportunity) {
		super(new Double( Math.random() * 1e12).longValue(), opportunity);
	}
	
	private OpportunityGeoLocationIndexImpl() {
		super(null,null);
	}
	

	private static final long serialVersionUID = 1L;
	
	@Column(name="geo_coordinates" ,insertable=false, updatable=false  , columnDefinition="GEOGRAPHY(Point)" )
	@Basic(fetch=FetchType.LAZY)
	private String geometry;

}
