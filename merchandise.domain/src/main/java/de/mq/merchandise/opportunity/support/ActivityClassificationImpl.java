package de.mq.merchandise.opportunity.support;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity(name="ActivityClassification")
@DiscriminatorValue("A")
public class ActivityClassificationImpl extends AbstractClassification implements ActivityClassification{
	private static final long serialVersionUID = 1L;

}
