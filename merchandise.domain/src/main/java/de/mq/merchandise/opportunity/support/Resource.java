package de.mq.merchandise.opportunity.support;

import de.mq.merchandise.BasicEntity;

public enum Resource {
	
	Opportunity("opportunities", OpportunityImpl.class),
	
	Subject("subjects", CommercialSubjectImpl.class);
	
	private final String urlPart;
	
	private final Class<? extends BasicEntity> entityClass; 
	
	Resource(final String urlPart, final  Class<? extends BasicEntity> entityClass){
		this.urlPart=urlPart;
		this.entityClass=entityClass;
	}
	
	public final String  urlPart() {
		return urlPart;
	}
	
	public final  Class<? extends BasicEntity> entityClass() {
		return this.entityClass;
	}

}