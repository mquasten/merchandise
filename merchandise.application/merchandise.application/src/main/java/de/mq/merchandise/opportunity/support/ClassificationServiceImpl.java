package de.mq.merchandise.opportunity.support;

import java.util.Collection;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import de.mq.merchandise.opportunity.ClassificationService;
import de.mq.merchandise.opportunity.support.ActivityClassification;
import de.mq.merchandise.opportunity.support.ClassificationRepository;
import de.mq.merchandise.opportunity.support.ProductClassification;
@Service
@Transactional(propagation=Propagation.REQUIRED , readOnly=true)
class ClassificationServiceImpl implements ClassificationService {
	
	private final ClassificationRepository classificationRepository;
	
	@Autowired
	ClassificationServiceImpl(final ClassificationRepository classificationRepository){
		this.classificationRepository=classificationRepository;
	}
	
	
	/* (non-Javadoc)
	 * @see de.mq.merchandise.opportunity.support.ClassificationService#activityClassifications()
	 */
	@Override
	public final Collection<ActivityClassification> activityClassifications() {
		return classificationRepository.allActivityClassifications();
	}
	
	/* (non-Javadoc)
	 * @see de.mq.merchandise.opportunity.support.ClassificationService#productClassCollections()
	 */
	@Override
	public final Collection<ProductClassification> productClassCollections(){
		return classificationRepository.allProductClassifications();
	}

}