package de.mq.merchandise.subject.support;

import java.util.Collection;
import java.util.Optional;

import javax.persistence.Id;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.util.ReflectionUtils;

import de.mq.merchandise.ResultNavigation;
import de.mq.merchandise.customer.Customer;
import de.mq.merchandise.subject.Condition;
import de.mq.merchandise.subject.Subject;
import de.mq.merchandise.subject.support.CommercialSubjectModel.EventType;
import de.mq.merchandise.subject.support.MapperQualifier.MapperType;
import de.mq.merchandise.support.Mapper;

@Controller
class CommercialSubjectModelControllerImpl {

	private final CommercialSubjectService commercialSubjectService;
	
	private final SubjectService subjectService;
	
	
	private final Mapper<CommercialSubject,CommercialSubject>  commercialSubject2CommercialSubjectMapper;
	
	private final Mapper<CommercialSubjectItem,CommercialSubject> commercialSubjectItemIntoCommercialSubjectMapper;
	
	@Autowired
	CommercialSubjectModelControllerImpl(CommercialSubjectService commercialSubjectService,final SubjectService subjectService, @MapperQualifier(MapperType.CommercialSubject2CommercialSubject) final Mapper<CommercialSubject,CommercialSubject>  commercialSubject2CommercialSubjectMapper, final @MapperQualifier(MapperType.CommercialSubjectItemIntoCommercialSubject) Mapper<CommercialSubjectItem,CommercialSubject> commercialSubjectItemIntoCommercialSubjectMapper) {
		this.commercialSubjectService = commercialSubjectService;
		this.subjectService=subjectService;
		this.commercialSubject2CommercialSubjectMapper=commercialSubject2CommercialSubjectMapper;
		this.commercialSubjectItemIntoCommercialSubjectMapper=commercialSubjectItemIntoCommercialSubjectMapper;
	}

	@CommercialSubjectEventQualifier(EventType.CountPaging)
	Number countCommercialSubjects(final CommercialSubjectModel subjectModel) {
		return commercialSubjectService.commercialSubjects(subjectModel.getSearch());
	}

	@CommercialSubjectEventQualifier(EventType.ListPaging)
	Collection<CommercialSubject> commercialSubjects(final CommercialSubjectModel subjectModel, final ResultNavigation paging) {
		return commercialSubjectService.commercialSubjects(subjectModel.getSearch(),paging);
	}


	@CommercialSubjectEventQualifier(EventType.CommericalSubjectChanged)
	CommercialSubject subject(final Long id) {
		return commercialSubjectService.commercialSubject(id);
	}


	@CommercialSubjectEventQualifier(EventType.CommercialSubjectSaved)
	void save(final Long commercialSubjectId, final CommercialSubject commercialSubject) {
		Assert.notNull(commercialSubject.customer(), "Customer is mandatory");
		
		if (commercialSubjectId == null) {
			commercialSubjectService.save(new CommercialSubjectImpl( commercialSubject.name(), commercialSubject.customer()));
			return;
		}

		final CommercialSubject toBeChanged = commercialSubjectService.commercialSubject(commercialSubjectId);
		Assert.notNull(toBeChanged.customer(), "Customer is mandatory");

		Assert.isTrue(toBeChanged.customer().equals(commercialSubject.customer()));

		
		commercialSubject2CommercialSubjectMapper.mapInto(commercialSubject, toBeChanged);

		commercialSubjectService.save(toBeChanged);
	}


	@CommercialSubjectEventQualifier(EventType.CommercialSubjectDeleted)
	void delete(final CommercialSubject commercialSubject) {
		commercialSubjectService.remove(commercialSubject);
	}
	
	@CommercialSubjectEventQualifier(EventType.ListSubjects)
	public Collection<Subject> subjects(final Customer customer) {
		Assert.notNull(customer, "customer is mandatory");
		Assert.isTrue(customer.id().isPresent(), "Customer should be persistent");
		return subjectService.subjects(customer);
	}
	
	@CommercialSubjectEventQualifier(EventType.ItemSaved)
	CommercialSubject saveItem(final CommercialSubjectItem commercialSubjectItem, final Long id ) {
		Assert.notNull(id, "Id is mandatory");
		final CommercialSubject commercialSubject = commercialSubjectItemIntoCommercialSubjectMapper.mapInto(commercialSubjectItem, commercialSubjectService.commercialSubject(id));
		commercialSubjectService.save(commercialSubject);
		return commercialSubject;
	}
	
	@CommercialSubjectEventQualifier(EventType.CommericalSubjectItemChanged)
	CommercialSubjectItem commericalSubjectItem(final CommercialSubjectModel model, final Long itemId ) {
		Assert.isTrue(model.getCommercialSubject().isPresent(), "CommercialSubject is mandatory");
		Assert.isTrue(model.getCommercialSubject().get().id().isPresent());
		final CommercialSubject commercialSubject = commercialSubjectService.commercialSubject(model.getCommercialSubject().get().id().get());
		Assert.notNull(commercialSubject, "CommercialSubject not found");
		Optional<CommercialSubjectItem> result = commercialSubject.commercialSubjectItems().stream().filter(i -> i.id().get().equals(itemId)).findAny();
		Assert.isTrue(result.isPresent(), "Item not found");
		return result.get();
		
	}
	
	
	@CommercialSubjectEventQualifier(EventType.CommericalSubjectItemDeleted)
	CommercialSubject delete(final CommercialSubjectItem commercialSubjectItem, final Long subjectId) {
		Assert.notNull(subjectId , "ParentId is mandatory");
		Assert.notNull(commercialSubjectItem, "Parent is mandatory");
		Assert.notNull(commercialSubjectItem.subject(), "Subject is mandatory");
		Assert.isTrue(commercialSubjectItem.subject().id().isPresent(), "Subject should be persistent");
		
		final Subject subject = subjectService.subject(commercialSubjectItem.subject().id().get());
		final CommercialSubject toBeChanged = commercialSubjectService.commercialSubject(subjectId);
	
		toBeChanged.remove(subject);
		commercialSubjectService.save(toBeChanged);
		return toBeChanged;
		
	}
	
	@CommercialSubjectEventQualifier(EventType.ConditionChanged)
	CommercialSubjectItemConditionImpl  conditionChanged(final CommercialSubjectModel model, final Long conditionId) {
		
	
		final Condition condition = BeanUtils.instantiateClass(ConditionImpl.class);
		ReflectionUtils.doWithFields(condition.getClass(), field -> {field.setAccessible(true); ReflectionUtils.setField(field, condition, conditionId);}, field -> field.isAnnotationPresent(Id.class) );
		
		
		final CommercialSubjectItemConditionImpl result =BeanUtils.instantiateClass(CommercialSubjectItemConditionImpl.class);
		ReflectionUtils.doWithFields(CommercialSubjectItemConditionImpl.class, field ->  { field.setAccessible(true); ReflectionUtils.setField(field, result, condition);} , field -> field.getType().equals(Condition.class)); 
		return result;
		
	}
	
	@CommercialSubjectEventQualifier(EventType.AddInputValue)
	CommercialSubjectItem addInputValue(final CommercialSubjectModel model, final Long conditionId) {
		Assert.isTrue(model.getCommercialSubject().isPresent(), "commercialSubject is manatory");
		Assert.isTrue(model.getCommercialSubject().get().id().isPresent(), "commercialSubject should be persistent");
		Assert.notNull(model.getCommercialSubjectItem().get().subject(), "Subject is mandatory");
		final CommercialSubject commercialSubject = commercialSubjectService.commercialSubject(model.getCommercialSubject().get().id().get());
		
		final Optional<CommercialSubjectItem> commercialSubjectItem = commercialSubject.commercialSubjectItem(model.getCommercialSubjectItem().get().subject());
		Assert.isTrue(commercialSubjectItem.isPresent() ,"CommercialSubjectItem must be present for for subject: " + model.getCommercialSubjectItem().get().subject());
		final CommercialSubjectItem item = commercialSubjectItem.get();
	
		item.assign(model.getCondition(conditionId).conditionType(), model.convertConditionValue(model.getInputValue(), conditionId));
		
		commercialSubjectService.save(commercialSubject);
		return item;
	}


	
	@CommercialSubjectEventQualifier(EventType.DeleteInputValue)
	CommercialSubjectItem deleteInputValue(final CommercialSubjectModel model, final Long conditionId, final String currentValue) {
		
	
		final CommercialSubject commercialSubject = commercialSubjectService.commercialSubject(model.getCommercialSubject().get().id().get());
		
		
		final Optional<CommercialSubjectItem> commercialSubjectItem = commercialSubject.commercialSubjectItem(model.getCommercialSubjectItem().get().subject());
		Assert.isTrue(commercialSubjectItem.isPresent() ,"CommercialSubjectItem must be present for for subject: " + model.getCommercialSubjectItem().get().subject());
		final CommercialSubjectItem item = commercialSubjectItem.get();
		
		item.remove(model.getCondition(conditionId).conditionType(), model.convertConditionValue(currentValue, conditionId));
		commercialSubjectService.save(commercialSubject);
		return item;
	}
	
}
