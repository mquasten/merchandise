package de.mq.merchandise.opportunity.support;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity(name="Condition")
class ConditionImpl implements Condition{

	
	private static final long serialVersionUID = 8021776550000024158L;

	@Id
	@GeneratedValue
	private Long id; 

	@CollectionTable(name="condition_values", joinColumns={@JoinColumn(name="condition_id")})
	@ElementCollection()
	@Column(name="value" , length=50)
	private List<String> values=new ArrayList<>();
	
	@Column(length=250)
	private String validation;
	
	@Column(length=250)
	private String calculation;
	
	@ManyToOne(targetEntity=CommercialRelationImpl.class)
	@JoinColumn(name="commercial_relation_id")
	private CommercialRelation commercialRelation;

	
	
	@Override
	public List<String> values() {
		return Collections.unmodifiableList(values);
	}
	
	@Override
	public String calculation() {
		return calculation;
	}
	
	@Override
	public String validation() {
		return validation;
	}
	
	protected ConditionImpl() {
		
	}
	
	
	
	ConditionImpl(final List<String> values) {
		this(values, null, null);
	}
	
	ConditionImpl(final List<String> values, final String validation, final String calculation) {
		this.validation = validation;
		this.calculation = calculation;
		this.values.addAll(values);
	}
	
	

}