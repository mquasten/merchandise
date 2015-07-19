package de.mq.merchandise.subject.support;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import de.mq.merchandise.subject.Subject;

@Entity(name="commercial_subject_item")
@Table(name="commercial_subject_item")
class CommercialSubjectItemImpl {
	
	
	@Id
	@GeneratedValue
	private Long id;
	
	@NotNull(message="jsr303_mandatory")
	 @Size(min=5, max=30 , message="jsr303_commercial_subject_item_name_size")	
	@Column(nullable=false, length=30)
	private String name ;
	
	@NotNull(message="jsr303_mandatory")
	@ManyToOne(targetEntity = SubjectImpl.class, optional = false, fetch = FetchType.LAZY )
	@JoinColumn(name = "subject_id", referencedColumnName = "id", updatable = false, nullable = false)
	@Valid
	private Subject subject;
	
	@NotNull(message="jsr303_mandatory")
	@ManyToOne(targetEntity = CommercialSubjectImpl.class, optional = false, fetch = FetchType.LAZY )
	@JoinColumn(name = "commercial_subject_id", referencedColumnName = "id", updatable = false, nullable = false)
	@Valid
	private CommercialSubjet commercialSubjet; ; 
	
	public CommercialSubjectItemImpl(final String name, final CommercialSubjet commercialSubjet, final Subject subject) {
		this.name = name;
		this.subject = subject;
		this.commercialSubjet = commercialSubjet;
	}
	
	public final String name() {
		return this.name;
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

}
