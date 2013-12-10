package de.mq.merchandise.rule.support;

import de.mq.merchandise.customer.Customer;
import de.mq.merchandise.rule.RuleService;

class RuleControllerImpl {
	
	private  final RuleService ruleServive;
	
	RuleControllerImpl(RuleService ruleServive) {
		this.ruleServive = ruleServive;
	}

	void rules(final RuleModelAO ruleModelAO, final Customer customer) {
		System.out.println();
	
		ruleModelAO.setRules(ruleServive.rules(customer,nvl(ruleModelAO.getPattern()) + "%", ruleModelAO.getPaging().getPaging()));
		
		updateSelection(ruleModelAO);
	}

	private String nvl(String  value) {
		if( value==null){
			return "";
		}
		
		return value;
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
	
	
	void initRuleAO(final RuleAO ruleAO, final Long ruleId, final String state){
	   
		ruleAO.setParentState(state);
		if(ruleId==null){
			return;
		}
		ruleAO.setRule(ruleServive.read(ruleId));	
		
	}
	
	String save(final RuleAO ruleAO){
		   
		return "rules.xhtml?faces-redirect=true&state=" + ruleAO.getParentState();
		
	}
	
	
	
	
}
