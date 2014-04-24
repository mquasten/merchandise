package de.mq.merchandise.opportunity.support;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
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
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Transient;

import de.mq.merchandise.rule.Rule;
import de.mq.merchandise.util.EntityUtil;
import de.mq.merchandise.util.Equals;

@Entity(name="Condition")
@Cacheable(false)
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
	
	@Enumerated(EnumType.STRING)
	@Column(name="condition_type")
	@Equals
	private ConditionType conditionType;
	
	@ManyToOne(targetEntity=CommercialRelationImpl.class )
	@JoinColumn(name="commercial_relation_id")
	@Equals
	private CommercialRelation commercialRelation; 
	
	@OneToMany(mappedBy="condition", targetEntity=RuleInstanceImpl.class,  fetch=FetchType.LAZY,  cascade={CascadeType.PERSIST, CascadeType.MERGE,  CascadeType.REMOVE })
	@OrderBy("priority")
	private List<RuleInstance> ruleInstances = new ArrayList<>();
	
	@Transient
	private RuleOperationsInternal ruleOperationsInternal = new AbstractRuleTemplate() {
		
		private static final long serialVersionUID = 1L;

		@Override
		protected final RuleInstance ruleInstance(final Rule rule, final int priority) {
			return new RuleInstanceImpl(ConditionImpl.this, rule, priority);
		}
	};

	protected ConditionImpl() {
		
	}
	
	
	
	ConditionImpl(final ConditionType conditionType,final List<String> values) {
		this(conditionType, values, null, null);
	}
	
	ConditionImpl(final ConditionType conditionType, final List<String> values, final String validation, final String calculation) {
		this.conditionType=conditionType;
		this.validation = validation;
		this.calculation = calculation;
		this.values.addAll(values);
	}
	
	
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
	

	@Override
	public CommercialRelation commercialRelation() {
		return commercialRelation;
	} 
	
	@Override
	public ConditionType conditionType() {
		return conditionType;
		
	}
	
	@Override
	public int hashCode() {
		return EntityUtil.equalsBuilder().withSource(this).buildHashCode();
	}

	@Override
	public boolean equals(Object obj) {
		 return EntityUtil.equalsBuilder().withSource(this).withTarget(obj).forInstance(Condition.class).isEquals();
	}



	@Override
	public long id() {
		EntityUtil.idAware(id);
		return id;
	}
	
	@Override
	public boolean hasId() {
		return (id != null);
	}



	@Override
	public void assignValue(final String value) {
		if( values.contains(value)){
			return;
		}
		values.add(value);
		
	}



	@Override
	public void removeValue(String value) {
		values.remove(value);
	}
	
	@Override
	public List<RuleInstance> ruleInstances() {
		return ruleOperationsInternal.ruleInstances(ruleInstances);
	}
	
	
	@Override
	public  final RuleInstance ruleInstance(final Rule rule) {
		return ruleOperationsInternal.ruleInstance(ruleInstances, rule);
		
	}


	@Override
	public final void assign(final Rule rule, final int priority ){
		ruleOperationsInternal.assign(ruleInstances, rule, priority);
	}



	@Override
	public void remove(final Rule rule) {
		ruleOperationsInternal.remove(ruleInstances, rule);
	}



	@Override
	public boolean hasRule(final Rule rule) {
		return ruleOperationsInternal.hasRule(ruleInstances, rule);
	} 
	
}
