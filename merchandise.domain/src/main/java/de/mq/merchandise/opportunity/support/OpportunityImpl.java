package de.mq.merchandise.opportunity.support;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import de.mq.merchandise.customer.Customer;
import de.mq.merchandise.customer.support.CustomerImpl;
import de.mq.merchandise.util.EntityUtil;
import de.mq.merchandise.util.Equals;

@Entity(name="Opportunity")
public class OpportunityImpl implements Opportunity {
	
	
	private static final long serialVersionUID = 1L;

	@GeneratedValue
	@Id
	private Long id;
	
	@Column(length=50)
	@Equals
	private String name;

	@Column(length=250)
	@Equals
	private String description;
	
	@ManyToOne(targetEntity=CustomerImpl.class, cascade={CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH} )
	@Equals
	@JoinColumn(name="customer_id" )
	private Customer customer;
	
	@OneToMany(mappedBy="commercialSubject", targetEntity=CommercialRelationImpl.class,  fetch=FetchType.LAZY,  cascade={CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH})
	private Set<CommercialRelation> commercialRelations = new HashSet<>(); 
	
	protected OpportunityImpl() {
		
	}
	
	public OpportunityImpl(final Customer customer, final String name, final String description) {
		this.customer = customer;
		this.name = name;
		this.description = description;
	}
	
	public OpportunityImpl(final Customer customer, final String name) {
		this.customer = customer;
		this.name = name;
	}
	
	public long id() {
		EntityUtil.idAware(id);
		return id;
	}
	
	/* (non-Javadoc)
	 * @see de.mq.merchandise.opportunity.support.Opporunity#name()
	 */
	@Override
	public String name() {
		return name;
	}
	
	/* (non-Javadoc)
	 * @see de.mq.merchandise.opportunity.support.Opporunity#description()
	 */
	@Override
	public String description() {
		return name;
	}
	
	
	/* (non-Javadoc)
	 * @see de.mq.merchandise.opportunity.support.Opporunity#customer()
	 */
	@Override
	public final Customer customer() {
		return customer;
	}

	@Override
	public boolean hasId() {
		return (id==null);
	}
	
	
	@Override
	public int hashCode() {
		return  EntityUtil.equalsBuilder().withSource(this).buildHashCode();
	}

	@Override
	public boolean equals(final Object obj) {
	    return EntityUtil.equalsBuilder().withSource(this).withTarget(obj).forInstance(Opportunity.class).isEquals();
	}

}
