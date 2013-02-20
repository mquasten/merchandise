package de.mq.merchandise.reference.support;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import de.mq.merchandise.reference.Reference;
import de.mq.merchandise.reference.ReferenceService;
import de.mq.merchandise.reference.Reference.Kind;
import de.mq.merchandise.reference.support.ReferenceRepository;
@Service
@Transactional(propagation=Propagation.REQUIRED, readOnly=true)
class ReferenceServiceImpl implements ReferenceService {
	
	private final ReferenceRepository referenceRepository;
	
	@Autowired
	public ReferenceServiceImpl(ReferenceRepository referenceRepository){
		this.referenceRepository=referenceRepository;
	}
	
	
	/* (non-Javadoc)
	 * @see de.mq.merchandise.application.reference.support.ReferenceService#languages()
	 */
	@Override
	public final List<Locale> languages() {
		final List<Locale> languages = new ArrayList<>();
		
		for(final Reference reference : referenceRepository.forType(Kind.Language)){
			languages.add(new Locale(reference.key().toLowerCase()));
		}
		return languages;
	}
	
	/* (non-Javadoc)
	 * @see de.mq.merchandise.application.reference.support.ReferenceService#countries()
	 */
	@Override
	public final List<Locale> countries() {
		final List<Locale> countries = new ArrayList<>();
		for(final Reference reference : referenceRepository.forType(Kind.Country)){
			countries.add(new Locale("" ,reference.key().toUpperCase()));
		}
		return countries;
	}

}
