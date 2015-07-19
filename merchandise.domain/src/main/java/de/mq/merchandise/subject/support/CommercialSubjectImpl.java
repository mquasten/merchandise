package de.mq.merchandise.subject.support;

import java.util.Collection;
import java.util.HashSet;

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
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.util.StringUtils;

import de.mq.merchandise.customer.Customer;
import de.mq.merchandise.customer.support.CustomerImpl;
import de.mq.merchandise.subject.Subject;


@Entity(name="CommercialSubject")
@Table(name="commercial_subject")
class CommercialSubjectImpl implements CommercialSubjet {
	
	
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
	
	
	CommercialSubjectImpl(final String name, final Customer customer){
		this.name=name;
		this.customer=customer;
	}
	
	
	
	public final void assign(final Subject subject, final String name) {
		items.add(new CommercialSubjectItemImpl(name, this, subject));
	};
	
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
