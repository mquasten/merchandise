package de.mq.merchandise.opportunity.support;

import java.io.Serializable;



public interface Classification extends Serializable {
	
	
	String id();
	
	String description();
	
	Classification parent() ;

}
