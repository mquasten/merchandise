package de.mq.merchandise.opportunity.support;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapKeyColumn;
import javax.persistence.MapKeyEnumerated;
import javax.persistence.OneToMany;

import de.mq.merchandise.opportunity.support.Condition.ConditionType;
import de.mq.merchandise.util.EntityUtil;
import de.mq.merchandise.util.Equals;

@Entity(name="CommercialRelation")
class CommercialRelationImpl implements CommercialRelation {
	
	
	private static final long serialVersionUID = 1L;

	@GeneratedValue
	@Id
	private Long id;
	
	@ManyToOne(targetEntity=CommercialSubjectImpl.class, cascade={CascadeType.MERGE, CascadeType.PERSIST} )
	@Equals
	@JoinColumn(name="commercial_subject_id" )
	private CommercialSubject commercialSubject;

	@ManyToOne(targetEntity=OpportunityImpl.class, cascade={CascadeType.MERGE, CascadeType.PERSIST} )
	@Equals
	@JoinColumn(name="opportunity_id" )
	private Opportunity opportunity;
	
	@Column(length=250)
	private String validate;
	
	@Column(length=250)
	private String calculation;
 
	@OneToMany(targetEntity=ConditionImpl.class  , mappedBy="commercialRelation",  cascade={CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REMOVE, CascadeType.REFRESH} )
	@MapKeyColumn(name="condition_type", length=20)
    @MapKeyEnumerated(EnumType.STRING)
	private Map<Condition.ConditionType, Condition> conditions = new HashMap<>();
	
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
	
	
}
