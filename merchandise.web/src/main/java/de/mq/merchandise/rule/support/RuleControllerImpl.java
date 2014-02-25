package de.mq.merchandise.rule.support;

import java.util.ArrayList;
import java.util.List;

import javax.faces.event.ValueChangeEvent;
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
	
	void changeState(final Long ruleId, final Boolean state) {
		
		
		System.out.println(">>>" + ruleId + ":" +state);
	}
	
	List<SelectItem>  ruleItems(final RuleModelAO rulesAO) {
		
		final List<SelectItem> result = new ArrayList<>(); 
		for(final RuleAO ruleAO : rulesAO.getRules()){
			System.out.println(">>>" + ruleAO.getId() + " " + ruleAO.getName());
			result.add(new SelectItem(ruleAO.getId(), ruleAO.getName()));
		}
		
		return result;
		
	}
	
	void change(final ValueChangeEvent  event) {
		System.out.println(event.getSource());
	}
	
	
	void assignSelected(final Long id, final RuleInstanceAO ruleInstanceAO) {
		if( id == null){
			return ;
		}
		
		final RuleInstance ruleInstance = EntityUtil.create(RuleInstanceImpl.class);
		final Rule rule = ruleServive.read(id);
		EntityUtil.setDependency(ruleInstance, Rule.class, rule);
		ruleInstanceAO.setRuleInstance(ruleInstance);
	
	    for(final String parameter :source(id)){
	    	ruleInstance.assign(parameter, "???");
	    	System.out.println("?" + parameter);
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
	
	void assign(final RuleOperations ruleOperations, final RuleInstanceAO ruleInstanceAO){
		System.out.println(ruleOperations);
		System.out.println(ruleInstanceAO);
	
		ruleInstanceAO.setParent(ruleOperations);
		
	}
	
}
