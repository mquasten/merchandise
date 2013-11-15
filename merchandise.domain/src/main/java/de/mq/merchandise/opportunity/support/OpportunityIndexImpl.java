package de.mq.merchandise.opportunity.support;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import de.mq.merchandise.util.EntityUtil;
import de.mq.merchandise.util.Equals;



@Entity(name="OpportunityIndex")
@Table(name="opportunity_index")
class OpportunityIndexImpl implements OpportunityIndex{
	
	private static final long serialVersionUID = 1L;

	@ManyToOne(targetEntity = OpportunityImpl.class, fetch = FetchType.LAZY,cascade={CascadeType.PERSIST, CascadeType.MERGE}, optional=false)
	@JoinColumn(name = "opportunity_id")
	@Equals
	private final  Opportunity opportunity;
	@Id
	private final Long id; 
	

	
	@Column(name="search_vector" , insertable=false, updatable=false,  columnDefinition="TSVECTOR")
	@Basic(fetch=FetchType.LAZY)
	private String searchVector;
	
	@Column(name="geo_coordinates" ,insertable=false, updatable=false  , columnDefinition="GEOGRAPHY(Point)")
	@Basic(fetch=FetchType.LAZY)
	private String geometry;
	
	@SuppressWarnings("unused")
	private OpportunityIndexImpl() {
		opportunity=null;
		id=null;
	}
	
	OpportunityIndexImpl(final  Opportunity opportunity){
		this.opportunity=opportunity;
		this.id=opportunity.id();
	}

	
	@Override
	public long id() {
	
		return opportunity.id();
	}

	@Override
	public boolean hasId() {
		return id!=null;
	}

	
	@Override
	public final Opportunity opportunity() {
		return opportunity;
		
	}
	
	@Override
	public int hashCode() {
		return EntityUtil.equalsBuilder().withSource(this).buildHashCode();
	}

	@Override
	public boolean equals(Object obj) {
		 return EntityUtil.equalsBuilder().withSource(this).withTarget(obj).forInstance(OpportunityIndex.class).isEquals();
	}

	
}
