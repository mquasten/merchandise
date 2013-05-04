package de.mq.merchandise.opportunity.support;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity(name="ProductClassification")
@DiscriminatorValue("P")
public class ProductClassificationImpl extends AbstractClassification implements ProcuctClassification{

	
	private static final long serialVersionUID = 1L;
	

}
