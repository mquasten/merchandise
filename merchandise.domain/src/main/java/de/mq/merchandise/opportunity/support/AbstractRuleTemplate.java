package de.mq.merchandise.opportunity.support;

import java.util.Collections;
import java.util.List;

import de.mq.merchandise.rule.Rule;

abstract class AbstractRuleTemplate implements RuleOperationsInternal {

	
	private static final long serialVersionUID = 1L;


	/* (non-Javadoc)
	 * @see de.mq.merchandise.opportunity.support.RuleOperationsInternal#ruleInstances(java.util.List)
	 */
	@Override
	public List<RuleInstance> ruleInstances(final List<RuleInstance> ruleInstances) {
		return Collections.unmodifiableList(ruleInstances);
	}

	
	/* (non-Javadoc)
	 * @see de.mq.merchandise.opportunity.support.RuleOperationsInternal#assign(java.util.List, de.mq.merchandise.rule.Rule, int)
	 */
	@Override
	public void assign(final List<RuleInstance> ruleInstances, final Rule rule, final int priority) {
		final RuleInstance existing = instance(ruleInstances, rule);
		if( existing != null) {
			existing.assign(priority);
			return;
		}
		ruleInstances.add(ruleInstance(rule, priority));
		
	}

	protected abstract RuleInstance ruleInstance(final Rule rule, final int priority );

	private RuleInstance instance(final List<RuleInstance> ruleInstances, final Rule rule) {
		for(final RuleInstance ruleInstance : ruleInstances){
			if(! ruleInstance.forRule(rule)){
				
				continue;
			}
			return ruleInstance;
		}
		return null;
	}

	
	/* (non-Javadoc)
	 * @see de.mq.merchandise.opportunity.support.RuleOperationsInternal#ruleInstance(java.util.List, de.mq.merchandise.rule.Rule)
	 */
	@Override
	public RuleInstance ruleInstance(final List<RuleInstance> ruleInstances, final Rule rule) {
		return ruleInstanceExistsGuard(rule, instance(ruleInstances, rule));
	}

	private RuleInstance ruleInstanceExistsGuard(final Rule rule, final RuleInstance result) {
		if( result == null){
			throw new IllegalArgumentException("Rule " + rule.name() + " isn't assigned to parent");
		}
		return result; 
	}

	
	/* (non-Javadoc)
	 * @see de.mq.merchandise.opportunity.support.RuleOperationsInternal#remove(java.util.List, de.mq.merchandise.rule.Rule)
	 */
	@Override
	public void remove(final List<RuleInstance> ruleInstances, Rule rule) {
		ruleInstances.remove(ruleInstance(rule, 0));
		
	}

	
	/* (non-Javadoc)
	 * @see de.mq.merchandise.opportunity.support.RuleOperationsInternal#hasRule(java.util.List, de.mq.merchandise.rule.Rule)
	 */
	@Override
	public boolean hasRule(final List<RuleInstance> ruleInstances,Rule rule) {
		for(RuleInstance ruleInstance : ruleInstances) {
			if( ruleInstance.forRule(rule)) {
				return true;
			}
		}
		return false;
	}
	
	 

}