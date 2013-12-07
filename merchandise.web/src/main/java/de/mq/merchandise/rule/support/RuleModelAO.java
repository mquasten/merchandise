package de.mq.merchandise.rule.support;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import de.mq.mapping.util.proxy.Getter;
import de.mq.mapping.util.proxy.GetterProxyCollection;
import de.mq.mapping.util.proxy.Setter;
import de.mq.merchandise.model.support.SimpleMapDataModel;
import de.mq.merchandise.opportunity.support.PagingAO;
import de.mq.merchandise.rule.Rule;
import de.mq.merchandise.util.support.HibernateProxyConverter;

public abstract class RuleModelAO implements Serializable {

	private static final long serialVersionUID = 1L;

	@GetterProxyCollection(name = "rules", proxyClass = RuleAO.class, collectionClass = SimpleMapDataModel.class , converter=HibernateProxyConverter.class )
	public abstract List<RuleAO> getRules();

	@Setter(value = "rules")
	public abstract void setRules(final Collection<? extends Rule> rules);

	@Getter(value = "selected")
	public abstract RuleAO getSelected();

	@Setter(value = "selected")
	public abstract void setSelected(final RuleAO selected);
	
	@Setter(value = "pattern")
	public abstract void setPattern(final String pattern);
	
	@Getter(value = "pattern")
	public abstract String getPattern();
	
	@Getter(value = "paging")
	public abstract PagingAO getPaging();
	

}
