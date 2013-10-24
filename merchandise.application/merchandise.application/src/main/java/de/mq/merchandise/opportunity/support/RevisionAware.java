package de.mq.merchandise.opportunity.support;


public interface RevisionAware {
	
	void setRevision(final String revision);
	
	void setDeleted(final boolean deleted);

	

}