package de.mq.merchandise.opportunity.support;


import java.util.HashMap;
import java.util.Map;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapKeyColumn;
import javax.persistence.Table;

import de.mq.merchandise.rule.Rule;
import de.mq.merchandise.rule.support.RuleImpl;
import de.mq.merchandise.util.EntityUtil;
import de.mq.merchandise.util.Equals;

@Entity(name="RuleInstance")
@Table(name="rule_instance")
class RuleInstanceImpl implements RuleInstance {
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	private Long id; 
	
	@ManyToOne(targetEntity=ConditionImpl.class, cascade={CascadeType.MERGE, CascadeType.PERSIST} )
	@JoinColumn(name="condition_id" )
	@Equals(mandatory=false)
	private final RuleOperations condition; 
	
	@ManyToOne(targetEntity=CommercialRelationImpl.class, cascade={CascadeType.MERGE, CascadeType.PERSIST} )
	@JoinColumn(name="commercial_relation_id" )
	@Equals(mandatory=false)
	private final CommercialRelation commercialRelation;
	
	
	@ManyToOne(targetEntity=RuleImpl.class, cascade={CascadeType.MERGE, CascadeType.PERSIST} )
	@JoinColumn(name="rule_id" )
    @Equals
	private Rule rule; 
	
	
	@ElementCollection(fetch=FetchType.LAZY)
	@CollectionTable(name="rule_instance_parameters",joinColumns=@JoinColumn(name="rule_instance_id"))
    @MapKeyColumn(name="parameter_name", length=50)
	@Column(name="parameter_value" , length=50 )
	private Map<String,String> parameters=new HashMap<>();
	
	@Column
	@Basic(optional=false)
	private  Integer priority;
	
	@SuppressWarnings("unused")
	private RuleInstanceImpl() {
		this.condition=null;
		this.rule=null;
		this.commercialRelation=null;
		this.priority=0;
	}
	
	RuleInstanceImpl(final Condition condition, final Rule rule, final int priority) {
		this.condition=condition;
		this.rule=rule;
		this.commercialRelation=null;
		this.priority=priority;
		valid(this);
	}
	
	
	RuleInstanceImpl(final CommercialRelation commercialRelation, final Rule rule, final int priority) {
		this.condition=null;
		this.commercialRelation=commercialRelation;
		this.rule=rule;
		this.priority=priority;
		valid(this);
	}
	
	
	/* (non-Javadoc)
	 * @see de.mq.merchandise.opportunity.support.RuleInstance#assign(java.lang.String, java.lang.String)
	 */
	@Override
	public final void assign(final String name, final String value) {
		parameters.put(name, value);
	}
	
	
	/* (non-Javadoc)
	 * @see de.mq.merchandise.opportunity.support.RuleInstance#assign(int)
	 */
	@Override
	public final void assign(final int priority){
		this.priority=priority;
	}
	
	/* (non-Javadoc)
	 * @see de.mq.merchandise.opportunity.support.RuleInstance#parameter(java.lang.String)
	 */
	@Override
	public final String parameter(final String name) {
		if( !parameters.containsKey(name)){
			throw new IllegalArgumentException("Parameter not aware");
		}
		return parameters.get(name);
		
	}
	
	/* (non-Javadoc)
	 * @see de.mq.merchandise.opportunity.support.RuleInstance#rule()
	 */
	@Override
	public Rule rule() {
		return rule;
	}
	
	@Override
	public int priority() {
		EntityUtil.notNullGuard(priority, "priority");
		return priority;
	}
	
	
	@Override
	public int hashCode() {
		if( ! valid(this) ) {
			super.hashCode();
		}
		return  EntityUtil.equalsBuilder().withSource(this).buildHashCode();
	}

	@Override
	public boolean equals(final Object obj) {
		if( ! valid(this)){
			return super.equals(obj);
		}
		if( ! valid(obj)) {
			return super.equals(obj);
		}
	    return EntityUtil.equalsBuilder().withSource(this).withTarget(obj).isEquals();
	}

	@Override
	public boolean forRule(final Rule rule) {
		EntityUtil.notNullGuard(rule, "rule");
		EntityUtil.notNullGuard(this.rule, "rule");
		return this.rule.equals(rule);
	}

	@Override
	public long id() {
		EntityUtil.idAware(id);
		return id;
	}

	@Override
	public boolean hasId() {
		return (id!=null);
	}
	
	
	
	
	boolean valid(final Object ruleinstance ) {
		if (!(ruleinstance instanceof RuleInstanceImpl)) {
			return false;
		}
		
	    return (((RuleInstanceImpl) ruleinstance).commercialRelation()!=null ^ ((RuleInstanceImpl) ruleinstance).condition()!=null) && (((RuleInstanceImpl) ruleinstance).rule()!=null);
	}

	
	RuleOperations condition() {
		return condition;
	}


	CommercialRelation commercialRelation() {
		return commercialRelation;
	}

}
