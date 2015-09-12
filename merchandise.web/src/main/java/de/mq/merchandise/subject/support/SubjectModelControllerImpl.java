package de.mq.merchandise.subject.support;

import java.util.Collection;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;

import de.mq.merchandise.ResultNavigation;
import de.mq.merchandise.subject.Condition;
import de.mq.merchandise.subject.Subject;
import de.mq.merchandise.subject.support.MapperQualifier.MapperType;
import de.mq.merchandise.subject.support.SubjectModel.EventType;
import de.mq.merchandise.support.Mapper;

@Controller
class SubjectModelControllerImpl {

	private final SubjectService subjectService;
	private final Mapper<Subject, Subject> subjectIntoSubjectMapper;

	private final Mapper<Condition, Subject> conditionIntoSubjectMapper;

	@Autowired
	SubjectModelControllerImpl(final SubjectService subjectService, final @MapperQualifier(MapperType.Subject2Subject) Mapper<Subject, Subject> subjectIntoSubjectMapper, final @MapperQualifier(MapperType.Condition2Subject) Mapper<Condition, Subject> conditionIntoSubjectMapper) {
		this.subjectService = subjectService;
		this.subjectIntoSubjectMapper = subjectIntoSubjectMapper;
		this.conditionIntoSubjectMapper = conditionIntoSubjectMapper;
	}

	@SubjectEventQualifier(EventType.CountPaging)
	Number countSubjects(final SubjectModel subjectModel) {
		return subjectService.subjects(subjectModel.getSearchCriteria());

	}

	@SubjectEventQualifier(EventType.ListPaging)
	Collection<Subject> subjects(final SubjectModel subjectModel, final ResultNavigation paging) {
		return subjectService.subjects(subjectModel.getSearchCriteria(), paging);
	}

	@SubjectEventQualifier(EventType.SubjectChanged)
	Subject subject(final Long id) {
		return subjectService.subject(id);
	}

	@SubjectEventQualifier(EventType.ConditionChanged)
	Condition condition(final SubjectModel subjectModel, final Long id) {
		Assert.notNull(id, "Id is mandatory");
		Assert.isTrue((subjectModel.getSubject().isPresent()), "Subject is mandatory");
		Assert.isTrue((subjectModel.getSubject().get().id().isPresent()), "SubjectId is mandatory");
		final Subject subject = subjectService.subject(subjectModel.getSubject().get().id().get());

		final Optional<Condition> result = subject.conditions().stream().filter(condition -> condition.id().orElse(-1L).equals(id)).findFirst();
		Assert.isTrue(result.isPresent(), "Condition should be persistent");
		return result.get();
	}

	@SubjectEventQualifier(EventType.SubjectSaved)
	void save(final Long subjectId, final Subject subject) {
		Assert.notNull(subject.customer(), "Customer is mandatory");
		if (subjectId == null) {
			subjectService.save(new SubjectImpl(subject.customer(), subject.name(), subject.description()));
			return;
		}

		final Subject toBeChanged = subjectService.subject(subjectId);
		Assert.notNull(toBeChanged.customer(), "Customer is mandatory");

		Assert.isTrue(toBeChanged.customer().equals(subject.customer()));

		subjectIntoSubjectMapper.mapInto(subject, toBeChanged);

		subjectService.save(toBeChanged);

	}

	@SubjectEventQualifier(EventType.ConditionSaved)
	Subject save(final Condition condition, final Long subjectId) {
		Assert.notNull(subjectId, "Subject should be persistent");
		final Subject subject = subjectService.subject(subjectId);
		conditionIntoSubjectMapper.mapInto(condition, subject);
		subjectService.save(subject);
		return subject;
	}

	@SubjectEventQualifier(EventType.SubjectDeleted)
	void delete(final Subject subject) {

		subjectService.remove(subject);

	}

	@SubjectEventQualifier(EventType.ConditionDeleted)
	Subject delete(final Condition condition, final Long subjectId) {
		Assert.notNull(subjectId, "Subject should be persistent");
		Assert.notNull(condition.conditionType(), "ConditionType is mandatory");
		final Subject subject = subjectService.subject(subjectId);

		subject.remove(condition.conditionType());

		subjectService.save(subject);
		return subject;
	}

}
