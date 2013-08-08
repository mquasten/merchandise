package de.mq.merchandise.opportunity.support;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;



@Entity(name="ActivityClassification")
@DiscriminatorValue("A")
@NamedQueries({
@NamedQuery(name=ClassificationRepository.FIND_ALL_ACTIFITY_CLASSIFICATIONS, query="select a from ActivityClassification a")
})
public class ActivityClassificationImpl extends AbstractClassification implements ActivityClassification{
	private static final long serialVersionUID = 1L;
	
	

}
