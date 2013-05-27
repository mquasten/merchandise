package de.mq.merchandise.controller;

import java.lang.reflect.Field;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.ReflectionUtils.FieldCallback;

import de.mq.merchandise.customer.Customer;
import de.mq.merchandise.customer.support.SecurityContextFactory;
import de.mq.merchandise.opportunity.CommercialSubjectService;
import de.mq.merchandise.opportunity.support.CommercialSubject;
import de.mq.merchandise.opportunity.support.CommercialSubjectAO;
import de.mq.merchandise.opportunity.support.CommercialSubjectsModelAO;

@Component("subjectController")
public class SubjectControllerImpl {
	
	private final CommercialSubjectService commercialSubjectService;
	
	private final SecurityContextFactory securityContextFactory;
	
	
	@Autowired
	public SubjectControllerImpl(final CommercialSubjectService commercialSubjectService, final SecurityContextFactory securityContextFactory){
		this.commercialSubjectService=commercialSubjectService;
		this.securityContextFactory=securityContextFactory;
	}
	
	public void subjects(final CommercialSubjectsModelAO commercialSubjectsModel) {
		final Customer customer = (Customer)securityContextFactory.securityContext().getAuthentication().getDetails();
		commercialSubjectsModel.setCommercialSubjects(commercialSubjectService.subjects(customer, commercialSubjectsModel.getPattern() + "%" , commercialSubjectsModel.getPaging().getPaging()));
	}
	
	
	
	
	
	public final void save(final CommercialSubject commercialSubject){
		final Customer customer = (Customer)securityContextFactory.securityContext().getAuthentication().getDetails();
		ReflectionUtils.doWithFields(commercialSubject.getClass(), new FieldCallback() {
			
			@Override
			public void doWith(final Field field) throws IllegalArgumentException, IllegalAccessException {
				if( field.getType().equals(Customer.class)){
					field.setAccessible(true);
					field.set(commercialSubject , customer);
				}
				
			}
		});
		commercialSubjectService.createOrUpdate(commercialSubject);
	}

	
	public final void delete(final CommercialSubjectAO commercialSubjectAO){
		if( commercialSubjectAO == null) {
			return;
		}
		System.out.println("delete" + commercialSubjectAO.getId());
		commercialSubjectService.delete(commercialSubjectAO.getCommercialSubject());
	}
	

}
