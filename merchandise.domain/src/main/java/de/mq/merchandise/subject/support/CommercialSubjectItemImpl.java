package de.mq.merchandise.subject.support;

import java.util.AbstractMap;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Optional;
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

import de.mq.merchandise.subject.Condition;
import de.mq.merchandise.subject.Subject;

@Entity(name="commercial_subject_item")
@Table(name="commercial_subject_item")
class CommercialSubjectItemImpl implements CommercialSubjectItem  {
	
	
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
	private CommercialSubject commercialSubjet; 
	
	
	
	@OneToMany(mappedBy="commercialSubjectItem", targetEntity=CommercialSubjectItemConditionImpl.class, fetch=FetchType.LAZY ,cascade={CascadeType.ALL} , orphanRemoval=true)
	private Collection<CommercialSubjectItemConditionImpl> commercialSubjectItemConditions = new HashSet<>();
	
	
	@SuppressWarnings("unused")
	private CommercialSubjectItemImpl() {
		
	}
	
	public CommercialSubjectItemImpl(final String name, final CommercialSubject commercialSubjet, final Subject subject) {
		this(name, commercialSubjet,subject, true);
	}
	
	CommercialSubjectItemImpl(final String name, final CommercialSubject commercialSubjet, final Subject subject, final boolean mandatory) {
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

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		if( ! valid(this)){
			return super.hashCode();
		}
		return subject.hashCode() + commercialSubjet.hashCode() ;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
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

	
	/* (non-Javadoc)
	 * @see de.mq.merchandise.subject.support.CommercialSubjectItem#subject()
	 */
	@Override
	public final Subject subject() {
		return this.subject;
	}
	
	@Override
	/*
	 * (non-Javadoc)
	 * @see de.mq.merchandise.subject.support.CommercialSubjectItem#conditionValues()
	 */
	public final <T>  Collection <Entry<Condition, Collection<T>>>conditionValues() {
		return  Collections.unmodifiableSet(commercialSubjectItemConditions.stream().map(item -> new AbstractMap.SimpleEntry<Condition, Collection<T>>(item.condition(), item.values())).collect(Collectors.toSet()));
	}
	/*
	 * (non-Javadoc)
	 * @see de.mq.merchandise.subject.support.CommercialSubjectItem#assign(java.lang.String, java.lang.Object)
	 */
	@Override
	public final  <T> void  assign(final String conditionType, final T value  ){
		commercialSubjectItemCondition(conditionType, value).get().assign(value);
	}

	private <T> Optional<CommercialSubjectItemConditionImpl> commercialSubjectItemCondition(final String conditionType, final T value) {
		Assert.notNull(conditionType);
		Assert.notNull(value);
		final Optional<CommercialSubjectItemConditionImpl> result = commercialSubjectItemConditions.stream().filter(item -> item.condition().conditionType().equals(conditionType)).findFirst();
	   Assert.isTrue(result.isPresent(), "CommercialSubjectItemCondition not assigned");
		return result;
	}

	/*
	 * (non-Javadoc)
	 * @see de.mq.merchandise.subject.support.CommercialSubjectItem#remove(java.lang.String, java.lang.Object)
	 */
	@Override
	public final <T> void  remove(final String conditionType, final T value  ){
		 commercialSubjectItemCondition(conditionType, value).get().remove(value);
	 }

}
