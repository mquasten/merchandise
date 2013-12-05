package de.mq.merchandise.customer.support;

import de.mq.merchandise.customer.State;
import de.mq.merchandise.customer.StateBuilder;


public  class StateBuilderImpl implements StateBuilder {
	
	
	
	private State state = new StateImpl();
	
	
	@Override
	public StateBuilder forState(boolean active){
		
		if( active){
			state.activate();
		} else {
			state.deActivate();
		}
		
		return this;
	}
	

	@Override
	public State build() {
		return state;
	}
	

	
	
}
