package de.mq.merchandise.opportunity.support;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import de.mq.mapping.util.proxy.Getter;
import de.mq.mapping.util.proxy.GetterProxyCollection;
import de.mq.mapping.util.proxy.Setter;
import de.mq.merchandise.model.support.SimpleMapDataModel;
import de.mq.merchandise.util.support.HibernateProxyConverter;

public abstract class OpportunityModelAO implements Serializable {

	private static final long serialVersionUID = 1L;

	@GetterProxyCollection(name = "opportunities", proxyClass = OpportunityAO.class, collectionClass = SimpleMapDataModel.class , converter=HibernateProxyConverter.class )
	public abstract List<OpportunityAO> getOpportunities();

	@Setter(value = "opportunities")
	public abstract void setOpportunities(final Collection<? extends Opportunity> opportunities);

	@Getter(value = "selected")
	public abstract OpportunityAO getSelected();

	@Setter(value = "selected")
	public abstract void setSelected(final OpportunityAO selected);
	
	@Setter(value = "pattern")
	public abstract void setPattern(final String pattern);
	
	@Getter(value = "pattern")
	public abstract String getPattern();
	
	@Getter(value = "paging")
	public abstract PagingAO getPaging();


}
