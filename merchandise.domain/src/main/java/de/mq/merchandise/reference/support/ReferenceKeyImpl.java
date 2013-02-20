package de.mq.merchandise.reference.support;

import java.io.Serializable;

import de.mq.merchandise.reference.Reference.Kind;
import de.mq.merchandise.util.EntityUtil;
import de.mq.merchandise.util.Equals;

class ReferenceKeyImpl implements Serializable{
	
	private static final long serialVersionUID = -1L;
	
	@Equals
	private final String key;
	
	@Equals
	private final Kind referenceType;

	ReferenceKeyImpl(String key, Kind referenceType) {
		this.key = key;
		this.referenceType = referenceType;
	}
	
	@SuppressWarnings("unused")
	private ReferenceKeyImpl(){
		this.key=null;
		this.referenceType=null;
	}
	
	String key() {
		return this.key;
	}
	
	Kind referenceType(){
		return this.referenceType;
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
