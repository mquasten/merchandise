package de.mq.merchandise.opportunity.support;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import javax.validation.constraints.Size;

import org.primefaces.model.TreeNode;

import de.mq.mapping.util.proxy.ActionEvent;
import de.mq.mapping.util.proxy.Getter;
import de.mq.mapping.util.proxy.GetterDomain;
import de.mq.mapping.util.proxy.GetterProxy;
import de.mq.mapping.util.proxy.GetterProxyCollection;
import de.mq.mapping.util.proxy.MethodInvocation;
import de.mq.mapping.util.proxy.Parameter;
import de.mq.mapping.util.proxy.Setter;
import de.mq.mapping.util.proxy.SetterDomain;
import de.mq.mapping.util.proxy.support.Enum2StringConverter;
import de.mq.mapping.util.proxy.support.Number2StringConverter;
import de.mq.merchandise.customer.support.CustomerAO;
import de.mq.merchandise.model.support.String2LongConverter;
import de.mq.merchandise.util.support.HibernateProxyConverter;

public abstract class OpportunityAO implements Serializable {
	
	
	private static final long serialVersionUID = 1L;

	@Getter(clazz = OpportunityImpl.class, value = "id", converter = Number2StringConverter.class)
	public abstract String getId();
	
	@Setter(clazz = OpportunityImpl.class, value = "id", converter =String2LongConverter.class)
	public abstract void setId(final String id);
	
	@Size(min=1,  message="{mandatory_field}")
	@Getter(clazz = OpportunityImpl.class, value = "name")
	public abstract String getName();

	@Setter(clazz = OpportunityImpl.class, value = "name")
	public abstract void setName(final String name);
	
	
	@Getter(clazz = OpportunityImpl.class, value = "description")
	public abstract String getDescription();

	@Setter(clazz = OpportunityImpl.class, value = "description")
	public abstract void setDescription(final String description);
	
	@GetterProxy(clazz=OpportunityImpl.class, name = "customer", proxyClass = CustomerAO.class , converter=HibernateProxyConverter.class)
	public abstract CustomerAO getCustomer(); 
	
	@Getter(clazz=OpportunityImpl.class, value="kind", converter=Enum2StringConverter.class)
	public abstract  String getKind();

	@Setter(clazz=OpportunityImpl.class, value="kind" , converter=String2OpportunityKindConverter.class)
	public abstract void setKind(String kind);
	

	@Getter( clazz=OpportunityImpl.class,  value = "keyWords" )
	public abstract Set<String> getKeyWords();
	
	
	@GetterProxyCollection(clazz=OpportunityImpl.class, name = "activityClassifications",  proxyClass = ActivityClassificationAO.class , converter=HibernateProxyConverter.class)
	public abstract List<ActivityClassificationAO> getActivityClassifications();
	
	@Setter(clazz=OpportunityImpl.class, value="activityClassifications", converter=TreeNode2ClassificationConverterImpl.class )
	public abstract void setActivities(TreeNode[]  activityClassification);
	
	@Getter(clazz=OpportunityImpl.class, value="activityClassifications", converter=Classification2TreeNodeConverter.class )
	public abstract TreeNode[] getActivities();
	
	@GetterProxyCollection(clazz=OpportunityImpl.class, name = "procuctClassifications", proxyClass = ProductclassificationAO.class , converter=HibernateProxyConverter.class)
	public abstract Collection<ProductclassificationAO> getProcuctClassifications();
	

	//@GetterProxyCollection(name = "commercialRelations", proxyClass = CommercialRelationAO.class, collectionClass = HashSet.class , converter=HibernateProxyConverter.class )
	//private Set<CommercialRelation> commercialRelations = new HashSet<>(); 
	
	@GetterDomain(clazz=OpportunityImpl.class)
	public abstract Opportunity getOpportunity(); 
	
	@SetterDomain(clazz=OpportunityImpl.class)
	public abstract void setOpportunity(final Opportunity opportunity); 
	
	
	/*
	 * like an observer ... 
	 */
	@MethodInvocation(actions={@ActionEvent(  params={@Parameter(clazz=OpportunityAO.class, domain=OpportunityImpl.class,elResultType=Collection.class ,  el="#arg.activityClassifications()"), @Parameter(clazz = ActivityClassificationTreeAO.class, elResultType=TreeNode.class, el="#arg.treeNode")})}, clazz = ClassificationTreeChangedObserveableControllerImpl.class)
    public abstract void notifyActivityClassificationChanged();
	
	/*
	 * like an observer, too ... 
	 */
	@MethodInvocation(actions={@ActionEvent(  params={@Parameter(clazz=OpportunityAO.class, domain=OpportunityImpl.class,elResultType=Collection.class ,  el="#arg.productClassifications()"), @Parameter(clazz = ProductClassificationTreeAO.class, elResultType=TreeNode.class, el="#arg.treeNode")})}, clazz = ClassificationTreeChangedObserveableControllerImpl.class)
    public abstract void notifyProductClassificationChanged();

}
