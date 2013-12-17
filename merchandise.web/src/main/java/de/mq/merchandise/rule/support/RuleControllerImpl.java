package de.mq.merchandise.rule.support;

import de.mq.merchandise.customer.Customer;
import de.mq.merchandise.opportunity.support.DocumentModelAO;
import de.mq.merchandise.rule.Rule;
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
	
	
	void initRuleAO(final RuleAO ruleAO, final DocumentModelAO documentModelAO, final Long ruleId, final String state){
	   
		ruleAO.setParentState(state);
		if(ruleId==null){
			return;
		}
		final Rule rule = ruleServive.read(ruleId);
		ruleAO.setRule(rule);	
		documentModelAO.setDocument(rule);
		
	}
	
	String save(final RuleAO ruleAO){
		ruleServive.createOrUpdate(ruleAO.getRule()) ;  
		return "rules.xhtml?faces-redirect=true&state=" + ruleAO.getParentState();
		
	}
	
	void changeState(final Long ruleId, final Boolean state) {
		
		
		System.out.println(">>>" + ruleId + ":" +state);
	}
	
	
}
