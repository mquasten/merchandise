package de.mq.merchandise.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


import de.mq.merchandise.opportunity.CommercialSubjectService;
import de.mq.merchandise.opportunity.support.CommercialSubjectsModelAO;
import de.mq.merchandise.util.Paging;

@Component("subjectController")
public class SubjectControllerImpl {
	
	private final CommercialSubjectService commercialSubjectService;
	
	@Autowired
	public SubjectControllerImpl(final CommercialSubjectService commercialSubjectService){
		this.commercialSubjectService=commercialSubjectService;
	}
	
	public void subjects(final CommercialSubjectsModelAO commercialSubjectsModel,  final Paging paging) {
		/*final CommercialSubject domain1 = new CommercialSubjectImpl(null, "EscortService", "Nicoles special service");
		final CommercialSubject domain2 = new CommercialSubjectImpl(null, "Music-Downloads", "Flatrate für Musik");
		EntityUtil.setId(domain1, 19680528L);
		EntityUtil.setId(domain2, 4711L);
		final List<CommercialSubject> results = new ArrayList<>();
		results.add(domain1);
		results.add(domain2);
		commercialSubjectsModel.setCommercialSubjects(results);
		
		System.out.println("subjects, paging:"+  paging.currentPage());
		paging.assignRowCounter(results.size()); */
		commercialSubjectsModel.setCommercialSubjects(commercialSubjectService.subjects("%", paging));
		
		
	}

}
