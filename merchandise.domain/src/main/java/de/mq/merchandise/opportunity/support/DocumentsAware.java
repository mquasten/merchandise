package de.mq.merchandise.opportunity.support;

import java.util.Map;

import de.mq.merchandise.opportunity.support.CommercialSubject.DocumentType;

public interface DocumentsAware {

	Map<String, byte[]> documents();

	void assignDocument(final String name, final DocumentType documentType, final byte[] document);

	void removeDocument(final String name, final DocumentType documentType);

	

}