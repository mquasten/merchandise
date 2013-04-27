package de.mq.merchandise.reference.support;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

import de.mq.merchandise.reference.Reference;
import de.mq.merchandise.util.EntityUtil;
import de.mq.merchandise.util.Equals;

@Entity(name="Reference")
@IdClass(ReferenceKeyImpl.class)
@NamedQueries( { @NamedQuery(name = ReferenceRepository.QUERY_FOR_TYPE , query = "select r from Reference r where r.referenceType= :referenceType") })
class ReferenceImpl implements Reference {
	
	private static final long serialVersionUID = 1L;

	@Id
	@Column(length=100,nullable=false)
	@Equals
	private final  String key;
	
	@Enumerated(EnumType.STRING)
	@Column(length=20,nullable=false)
	@Id
	@Equals
	private final  Kind referenceType;
	
	
	
	@SuppressWarnings("unused")
	private ReferenceImpl(){
		this.key=null;
		this.referenceType=null;
	}
	
	ReferenceImpl(final String key, final Kind referenceType) {
		this.key=key;
		this.referenceType=referenceType;
	}
	
	@Override
	public   int hashCode() {
		return EntityUtil.equalsBuilder().withSource(this).buildHashCode();
	}

	

	@Override
	public  boolean equals(final Object obj) {
		 return EntityUtil.equalsBuilder().withSource(this).withTarget(obj).forInstance(getClass()).isEquals();
	}
	
	/* (non-Javadoc)
	 * @see de.mq.merchandise.domain.reference.Reference#key()
	 */
	@Override
	public  String key() {
		return this.key;
	}
	
	
	/* (non-Javadoc)
	 * @see de.mq.merchandise.domain.reference.Reference#referenceType()
	 */
	@Override
	public  Kind referenceType() {
		return this.referenceType;
	}
	

}
