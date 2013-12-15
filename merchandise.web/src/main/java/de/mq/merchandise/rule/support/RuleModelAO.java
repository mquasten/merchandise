package de.mq.merchandise.rule.support;


import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.security.core.context.SecurityContext;

import de.mq.mapping.util.proxy.ActionEvent;
import de.mq.mapping.util.proxy.Getter;
import de.mq.mapping.util.proxy.GetterProxyCollection;
import de.mq.mapping.util.proxy.MethodInvocation;
import de.mq.mapping.util.proxy.Parameter;
import de.mq.mapping.util.proxy.Setter;
import de.mq.merchandise.controller.SerialisationControllerImpl;
import de.mq.merchandise.controller.State;
import de.mq.merchandise.customer.Customer;
import de.mq.merchandise.model.support.FacesContextFactory;
import de.mq.merchandise.model.support.SimpleMapDataModel;
import de.mq.merchandise.opportunity.support.DocumentModelAO;
import de.mq.merchandise.opportunity.support.PagingAO;
import de.mq.merchandise.rule.Rule;
import de.mq.merchandise.util.support.HibernateProxyConverter;

@State({"paging.currentPage", "selected.id" , "pattern"})
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
	
	@MethodInvocation(actions={@ActionEvent(name="serialize" , params={@Parameter(clazz = Object.class, proxy=true)})}, clazz = SerialisationControllerImpl.class)
	public abstract String state();
	
	@MethodInvocation(actions={@ActionEvent(name="deserialize" ,params={@Parameter(clazz = Object.class, proxy=true) ,  @Parameter(clazz = FacesContextFactory.class , el="#arg.facesContext().externalContext.requestParameterMap['state']" , elResultType=String.class ) }) , @ActionEvent(clazz=RuleControllerImpl.class, name="rules", params={@Parameter(clazz = RuleModelAO.class, proxy=true),  @Parameter(clazz = SecurityContext.class , el="#arg.authentication.details" , elResultType=Customer.class)}) }, clazz=SerialisationControllerImpl.class)
	@PostConstruct()
	public abstract void initRuleModelAO() ;
	

}
