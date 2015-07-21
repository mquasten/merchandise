package de.mq.merchandise.subject.support;

import java.util.Collection;
import java.util.HashSet;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.util.Assert;

import de.mq.merchandise.subject.Subject;

@Entity(name="commercial_subject_item")
@Table(name="commercial_subject_item")
class CommercialSubjectItemImpl implements CommercialSubjectItem {
	
	
	@Id
	private String id;
	
	@NotNull(message="jsr303_mandatory")
	 @Size(min=5, max=30 , message="jsr303_commercial_subject_item_name_size")	
	@Column(nullable=false, length=30)
	private String name ;
	
	@NotNull(message="jsr303_mandatory")
	@ManyToOne(targetEntity = SubjectImpl.class, optional = false, fetch = FetchType.LAZY )
	@JoinColumn(name = "subject_id", referencedColumnName = "id", updatable = false, nullable = false)
	@Valid
	private Subject subject;
	
	
	private boolean mandatory=true;
	
	@NotNull(message="jsr303_mandatory")
	@ManyToOne(targetEntity = CommercialSubjectImpl.class, optional = false, fetch = FetchType.LAZY )
	@JoinColumn(name = "commercial_subject_id", referencedColumnName = "id", updatable = false, nullable = false)
	@Valid
	private CommercialSubjet commercialSubjet; 
	
	
	
	@OneToMany(mappedBy="commercialSubjectItem", targetEntity=CommercialSubjectItemConditionImpl.class, fetch=FetchType.LAZY ,cascade={CascadeType.ALL} , orphanRemoval=true)
	private Collection<CommercialSubjectItemConditionImpl> commercialSubjectItemConditions = new HashSet<>();
	
	public CommercialSubjectItemImpl(final String name, final CommercialSubjet commercialSubjet, final Subject subject) {
		this(name, commercialSubjet,subject, true);
	}
	
	CommercialSubjectItemImpl(final String name, final CommercialSubjet commercialSubjet, final Subject subject, final boolean mandatory) {
		Assert.isTrue(subject.id().isPresent());
		this.id=new UUID(subject.id().get(), System.nanoTime() + (long) (1e12 * Math.random())).toString();
		commercialSubjectItemConditions.addAll(subject.conditions().stream().map(c -> new CommercialSubjectItemConditionImpl(this, c)).collect(Collectors.toSet()));
		this.name = name;
		this.subject = subject;
		this.commercialSubjet = commercialSubjet;
		this.mandatory=mandatory;
	}
	
	/* (non-Javadoc)
	 * @see de.mq.merchandise.subject.support.CommercialSubjectItem#name()
	 */
	@Override
	public final String name() {
		return this.name;
	}
	
	/* (non-Javadoc)
	 * @see de.mq.merchandise.subject.support.CommercialSubjectItem#mandatory()
	 */
	@Override
	public final boolean mandatory() {
		return this.mandatory;
	}

	@Override
	public int hashCode() {
		if( ! valid(this)){
			return super.hashCode();
		}
		return subject.hashCode() + commercialSubjet.hashCode() ;
	}

	@Override
	public boolean equals(Object obj) {
		if( ! valid(this)){
			return false;
		}
		if (!(obj instanceof CommercialSubjectItemImpl)) {
			return false;
			
		}
		final CommercialSubjectItemImpl other = (CommercialSubjectItemImpl) obj; 
		if( ! valid(other)){
			return false;
		}
		return subject.equals(other.subject) && commercialSubjet.equals(other.commercialSubjet);
	}
	
	
	private boolean  valid(CommercialSubjectItemImpl item) {
		if( item.subject == null){
			return false;
		}
		if( item.commercialSubjet == null){
			return false;
		}
		return true;
	}

	

	@Override
	public Subject subject() {
		return this.subject;
	}

}
