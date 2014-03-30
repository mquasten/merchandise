package de.mq.merchandise.rule.support;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.faces.model.SelectItem;

import de.mq.merchandise.customer.Customer;
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
	
	RuleControllerImpl(RuleService ruleServive, SourceFactoryImpl sourceFactory) {
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
		// :TODO ugly add hasRule ... 
		try {
			params.addAll(ruleOperations.ruleInstance(rule).parameterNames());
			ruleInstance.assign(ruleOperations.ruleInstance(rule).priority());
		} catch ( Exception ex) {
			
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

	
	Long selectedId(final  RuleAO rule) {
		
	
		if( rule == null){
			return null;
		}
		
		return rule.getIdAsLong(); 
	}
	
	
	
	
	List<?> instances(final RuleOperations ruleOperations) {
		if( ruleOperations==null){
			return null;
		}
		return ruleOperations.ruleInstances();
		
		
	}
	
	
	
	
		
}
