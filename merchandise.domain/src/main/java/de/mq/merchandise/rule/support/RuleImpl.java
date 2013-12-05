package de.mq.merchandise.rule.support;



import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.Target;

import de.mq.merchandise.customer.Customer;
import de.mq.merchandise.customer.State;
import de.mq.merchandise.customer.support.CustomerImpl;
import de.mq.merchandise.customer.support.StateBuilderImpl;
import de.mq.merchandise.customer.support.StateImpl;
import de.mq.merchandise.rule.Rule;
import de.mq.merchandise.util.EntityUtil;
import de.mq.merchandise.util.Equals;

@Entity(name="Rule")
@NamedQuery(name=RuleRepository.RULE_FOR_NAME_PATTERN, query="select r from Rule r where r.name like :name and r.customer.id = :customerId")
@Table(name="rule" ,uniqueConstraints={@UniqueConstraint(columnNames={"customer_id", "name"})})
class RuleImpl implements Rule  {
	
	private static final long serialVersionUID = 1L;

	@Id
	@Column(length=50)
	@GeneratedValue
	private Long id;
	
	@Column(length=50)
	@Basic(optional=false)
	@Equals
	private String name;
	
	
	
	@ManyToOne(targetEntity=CustomerImpl.class)
	@JoinColumn(name="customer_id", nullable=false, updatable=false,insertable=true)
	@Equals
	private Customer customer;
	
	@Embedded
	@Basic(optional=false)
	@Target(StateImpl.class)
	private State state= new StateBuilderImpl().build();
	
	
	private RuleImpl() {
		super();
	}
	
	public RuleImpl(final Customer customer, final String name) {
		this();
		this.name=name;
		this.customer=customer;
	}
	
	
	@Override
	public final long id() {
		EntityUtil.idAware(id);
		return id;
	}
	
	
	@Override
	public final String name() {
		return name;
	}

	@Override
	public final  Customer customer() {
		return this.customer;
	}

	@Override
	public boolean hasId() {
		return id != null;
	}


	@Override
	public State state() {
		return state;
	}
	
	@Override
	public int hashCode() {
		return  EntityUtil.equalsBuilder().withSource(this).buildHashCode();
	}

	@Override
	public boolean equals(final Object obj) {
	    return EntityUtil.equalsBuilder().withSource(this).withTarget(obj).forInstance(Rule.class).isEquals();
	}

	@Override
	public String toString() {
		return "name="+name;
	}
	
	
	

}
