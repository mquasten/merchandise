package de.mq.merchandise.opportunity.support;

import java.io.Serializable;
import java.util.List;

import de.mq.merchandise.rule.Rule;

/**
 * The ruleOperations for internal use. A Collection of ruleInstanstances it put 
 * as a parameter to each method call. Strategy for DomainObjects
 * @author mquasten
 *
 */
 interface RuleOperationsInternal extends Serializable{


	 /**
	  * An unmodifyable List for the given Colletion
	  * @param ruleInstances a List of RuleInstances for that the unmodifyable Collection should be created
	  * @return an unmodifyable Collection for the given list
	  */
	  List<RuleInstance> ruleInstances(final List<RuleInstance> ruleInstances);

	  /**
	   * Add the given a ruleInstance  with the given priority and the given rule to the list
	   * @param ruleInstances a list of ruleInstances to that the ruleInstance should be added. If an Instance with the given rule exists, only the priority will be changed
	   * @param rule the rule for which a ruleInstance will be added, or the priority will be changed
	   * @param priority the priority for that the added ruleInstance or the Priority to that will be the ruleInstance modified
	   */
	  void assign(final List<RuleInstance> ruleInstances, final Rule rule, final int priority);

	  /**
	   * Return the ruleInstance for the given rule from the list of ruleInstances
	   * @param ruleInstances a list of RuleInstance in which will the rule be searched
	   * @param rule the rule for that will be searched
	   * @return the ruleInstabce which the given rule from the ruleInsatnces
	   */
	  RuleInstance ruleInstance(final List<RuleInstance> ruleInstances, final Rule rule);

	  /**
	   * Remove the ruleInstance with the given rule from the rulInstances list
	   * @param ruleInstances a list of ruleInstances from which the ruleInsatnce with the given rule should be removed.
	   * @param rule the rule for that the ruleInstabce shold be removed from the ruleInstances list
	   */
	  void remove(final List<RuleInstance> ruleInstances, final Rule rule);

	  /**
	   * Did a ruleInstance of a given rule exist in the ruleInstances list
	   * @param ruleInstances the list of ruleInstances that should be checked, if a ruleInstance for given rule exist.
	   * @param rule the rule for that the ruleInstance is checked in the ruleInstances list
	   * @return true if the ruleInstance exists else false
	   */
	  boolean hasRule(final List<RuleInstance> ruleInstances, final Rule rule);

}