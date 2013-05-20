package de.mq.merchandise.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import de.mq.merchandise.model.support.SimpleMapDataModel;
import de.mq.merchandise.model.support.WebProxyFactory;
import de.mq.merchandise.opportunity.support.CommercialSubject;
import de.mq.merchandise.opportunity.support.CommercialSubjectAO;
import de.mq.merchandise.opportunity.support.CommercialSubjectImpl;
import de.mq.merchandise.util.EntityUtil;
import de.mq.merchandise.util.Paging;

@Component("subjectController")
public class SubjectControllerImpl {
	
	
	
	private final WebProxyFactory webProxyFactory;
	
	@Autowired
	public SubjectControllerImpl(final WebProxyFactory webProxyFactory){
		this.webProxyFactory=webProxyFactory;
	}
	
	
	public List<CommercialSubjectAO> subjects(final Paging paging) {
		
		
		System.out.println("subjects, paging:"+  paging.currentPage());
		final List<CommercialSubjectAO> results = new SimpleMapDataModel<>();
		final CommercialSubject domain1 = new CommercialSubjectImpl(null, "EscortService", "Nicoles special service");
		final CommercialSubject domain2 = new CommercialSubjectImpl(null, "Music-Downloads", "Flatrate für Musik");
		EntityUtil.setId(domain1, 19680528L);
		EntityUtil.setId(domain2, 4711L);
        results.add(webProxyFactory.webModell(CommercialSubjectAO.class, domain1)); 
        results.add(webProxyFactory.webModell(CommercialSubjectAO.class, domain2)); 

		
		paging.assignRowCounter(results.size());
		return results;
		
	}

}
