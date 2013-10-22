package de.mq.merchandise.opportunity.support;

import java.io.Serializable;
import java.util.Collection;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import de.mq.mapping.util.proxy.Getter;
import de.mq.mapping.util.proxy.NoModel;
import de.mq.mapping.util.proxy.support.Enum2StringConverter;
import de.mq.mapping.util.proxy.support.Number2StringConverter;


@JsonSerialize(include=JsonSerialize.Inclusion.NON_EMPTY)
public abstract class OpportunityIndexAO  implements Serializable{
	
	private static final long serialVersionUID = 1L;

	@Getter(clazz = OpportunityImpl.class, value = "id", converter = Number2StringConverter.class)
	@JsonProperty("_id")
	public abstract String getId(); 
	
	@Getter(clazz = OpportunityImpl.class, value = "name")
	@JsonProperty("name")
	public abstract String getName();
	
	@Getter(clazz = OpportunityImpl.class, value = "description")
	@JsonProperty("description")
	public abstract String getDescription();
	
	@Getter(clazz = OpportunityImpl.class, value = "keyWords")
	@JsonProperty("keyWords")
	public abstract Collection<String> getKeyWords(); 
	
	@Getter(clazz=NoModel.class, value="revision")
	@JsonProperty("_rev")
	public abstract String getRevison(); 
	
	@Getter(clazz = OpportunityImpl.class, value = "kind", converter = Enum2StringConverter.class)
	@JsonProperty("kind")
	public abstract String getKind(); 
	
	@Getter(clazz=NoModel.class, value="deleted")
	@JsonProperty("deleted")
	public abstract Boolean isDeleted();
	
	

}
