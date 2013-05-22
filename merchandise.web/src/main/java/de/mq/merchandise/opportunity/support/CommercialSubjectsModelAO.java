package de.mq.merchandise.opportunity.support;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import de.mq.mapping.util.proxy.Getter;
import de.mq.mapping.util.proxy.GetterProxyCollection;
import de.mq.mapping.util.proxy.Setter;
import de.mq.merchandise.model.support.SimpleMapDataModel;

public abstract class CommercialSubjectsModelAO implements Serializable {

	private static final long serialVersionUID = 1L;

	@GetterProxyCollection(name = "commercialSubjects", proxyClass = CommercialSubjectAO.class, collectionClass = SimpleMapDataModel.class)
	public abstract List<CommercialSubject> getCommercialSubjects();

	@Setter(value = "commercialSubjects")
	public abstract void setCommercialSubjects(final Collection<? extends CommercialSubject> commercialSubjects);

	@Getter(value = "selected")
	public abstract CommercialSubjectAO getSelected();

	@Setter(value = "selected")
	public abstract void setSelected(final CommercialSubjectAO selected);
	
	@Setter(value = "pattern")
	public abstract void setPattern(final String pattern);
	
	@Getter(value = "pattern")
	public abstract String getPattern();
	
	@Getter(value = "paging")
	public abstract PagingAO getPaging();


}
