package de.mq.merchandise.opportunity.support;


import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import de.mq.merchandise.util.EntityUtil;
import de.mq.merchandise.util.Equals;



@Entity(name="OpportunityIndex")
@Inheritance(strategy=InheritanceType.TABLE_PER_CLASS)
abstract class AbstractOpportunityIndex implements OpportunityIndex{
	
	private static final long serialVersionUID = 1L;

	@ManyToOne(targetEntity = OpportunityImpl.class, fetch = FetchType.LAZY,cascade={CascadeType.PERSIST, CascadeType.MERGE}, optional=false)
	@JoinColumn(name = "opportunity_id")
	@Equals
	private final  Opportunity opportunity;
	@Id
	private final Long id; 
	

	
	protected AbstractOpportunityIndex(final Long id, final Opportunity opportunity) {
		this.opportunity=opportunity;
		this.id=id;
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
	
	@Override
	public long id() {
		EntityUtil.idAware(id);
		return id;
	}


	@Override
	public boolean hasId() {
		return id != null;
	}


	
}
