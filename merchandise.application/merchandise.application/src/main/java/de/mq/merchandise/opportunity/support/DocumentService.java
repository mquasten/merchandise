package de.mq.merchandise.opportunity.support;

import java.io.InputStream;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

public interface DocumentService {

	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public abstract void upload(DocumentsAware documentAware, String name, InputStream inputStream);

	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public abstract void delete(DocumentsAware documentAware, String name);

}