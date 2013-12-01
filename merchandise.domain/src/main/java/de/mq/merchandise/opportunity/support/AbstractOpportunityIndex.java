package de.mq.merchandise.opportunity.support;


import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

import de.mq.merchandise.util.EntityUtil;
import de.mq.merchandise.util.Equals;



@Entity(name="OpportunityIndex")
@Inheritance(strategy=InheritanceType.TABLE_PER_CLASS)
@NamedQueries(@NamedQuery(name = OpportunityIndexRepository.INDEX_BY_OPPORTUNITY_ID, query = "Select i from OpportunityIndex i where i.opportunity.id=:id"))
abstract class AbstractOpportunityIndex implements OpportunityIndex {
	
	private static final long serialVersionUID = 1L;

	@ManyToOne(targetEntity = OpportunityImpl.class, fetch = FetchType.LAZY,cascade={CascadeType.PERSIST, CascadeType.MERGE}, optional=false)
	@JoinColumn(name = "opportunity_id")
	private final  Opportunity opportunity;
	@Id
	@Column(length=50)
	@Equals
	private final String id; 
	

	
	AbstractOpportunityIndex(final Opportunity opportunity, final long leastSignificatantId) {
		this.id=new UUID(opportunity.id(), leastSignificatantId).toString();
		this.opportunity=opportunity;
	}
	
	protected AbstractOpportunityIndex() {
		id=null;
		opportunity=null;
	}
	
	
	
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
	
	
	public String id() {
		return id;
	}


	

	
}
