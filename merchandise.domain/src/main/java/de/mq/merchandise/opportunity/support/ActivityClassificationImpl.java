package de.mq.merchandise.opportunity.support;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.NamedQuery;

@Entity(name="ActivityClassification")
@DiscriminatorValue("A")
@NamedQuery(name="findActivityClassification" , query ="Select a from ActivityClassification a order by a.id")
public class ActivityClassificationImpl extends AbstractClassification implements ActivityClassification{
	private static final long serialVersionUID = 1L;
	public ActivityClassificationImpl(String description, ActivityClassification parent) {
		this.id=description;
		this.description=description;
		this.parent=parent;
	}
	
	public ActivityClassificationImpl() {
		
	}
	

}
