package de.mq.merchandise.opportunity.support;

import java.util.Collection;

import javax.persistence.EntityManager;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

@Repository
@Profile("db")
class ClassificationRepositoryImpl implements ClassificationRepository {
	
	private final EntityManager entityManager;
	
	ClassificationRepositoryImpl(final EntityManager entityManager) {
		this.entityManager=entityManager;
	}
	
	
	/* (non-Javadoc)
	 * @see de.mq.merchandise.opportunity.support.ClassificationRepository#allActivityClassifications()
	 */
	@Override
	public final Collection<ActivityClassification> allActivityClassifications() {
		return entityManager.createNamedQuery(ClassificationRepository.FIND_ALL_ACTIFITY_CLASSIFICATIONS, ActivityClassification.class).getResultList();
	}
	
	/* (non-Javadoc)
	 * @see de.mq.merchandise.opportunity.support.ClassificationRepository#allProductClassifications()
	 */
	@Override
	public final Collection<ProcuctClassification> allProductClassifications() {
		return entityManager.createNamedQuery(ClassificationRepository.FIND_ALL_PRODUCT_CLASSIFICATIONS, ProcuctClassification.class).getResultList();
	}

}
