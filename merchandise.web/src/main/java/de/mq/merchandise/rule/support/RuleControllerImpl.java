package de.mq.merchandise.rule.support;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;

import de.mq.merchandise.customer.Customer;
import de.mq.merchandise.opportunity.support.DocumentModelAO;
import de.mq.merchandise.rule.Rule;
import de.mq.merchandise.rule.RuleService;

class RuleControllerImpl {
	
	static final String RETURN_URL = "rules.xhtml?faces-redirect=true&state=%s";
	private  final RuleService ruleServive;
	
	RuleControllerImpl(RuleService ruleServive) {
		this.ruleServive = ruleServive;
	}

	void rules(final RuleModelAO ruleModelAO, final Customer customer) {
		
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
		return String.format(RETURN_URL,  ruleAO.getParentState());
		
	}
	
	void changeState(final Long ruleId, final Boolean state) {
		
		
		System.out.println(">>>" + ruleId + ":" +state);
	}
	
	List<SelectItem>  ruleItems(final Collection<RuleAO> ruleAOs) {
		final List<SelectItem> result = new ArrayList<>(); 
		for(final RuleAO ruleAO : ruleAOs){
			result.add( new SelectItem(ruleAO, ruleAO.getName()));
		}
		return result;
		
	}
	
	void change(final ValueChangeEvent  event) {
		System.out.println(event.getSource());
	}
	
	
}
