package de.mq.merchandise.opportunity.support;

import java.io.InputStream;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

public interface DocumentService {

	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	void upload(DocumentsAware documentAware, String name, InputStream inputStream, final String contentType);

	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	void delete(DocumentsAware documentAware, String name);

	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	void assignLink(DocumentsAware documentAware, String name);

}