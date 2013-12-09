package de.mq.merchandise.rule.support;

import de.mq.merchandise.customer.Customer;
import de.mq.merchandise.rule.RuleService;

class RuleControllerImpl {
	
	private  final RuleService ruleServive;
	
	RuleControllerImpl(RuleService ruleServive) {
		this.ruleServive = ruleServive;
	}

	void rules(final RuleModelAO ruleModelAO, final Customer customer) {
	
		ruleModelAO.setRules(ruleServive.rules(customer,ruleModelAO.getPattern() + "%", ruleModelAO.getPaging().getPaging()));
		
		updateSelection(ruleModelAO);
	}
	
	void updateSelection(final RuleModelAO ruleModelAO) {
		if (ruleModelAO.getSelected() == null) {
			return;
		}
		for (final RuleAO ruleAO : ruleModelAO.getRules()) {
			if (ruleAO.getRule().equals(ruleModelAO.getSelected().getRule())) {
				return;
			}
		}

		ruleModelAO.setSelected(null);
	}
	
	
	void initRuleAO(final RuleAO ruleAO, final Long ruleId){
		ruleAO.setRule(ruleServive.read(ruleId));
	}
	
	
}
