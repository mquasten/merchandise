package de.mq.merchandise.domain.subject.support;



import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import de.mq.merchandise.support.Customer;
import de.mq.merchandise.support.CustomerImpl;

@Entity(name="Subject")
@Table(name="subject")
public class SubjectImpl implements Subject{
	
	@GeneratedValue
	@Id
	protected  Long id;
	
	
	@Column(nullable=false, length=30)
	private String name;
	
	@Column(length=50)
	private String description; 
	@ManyToOne(targetEntity=CustomerImpl.class ,optional=false,fetch=FetchType.LAZY)
	@JoinColumn(name="customer_id" ,  referencedColumnName="id",updatable=false, nullable=false)
	private Customer customer; 
	
	@Temporal(TemporalType.DATE)
	@Column(name="date_created")
	private Date created = new Date(); 
	
	
	@SuppressWarnings("unused")
	private SubjectImpl(){
		
	}
	
	SubjectImpl(final Customer customer, final String name) {
		this(customer, name,null);
	}
	
	SubjectImpl(final Customer customer,final String name, final String description) {
		Assert.hasText(name, "Name is mandatory");
		Assert.notNull(customer, "Customer is mandatory");
		this.customer=customer;
		this.name=name;
		this.description=description;
	}
	
	
	
	
	/* (non-Javadoc)
	 * @see de.mq.merchandise.domain.subject.support.Subject#name()
	 */
	@Override
	public final String name() {
		Assert.hasText(name, "Name is mandatory");
		return name;
	}
	
	/* (non-Javadoc)
	 * @see de.mq.merchandise.domain.subject.support.Subject#description()
	 */
	@Override
	public final String description() {
		return description;
	}
	
	/*
	 * (non-Javadoc)
	 * @see de.mq.merchandise.domain.subject.support.Subject#customer()
	 */
	@Override
	public final Customer customer() {
		Assert.notNull(customer, "Customer is mandatory");
		return customer;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		if( ! valid(this) ) {
			return super.hashCode();
		}
		return customer.hashCode() + name.hashCode();
	}

	private boolean  valid(Subject subject) {
		if( subject.customer() == null){
			return false;
		}
		if (! StringUtils.hasText(subject.name()) ){
			return false;
		}
		return true;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(final Object obj) {
		if( ! valid(this)){
			return super.equals(obj);
		}
		if (!(obj instanceof Subject)) {
			return super.equals(obj);
			
		}
		final Subject other = (Subject) obj; 
		
		return  customer.equals(other.customer()) && name.equals(name);
	}

	
	
	

}
