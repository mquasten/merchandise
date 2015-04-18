package de.mq.merchandise.customer.support;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.springframework.util.StringUtils;

import de.mq.merchandise.customer.Customer;
import de.mq.merchandise.support.BasicEntity;
@Table(name="condition_type",uniqueConstraints={@UniqueConstraint(columnNames={"name", "customer_id"})})
@Entity(name="ConditionType")
class ConditionTypeImpl implements BasicEntity {
	
	@Id
	@GeneratedValue
	private Long id;
	
	@Column(length=15, nullable=false,  updatable=false)
	private String name;
	
	@ManyToOne(targetEntity=CustomerImpl.class , optional=false,fetch=FetchType.LAZY)
	@JoinColumn(name="customer_id" ,  referencedColumnName="id",updatable=false, nullable=false)
	private Customer customer; 
	
	@SuppressWarnings("unused")
	private ConditionTypeImpl() {/*touched for the very first time*/}
	
	ConditionTypeImpl(final Customer customer, final String name) {
		this.name=name;
		this.customer=customer;
	}
	
	
	
	final Customer customer() {
		return customer;
	}
	
	
	final String name() {
		return name;
	}

	@Override
	public int hashCode() {
		if( ! valid(this)){
			return super.hashCode();
		}
		return name().hashCode() + customer().hashCode();
	}
	
	private boolean valid(ConditionTypeImpl conditionType){
		if( ! StringUtils.hasText(conditionType.name())){
			return false;
		}
		if( conditionType.customer()== null){
			return false;
		}
		return true;
	}

	@Override
	public boolean equals(final Object obj) {
		if( ! valid(this)){
			return super.equals(obj);
		}
		if (!(obj instanceof ConditionTypeImpl)) {
			return super.equals(obj);
			
		}
		final ConditionTypeImpl other = (ConditionTypeImpl) obj; 
		if( ! valid(other)){
			return super.equals(obj);
		}
		return customer().equals(other.customer()) && name().equals(other.name());
	}
	
	

}
