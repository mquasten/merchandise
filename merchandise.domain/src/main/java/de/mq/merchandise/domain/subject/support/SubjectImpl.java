package de.mq.merchandise.domain.subject.support;



import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapKey;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.dao.InvalidDataAccessApiUsageException;
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
	
	 @OneToMany(mappedBy="subject", targetEntity=ConditionImpl.class, fetch=FetchType.LAZY ,cascade={CascadeType.ALL})
	 @MapKey(name="conditionType")
	 private Map<String, Condition<?>> conditions = new HashMap<>();
	
	
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
	
	
	@Override
	public final void add(final String conditionType, final ConditionDataType datatype ){
		Assert.notNull(conditionType , "ConditionType is mandatory");
		Assert.notNull(datatype , "ConditionDataType is mandatory");
		if( conditions.containsKey(conditionType) ) {
			return;
		}
		conditions.put(conditionType, new ConditionImpl<>(this, conditionType, datatype));
		
	}
	
	@Override
	public final <T> Condition<T> condition(final String conditionType) {
		Assert.notNull(conditionType , "ConditionType is mandatory");
		if (! conditions.containsKey(conditionType) ) {
			throw new InvalidDataAccessApiUsageException(String.format("No Condition aware for type: %s", conditionType));
		}
		@SuppressWarnings("unchecked")
		final Condition<T> result =  (Condition<T>) conditions.get(conditionType);
		return result;
	}
	
	@Override
	public final void remove(final String conditionType) {
		Assert.notNull(conditionType , "ConditionType is mandatory");
		conditions.remove(conditionType);
	}
	
	@Override
	public final Collection<Condition<?>> conditions() {
		return Collections.unmodifiableCollection(conditions.values());
	}
	
	@Override
	public final Collection<String> conditionType() {
		return Collections.unmodifiableCollection(conditions.keySet());
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
