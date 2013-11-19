package de.mq.merchandise.opportunity.support;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import de.mq.merchandise.contact.Address;
import de.mq.merchandise.contact.support.AbstractCityAddress;

@Entity(name="OpportunityGeoLocationIndex")
@Table(name="opportunity_gis_index")
public class OpportunityGeoLocationIndexImpl extends AbstractOpportunityIndex{


	@ManyToOne(targetEntity = AbstractCityAddress.class, fetch = FetchType.LAZY,cascade={CascadeType.PERSIST, CascadeType.MERGE}, optional=false)
    @JoinColumn(name = "address_id")
	private Address address;
	
	OpportunityGeoLocationIndexImpl(final Opportunity opportunity, final Address address) {
		super(opportunity, address.id());
		this.address=address;
	}
	
	@SuppressWarnings("unused")
	private OpportunityGeoLocationIndexImpl() {
		super();
	}
	

	private static final long serialVersionUID = 1L;
	
	@Column(name="geo_coordinates" ,insertable=false, updatable=false  , columnDefinition="GEOGRAPHY(Point)" )
	@Basic(fetch=FetchType.LAZY)
	private String geometry;
	
	final Address address() {
		return address;
	}

}
