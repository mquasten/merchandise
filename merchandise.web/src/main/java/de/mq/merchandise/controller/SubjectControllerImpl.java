package de.mq.merchandise.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import de.mq.merchandise.opportunity.CommercialSubjectService;
import de.mq.merchandise.opportunity.support.CommercialSubjectsModelAO;

@Component("subjectController")
public class SubjectControllerImpl {
	
	private final CommercialSubjectService commercialSubjectService;
	
	@Autowired
	public SubjectControllerImpl(final CommercialSubjectService commercialSubjectService){
		this.commercialSubjectService=commercialSubjectService;
	}
	
	public void subjects(final CommercialSubjectsModelAO commercialSubjectsModel) {
		commercialSubjectsModel.setCommercialSubjects(commercialSubjectService.subjects(commercialSubjectsModel.getPattern() + "%" , commercialSubjectsModel.getPaging().getPaging()));
	}

}
