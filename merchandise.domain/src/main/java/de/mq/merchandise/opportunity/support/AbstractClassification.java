package de.mq.merchandise.opportunity.support;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import de.mq.merchandise.util.EntityUtil;
import de.mq.merchandise.util.Equals;

@Entity(name="Classification")
@Table(name="classification")
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="kind")
abstract class AbstractClassification implements Classification{

	private static final long serialVersionUID = 1L;
	
	@Id
	private String id; 
	
	@Column(length=250)
	@Equals
	private String  description;
	
	
	@ManyToOne(targetEntity=AbstractClassification.class,fetch=FetchType.LAZY)
	@JoinColumn(name="parent_id")
	private Classification parent;

	@Override
	public String id() {
		return id;
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
