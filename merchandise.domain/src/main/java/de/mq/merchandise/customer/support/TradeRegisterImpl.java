package de.mq.merchandise.customer.support;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import de.mq.merchandise.customer.TradeRegister;
import de.mq.merchandise.util.EntityUtil;
import de.mq.merchandise.util.Equals;

@Embeddable
class TradeRegisterImpl implements Serializable, TradeRegister  {
	
	
	private static final long serialVersionUID = 1L;

	@Equals
	@Column(name="zip_code" , length=10)
	private final String zipCode;
	
	@Column(length=50)
	private final String city;
	
	@Equals
	@Column(length=25)
	private final String reference;

	
	@SuppressWarnings("unused")
	private TradeRegisterImpl() {
		this(null, null, null);
	}
	
	TradeRegisterImpl(final String zipCode, final String city, final String reference) {
		this.zipCode = zipCode;
		this.city = city;
		this.reference = reference;
	} 
	
	
	@Override
	public final String zipCode(){
		return this.zipCode;
	}
	
	
	@Override
	public final String city() {
		return this.city;
	}
	
	
	@Override
	public final String reference() {
		return this.reference;
	}
	
	
	
	
	@Override
	public int hashCode() {
		return EntityUtil.equalsBuilder().withSource(this).buildHashCode();
	}
	
	
	@Override
	public boolean equals(final Object obj) {
		return EntityUtil.equalsBuilder().withSource(this).withTarget(obj).forInstance(getClass()).isEquals();
	}
	
	
	
	
	

}
