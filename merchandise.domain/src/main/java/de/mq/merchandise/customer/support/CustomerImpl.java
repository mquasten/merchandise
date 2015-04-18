package de.mq.merchandise.customer.support;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import de.mq.merchandise.customer.Customer;
import de.mq.merchandise.subject.Subject;
import de.mq.merchandise.subject.support.SubjectImpl;

@NamedQueries({ @NamedQuery( name=CustomerRepository.CUSTOMER_BY_ID_QUERY, query="Select c from Customer c where c.id =:" + CustomerRepository.ID_PARAMETER)})
@Entity(name="Customer")
@Table(name="customer")
public class CustomerImpl implements Customer{
	

	@GeneratedValue
	@Id
	private Long id;
	
	@Column(length=50)
	private  String name;
	
	@OneToMany(cascade = {CascadeType.DETACH, CascadeType.REFRESH, CascadeType.REMOVE} , fetch = FetchType.LAZY, mappedBy = "customer", targetEntity=SubjectImpl.class)
	@OrderBy("date_created")
	private List<Subject> subjects = new ArrayList<>();
	
	@OneToMany(cascade = {CascadeType.DETACH, CascadeType.REFRESH, CascadeType.REMOVE, CascadeType.PERSIST , CascadeType.MERGE} , fetch = FetchType.LAZY, mappedBy = "customer", targetEntity=ConditionTypeImpl.class)
	@Column(name="condition_type")
	private Set<ConditionTypeImpl> conditionTypes = new HashSet<>();
	
	
	@SuppressWarnings("unused")
	private CustomerImpl() { /* touched for the very first time*/}
	
	public CustomerImpl(final String name){
		Assert.hasText(name, "Name is mandatory");
		this.name=name;
	}
	
	/* (non-Javadoc)
	 * @see de.mq.merchandise.support.Customer#name()
	 */
	@Override
	public final String name() {
		return name;
	}
	/*
	 * (non-Javadoc)
	 * @see de.mq.merchandise.customer.Customer#subjects()
	 */
	@Override
	public final List<Subject> subjects() {
		return Collections.unmodifiableList(subjects);
	}
	/*
	 * (non-Javadoc)
	 * @see de.mq.merchandise.customer.Customer#conditionTypes()
	 */
	@Override
	public final Collection<String> conditionTypes() {
		return Collections.unmodifiableCollection(conditionTypes.stream().map(ct -> ct.name()).collect(Collectors.toSet()));
	}
	
	/*
	 * (non-Javadoc)
	 * @see de.mq.merchandise.customer.Customer#assignConditionType(java.lang.String)
	 */
	@Override
	public final void assignConditionType(final String conditiontype ){
		conditionTypes.add(new ConditionTypeImpl(this, conditiontype));
	}
	/*
	 * (non-Javadoc)
	 * @see de.mq.merchandise.customer.Customer#removeConditionType(java.lang.String)
	 */
	@Override
	public final void removeConditionType(final String conditiontype ){
		conditionTypes.remove(new ConditionTypeImpl(this, conditiontype));
	}
	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		if(! StringUtils.hasText(name)){
			return super.hashCode();
		}
		
		return name.hashCode();
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(final Object obj) {
		if(!StringUtils.hasText(name)){
			return super.equals(obj);
		}
		if (!(obj instanceof Customer)) {
			return super.equals(obj);
		}
		final Customer other = (Customer) obj;
		if(!StringUtils.hasText(other.name())){
			return super.equals(obj);
		}
		return name.equals(other.name());
	}
	
	
	

}
