package de.mq.merchandise.opportunity.support;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapKeyColumn;
import javax.persistence.MapKeyEnumerated;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;

import de.mq.merchandise.opportunity.support.Condition.ConditionType;
import de.mq.merchandise.rule.Rule;
import de.mq.merchandise.util.EntityUtil;
import de.mq.merchandise.util.Equals;

@Entity(name="CommercialRelation")
@Cacheable(false)
class CommercialRelationImpl implements CommercialRelation {
	
	
	private static final long serialVersionUID = 1L;

	@GeneratedValue
	@Id
	private Long id;
	
	@ManyToOne(targetEntity=CommercialSubjectImpl.class, cascade={CascadeType.MERGE, CascadeType.PERSIST} )
	@Equals
	@JoinColumn(name="commercial_subject_id" )
	private CommercialSubject commercialSubject;

	@ManyToOne(targetEntity=OpportunityImpl.class,  cascade={CascadeType.MERGE, CascadeType.PERSIST },fetch=FetchType.LAZY )
	@Equals
	@JoinColumn(name="opportunity_id" )
	private Opportunity opportunity;
	
	@Column(length=250)
	private String validate;
	
	@Column(length=250)
	private String calculation;
 
	@OneToMany(targetEntity=ConditionImpl.class  , mappedBy="commercialRelation",  cascade={CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REMOVE} )
	@MapKeyColumn(name="condition_type", length=20)
    @MapKeyEnumerated(EnumType.STRING)
	private Map<Condition.ConditionType, Condition> conditions = new HashMap<>();
	
	@OneToMany(mappedBy="commercialRelation", targetEntity=RuleInstanceImpl.class,  fetch=FetchType.LAZY,  cascade={CascadeType.PERSIST, CascadeType.MERGE,  CascadeType.REMOVE })
	@OrderBy("priority")
	
	private List<RuleInstance> ruleInstances = new ArrayList<>();
	
	
	
	protected CommercialRelationImpl() {
		
	}

    CommercialRelationImpl(final CommercialSubject commercialSubject, final Opportunity opportunity) {
		this.commercialSubject = commercialSubject;
		this.opportunity = opportunity;
	}
	
	
	
	@Override
	public long id() {
		EntityUtil.idAware(id);
		return id;
	}
	
	
	@Override
	public CommercialSubject commercialSubject() {
		return commercialSubject;
	}
	
	
	@Override
	public Opportunity opportunity() {
		return opportunity;
	}
	
	
	@Override
	public Condition condition(final Condition.ConditionType conditionType) {
		if( ! conditions.containsKey(conditionType)){
			throw new IllegalArgumentException("Condition not defined: "+  conditionType.name());
		}
		return conditions.get(conditionType);
	}
	
	
	@Override
	public void assign(final Condition condition) {
		conditions.put(condition.conditionType(), condition);
	}
	
	
	void remove(final ConditionType conditionType){
		conditions.remove(conditionType);
	}

	@Override
	public Map<ConditionType, Condition> conditions() {
		return Collections.unmodifiableMap(conditions);
	}

	@Override
	public boolean hasId() {
		return (id != null);
	}
	
	
	@Override
	public int hashCode() {
		return EntityUtil.equalsBuilder().withSource(this).buildHashCode();
	}

	@Override
	public boolean equals(Object obj) {
		 return EntityUtil.equalsBuilder().withSource(this).withTarget(obj).forInstance(CommercialRelation.class).isEquals();
	}

	@Override
	public List<RuleInstance> ruleInstances() {
		return Collections.unmodifiableList(ruleInstances);
	}

	@Override
	public void assign(Rule rule, int priority) {
		ruleInstances.add(new RuleInstanceImpl(this, rule, priority));
		
	}

	@Override
	public RuleInstance ruleInstance(final Rule rule) {
		for(final RuleInstance ruleInstance : ruleInstances){
			if(! ruleInstance.forRule(rule)){
				
				continue;
			}
			return ruleInstance;
		}
		throw new IllegalArgumentException("Rule " + rule.name() + " isn't assigned to condition");
	}

	@Override
	public void remove(Rule rule) {
		ruleInstances.remove(new RuleInstanceImpl(this, rule, 0));
		
	} 
	
	
}
