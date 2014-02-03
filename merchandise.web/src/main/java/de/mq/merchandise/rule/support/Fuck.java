package de.mq.merchandise.rule.support;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component(value="fuck")
@Scope("request")



public class Fuck {

	
	private RuleAO fuck;

	public Long getFuck() {
	
		System.out.println("get" +  fuck);
		return null;
	}

	public void setFuck(Long fuck) {
		
		System.out.println("set" +  fuck);
		
	} 
	
	
	
}
