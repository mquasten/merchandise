package de.mq.merchandise.customer.support;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import de.mq.merchandise.customer.Nativity;
import de.mq.merchandise.util.EntityUtil;
import de.mq.merchandise.util.Equals;

@Embeddable
class NativityImpl implements Nativity {
	@Equals
	@Column(name="birth_place", length=50)
	private final String birthPlace;
	@Equals
	@Column(name="birth_date")
	@Temporal(TemporalType.DATE)
	private final Date birthDate;
	
	@SuppressWarnings("unused")
	private NativityImpl() {
		this(null,null);
	}
	
	NativityImpl(final String birthPlace, final Date birthDate){
		this.birthPlace=birthPlace;
		this.birthDate=birthDate;
	}
	
	@Override
	public String birthPlace() {
		return this.birthPlace;
	}
	

	@Override
	public Date birthDate(){
		return this.birthDate;
	}

	@Override
	public int hashCode() {
		return EntityUtil.equalsBuilder().withSource(this).buildHashCode();
	}

	@Override
	public boolean equals(Object obj) {
		return EntityUtil.equalsBuilder().withSource(this).withTarget(obj).forInstance(getClass()).isEquals();
	}
	
	

}
