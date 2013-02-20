package de.mq.merchandise.reference;

import java.util.List;
import java.util.Locale;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Transactional(propagation=Propagation.REQUIRED, readOnly=true)
public interface ReferenceService {

	public abstract List<Locale> languages();

	public abstract List<Locale> countries();

}