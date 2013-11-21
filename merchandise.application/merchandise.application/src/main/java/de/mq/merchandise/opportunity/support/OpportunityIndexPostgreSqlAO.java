package de.mq.merchandise.opportunity.support;

import java.util.Collection;

import de.mq.mapping.util.proxy.Getter;
import de.mq.mapping.util.proxy.NoModel;
import de.mq.mapping.util.proxy.Setter;

public abstract class OpportunityIndexPostgreSqlAO  implements RevisionAware{

	@Setter(clazz=NoModel.class, value="revision")
	public abstract void setRevision(final String revision);

	@Setter(value="deleted", clazz=NoModel.class)
	public abstract void setDeleted(boolean deleted) ;
	
	@Getter(value ="", converter=Opportunity2TSStringConverterImpl.class, clazz=OpportunityImpl.class)
	public abstract String getTS(); 

	@Getter(clazz=OpportunityImpl.class, value="" , converter=Opportunity2PointConverterImpl.class)
	public abstract Collection<String> getPoints();

}
