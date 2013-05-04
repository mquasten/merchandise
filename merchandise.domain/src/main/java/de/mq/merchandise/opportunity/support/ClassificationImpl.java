package de.mq.merchandise.opportunity.support;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import de.mq.merchandise.util.EntityUtil;
import de.mq.merchandise.util.Equals;

@Entity(name="Classification")
@Table(name="classification")
class ClassificationImpl implements Classification{

	private static final long serialVersionUID = 1L;
	
	@Id
	private Long id; 
	
	@Column(length=100)
	@Equals
	private String  description;
	
	@Enumerated(EnumType.STRING)
	private Kind kind;
	
	@ManyToOne(targetEntity=ClassificationImpl.class,fetch=FetchType.LAZY)
	@JoinColumn(name="parent_id")
	private Classification parent;

	@Override
	public long id() {
		return id;
	}

	@Override
	public boolean hasId() {
		return true;
	}
	
	public String description() {
		return description;
	}

	
	public Classification parent() {
		return parent;
	}
	
	@Override
	public int hashCode() {
		return EntityUtil.equalsBuilder().withSource(this).buildHashCode();
	}

	@Override
	public boolean equals(Object obj) {
		 return EntityUtil.equalsBuilder().withSource(this).withTarget(obj).forInstance(Classification.class).isEquals();
	}
	
}
