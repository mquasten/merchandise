package de.mq.merchandise.domain.subject.support;

import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity(name="Condition")
@Table(name ="condition")
class ConditionImpl implements Condition {
	

	@GeneratedValue
	@Id
	protected  Long id;
	
	@ManyToOne(targetEntity=SubjectImpl.class ,optional=false,fetch=FetchType.LAZY)
	@JoinColumn(name="subject_id" ,  referencedColumnName="id",updatable=false, nullable=false)
	private Subject subject;
	
	@Column(length=20, nullable=false, name="condition_type")
	private String conditionType;
	
	@Enumerated(EnumType.STRING)
	@Column(length=20, nullable=false)
	ConditionDataType dataType; 
	
	@ElementCollection(fetch=FetchType.LAZY)
	@CollectionTable(joinColumns={@JoinColumn(name="condition_id")},name="condition_value" )
	@Column(name="value", length=50)
	final Collection<String> values= new ArrayList<>(); 
	
	@SuppressWarnings("unused")
	private ConditionImpl() {
		
	};
	
	ConditionImpl(final Subject subject, final String conditionType, final ConditionDataType datatype){
		this.subject=subject;
		this.conditionType=conditionType;
		this.dataType=datatype;
	}

}
