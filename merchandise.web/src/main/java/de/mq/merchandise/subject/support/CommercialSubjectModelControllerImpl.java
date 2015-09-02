package de.mq.merchandise.subject.support;

import java.lang.reflect.Field;
import java.util.Collection;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import org.springframework.util.ReflectionUtils;

import de.mq.merchandise.ResultNavigation;
import de.mq.merchandise.customer.Customer;
import de.mq.merchandise.customer.support.CustomerImpl;
import de.mq.merchandise.subject.support.CommercialSubjectModel.EventType;

@Controller
class CommercialSubjectModelControllerImpl {

	private final CommercialSubjectService commercialSubjectService;
	
	@Autowired
	CommercialSubjectModelControllerImpl(CommercialSubjectService commercialSubjectService) {
		this.commercialSubjectService = commercialSubjectService;
		
	}

	@CommercialSubjectEventQualifier(EventType.CountPaging)
	Number countCommercialSubjects(final CommercialSubjectModel subjectModel) {
	
		Number result =  commercialSubjectService.commercialSubjects(search(customer()));
		System.out.println(result);
		return result;
	}

	@CommercialSubjectEventQualifier(EventType.ListPaging)
	Collection<CommercialSubject> commercialSubjects(final CommercialSubjectModel subjectModel, final ResultNavigation paging) {
		Customer customer = customer();
		
		
		
		
		Collection<CommercialSubject> results = commercialSubjectService.commercialSubjects(search(customer),paging);
	
		return results;
	}

	private CommercialSubjectImpl search(Customer customer) {
		return new CommercialSubjectImpl("", customer);
	}

	private Customer customer() {
		Customer customer = BeanUtils.instantiateClass(CustomerImpl.class);
		Field field = ReflectionUtils.findField(CustomerImpl.class, "id");
		field.setAccessible(true);
		
		ReflectionUtils.setField(field, customer, 1L);
		return customer;
	}
	


	

}
