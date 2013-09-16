package de.mq.merchandise.opportunity.support;

import de.mq.merchandise.BasicEntity;

public interface DocumentRepository {

	public abstract String revisionFor(final BasicEntity basicEntity);

}