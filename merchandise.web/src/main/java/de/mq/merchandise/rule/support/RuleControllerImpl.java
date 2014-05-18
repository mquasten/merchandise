package de.mq.merchandise.rule.support;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.faces.model.SelectItem;

import de.mq.merchandise.customer.Customer;
import de.mq.merchandise.model.support.SimpleMapDataModel;
import de.mq.merchandise.opportunity.support.DocumentModelAO;
import de.mq.merchandise.opportunity.support.RuleInstance;
import de.mq.merchandise.opportunity.support.RuleInstanceImpl;
import de.mq.merchandise.opportunity.support.RuleOperations;
import de.mq.merchandise.rule.ParameterNamesAware;
import de.mq.merchandise.rule.Rule;
import de.mq.merchandise.rule.RuleService;
import de.mq.merchandise.util.EntityUtil;

public class RuleControllerImpl {
	
	static final String RETURN_URL = "rules.xhtml?faces-redirect=true&state=%s";
	private  final RuleService ruleServive;
	
	private final SourceFactoryImpl sourceFactory;
	
	
	
	RuleControllerImpl(final RuleService ruleServive, final SourceFactoryImpl sourceFactory) {
		this.ruleServive = ruleServive;
		this.sourceFactory=sourceFactory;
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
	
	
	
	List<SelectItem>  ruleItems(final RuleModelAO rulesAO) {
		
		final List<SelectItem> result = new ArrayList<>(); 
		for(final RuleAO ruleAO : rulesAO.getRules()){
			result.add(new SelectItem(ruleAO.getId(), ruleAO.getName()));
		}
		
		return result;
		
	}
	

	
	void assignSelected(final Long id, final RuleInstanceAO ruleInstanceAO) {
		
		if( id == null){
			return ;
		}
		
	
		ruleInstanceAO.setPriority(null);
		final RuleInstance ruleInstance = EntityUtil.create(RuleInstanceImpl.class);
		
		final Rule rule = ruleServive.read(id);
		
		EntityUtil.setDependency(ruleInstance, Rule.class, rule);
		ruleInstanceAO.setRuleInstance(ruleInstance);
		
		
	
		final RuleOperations ruleOperations = ruleInstanceAO.getParent();
		
		
		final Set<String> params = new HashSet<>();
		
		if(ruleOperations.hasRule(rule) ){
			params.addAll(ruleOperations.ruleInstance(rule).parameterNames());
			ruleInstance.assign(ruleOperations.ruleInstance(rule).priority());
		}
	
	    for(final String parameter :source(id)){
	    	
	    	if( params.contains(parameter)) {
	    		ruleInstance.assign(parameter,ruleOperations.ruleInstance(rule).parameter(parameter));
	    		continue;
	    	}
	    	ruleInstance.assign(parameter, "");
	    }
	
		
		
	}

	private  String[] source(final Long id) {
		
		return ((ParameterNamesAware<?>)sourceFactory.create(id)).parameters();
		
	}

	
	Long selectedId(final  Rule rule) {
		
	
		if( ! rule.hasId()){
			return null;
		}
		
		return rule.id(); 
	}
	
	
	
	
	List<?> instances(final RuleOperations ruleOperations) {
		return new SimpleMapDataModel<>(ruleOperations.ruleInstances());
		 
		
	}
	
	
	
	RuleInstance selected(final  Rule rule, final RuleOperations ruleOperations) {
		for(final RuleInstance ruleInstance : ruleOperations.ruleInstances()){
			if(ruleInstance.rule().equals(rule)) {
				return ruleInstance ;
			}
		}
		
		return null;
		
	}
	
	
	void addRuleInstance(final RuleInstanceAO ruleInstanceAO) {
		final RuleOperations parent = ruleInstanceAO.getParent();
		final Rule newRule = ruleInstanceAO.getRule().getRule();
		parent.assign(newRule, ruleInstanceAO.getRuleInstance().priority());
		for(final ParameterAO parameter : ruleInstanceAO.getParameter()){
		
			parent.ruleInstance(newRule).assign(parameter.getName(), parameter.getValue());
		}
		
}		

	
	
	void deleteRuleInstance(final RuleInstanceAO ruleInstanceAO)  {
		ruleInstanceAO.getRuleInstance().clearAllParameter();
		ruleInstanceAO.getParent().remove(ruleInstanceAO.getRule().getRule());
		ruleInstanceAO.getRule().setRule(EntityUtil.create(RuleImpl.class));
		ruleInstanceAO.setPriority(null);
	
	}
	
	
	
	
		
}
