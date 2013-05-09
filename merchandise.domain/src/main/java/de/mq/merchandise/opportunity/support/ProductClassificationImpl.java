package de.mq.merchandise.opportunity.support;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.NamedQuery;

@Entity(name="ProductClassification")
@DiscriminatorValue("P")
@NamedQuery(name="findProductClassification" , query ="Select p from ProductClassification p order by p.id")
public class ProductClassificationImpl extends AbstractClassification implements ProcuctClassification{
	private static final long serialVersionUID = 1L;
	

}
