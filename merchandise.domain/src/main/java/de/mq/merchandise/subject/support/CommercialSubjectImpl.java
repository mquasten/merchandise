package de.mq.merchandise.subject.support;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import de.mq.merchandise.customer.Customer;
import de.mq.merchandise.customer.support.CustomerImpl;
import de.mq.merchandise.subject.Condition;
import de.mq.merchandise.subject.Subject;
import de.mq.merchandise.support.Mapper;


@Entity(name="CommercialSubject")
@Table(name="commercial_subject")
class CommercialSubjectImpl implements CommercialSubjet {
	
	@Transient
	private final Mapper<CommercialSubjectItemImpl,CommercialSubjectItemImpl> mapper = new CommercialSubjectItemIntoCommercialSubjectItemMapperImpl();
	
	@GeneratedValue
	@Id
	private  Long id;

	@NotNull(message="jsr303_mandatory")
   @Size(min=5, max=30 , message="jsr303_commercial_subject_name_size")	
	@Column(nullable=false, length=30)
	private String name;
	
	
	@NotNull(message="jsr303_mandatory")
	@Valid
	@ManyToOne(targetEntity=CustomerImpl.class ,optional=false,fetch=FetchType.LAZY)
	@JoinColumn(name="customer_id" ,  referencedColumnName="id",updatable=false, nullable=false)
	private Customer customer;
	
	@OneToMany(mappedBy="commercialSubjet", targetEntity=CommercialSubjectItemImpl.class, fetch=FetchType.LAZY ,cascade={CascadeType.ALL} , orphanRemoval=true)
	private Collection<CommercialSubjectItemImpl> items = new HashSet<>();
	
	
	@SuppressWarnings("unused")
	private CommercialSubjectImpl() {
		
	}
	
	CommercialSubjectImpl(final String name, final Customer customer){
		this.name=name;
		this.customer=customer;
	}
	
	
	/*
	 * (non-Javadoc)
	 * @see de.mq.merchandise.subject.support.CommercialSubjet#assign(de.mq.merchandise.subject.Subject, java.lang.String, boolean)
	 */
	@Override
	public final void assign(final Subject subject, final String name, final boolean mandatory ) {
		Assert.notNull(subject);
		Assert.notNull(name);
		final Optional<CommercialSubjectItemImpl> existingItem = items.stream().filter(item -> item.subject().equals(subject)).findFirst();
		if( existingItem.isPresent()) {
			mapper.mapInto(new CommercialSubjectItemImpl(name, this, subject), existingItem.get());
			return;
		}
		items.add(new CommercialSubjectItemImpl(name, this, subject, mandatory));
	}
	/*
	 * (non-Javadoc)
	 * @see de.mq.merchandise.subject.support.CommercialSubjet#remove(de.mq.merchandise.subject.Subject)
	 */
	@Override
	public final void remove(final Subject subject) {
		Assert.notNull(subject);
		items.stream().filter(item -> item.subject().equals(subject)).findFirst().ifPresent(item -> items.remove(item));
	}
	
	
	
	/*
	 * (non-Javadoc)
	 * @see de.mq.merchandise.subject.support.CommercialSubjet#subjects()
	 */
	@Override
	public final Collection<Subject> subjects() {
		return Collections.unmodifiableSet(items.stream().map(item -> item.subject()).collect(Collectors.toSet()));
	}
	
	/*
	 * (non-Javadoc)
	 * @see de.mq.merchandise.subject.support.CommercialSubjet#conditions(de.mq.merchandise.subject.Subject)
	 */
	@Override
	public final <T> Collection<Entry<Condition, Collection<T>>> conditionValues(final Subject subject) {
		 Assert.notNull(subject);
		 final Optional<CommercialSubjectItemImpl> item = items.stream().filter(s -> s.subject().equals(subject)).findFirst();
		 Assert.isTrue(item.isPresent(), "Subject is not assigned");
		 return item.get().conditionValues();
	}
	/*
	 * (non-Javadoc)
	 * @see de.mq.merchandise.subject.support.CommercialSubjet#assign(de.mq.merchandise.subject.Condition, java.lang.Object)
	 */
	@Override
	public final <T> void assign(final Condition condition, final T value){
		 Assert.notNull(condition);
		 Assert.notNull(condition.conditionType());
		 Assert.notNull(condition.subject());
		 Assert.notNull(value);
		 final Optional<CommercialSubjectItemImpl> item = items.stream().filter(s -> s.subject().equals(condition.subject())).findFirst();
		 Assert.isTrue(item.isPresent(), "Subject is not assigned");
		 
		 item.get().assign(condition.conditionType(), value);
		 
	}
	/*
	 * (non-Javadoc)
	 * @see de.mq.merchandise.subject.support.CommercialSubjet#remove(de.mq.merchandise.subject.Condition, java.lang.Object)
	 */
	@Override
	public final <T> void remove(final Condition condition, final T value){
		 Assert.notNull(condition);
		 Assert.notNull(condition.subject());
		 final Optional<CommercialSubjectItemImpl> item = items.stream().filter(s -> s.subject().equals(condition.subject())).findFirst();
		 Assert.isTrue(item.isPresent(), "Subject is not assigned");
		 item.get().remove(condition.conditionType(), value);
		 
	}
	
	/* (non-Javadoc)
	 * @see de.mq.merchandise.subject.support.CommercialSubjet#customer()
	 */
	@Override
	public final Customer customer() {
		return this.customer;
	}
	
	/* (non-Javadoc)
	 * @see de.mq.merchandise.subject.support.CommercialSubjet#name()
	 */
	@Override
	public final String name() {
		return this.name;
	}
	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		if( ! valid(this) ) {
			return super.hashCode();
		}
		
		return customer.hashCode() + name.hashCode();
	}

	private boolean  valid(CommercialSubjet subject) {
		if( subject.customer() == null){
			return false;
		}
		if (! StringUtils.hasText(subject.name()) ){
			return false;
		}
		return true;
	}
	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(final Object obj) {
		if( ! valid(this)){
			return super.equals(obj);
		}
		if (!(obj instanceof Subject)) {
			return super.equals(obj);
			
		}
		final Subject other = (Subject) obj; 
		
		return  customer.equals(other.customer()) && name.equals(other.name());
	}

	


}
