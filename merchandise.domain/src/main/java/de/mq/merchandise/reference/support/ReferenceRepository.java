package de.mq.merchandise.reference.support;

import java.util.List;

import de.mq.merchandise.reference.Reference;

/**
 * Repository for references
 * @author MQuasten
 *
 */
interface ReferenceRepository {
	
	
	public static final String QUERY_FOR_TYPE = "findReferenceForType";
	
	/**
	 * Find all references for a referenceType
	 * @param referenceType the referenceType that should be seared for
	 * @return the references for the given referenceType
	 */
	List<Reference> forType(final Reference.Kind referenceType);

}
