package de.mq.merchandise.controller;

import de.mq.merchandise.customer.Customer;
import de.mq.merchandise.customer.support.SecurityContextFactory;
import de.mq.merchandise.opportunity.CommercialSubjectService;
import de.mq.merchandise.opportunity.support.CommercialSubject;
import de.mq.merchandise.opportunity.support.CommercialSubjectAO;
import de.mq.merchandise.opportunity.support.CommercialSubjectsModelAO;
import de.mq.merchandise.util.EntityUtil;


public class SubjectControllerImpl {
	
	private final CommercialSubjectService commercialSubjectService;
	
	private final SecurityContextFactory securityContextFactory;
	
	public SubjectControllerImpl(final CommercialSubjectService commercialSubjectService, final SecurityContextFactory securityContextFactory){
		this.commercialSubjectService=commercialSubjectService;
		this.securityContextFactory=securityContextFactory;
	}
	
	public void subjects(final CommercialSubjectsModelAO commercialSubjectsModel) {
		final Customer customer = (Customer)securityContextFactory.securityContext().getAuthentication().getDetails();
		commercialSubjectsModel.setCommercialSubjects(commercialSubjectService.subjects(customer, commercialSubjectsModel.getPattern() + "%" , commercialSubjectsModel.getPaging().getPaging()));
		
		updateSelection(commercialSubjectsModel);
	}

	void updateSelection(final CommercialSubjectsModelAO commercialSubjectsModel) {
		if( commercialSubjectsModel.getSelected() == null){
			return;
		}
		for(final CommercialSubjectAO subjectAO : commercialSubjectsModel.getCommercialSubjects()){
		    if(subjectAO.getCommercialSubject().equals(commercialSubjectsModel.getSelected().getCommercialSubject())){
		    	return;
		    }
		} 
		
		
		commercialSubjectsModel.setSelected(null);
	}
	
	
	
	
	
	public final void save(final CommercialSubject commercialSubject){
		final Customer customer = (Customer)securityContextFactory.securityContext().getAuthentication().getDetails();
		EntityUtil.setDependency(commercialSubject, Customer.class, customer);
		commercialSubjectService.createOrUpdate(commercialSubject);
	}

	
	public final void delete(final CommercialSubjectAO commercialSubjectAO){
		if( commercialSubjectAO == null) {
			return;
		}
		commercialSubjectService.delete(commercialSubjectAO.getCommercialSubject());
	}
	
	public final void openChangeDialog(final CommercialSubjectAO  selected, final CommercialSubjectAO commercialSupject){
		if( selected == null){
			return;
		}
		
		/* like a virgin, touched for the very first time ... */
		commercialSupject.setCommercialSubject(commercialSubjectService.subject(selected.getCommercialSubject().id()));
		
	}
	
	
}
