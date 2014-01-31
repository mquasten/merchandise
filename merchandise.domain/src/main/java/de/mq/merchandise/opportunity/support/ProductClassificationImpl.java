package de.mq.merchandise.opportunity.support;

import javax.persistence.Cacheable;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

@Entity(name="ProductClassification")
@Cacheable(false)
@DiscriminatorValue("P")
@NamedQueries({
@NamedQuery(name=ClassificationRepository.FIND_ALL_PRODUCT_CLASSIFICATIONS, query ="Select p from ProductClassification p order by p.id")
})
public class ProductClassificationImpl extends AbstractClassification implements ProductClassification{
	private static final long serialVersionUID = 1L;
}
