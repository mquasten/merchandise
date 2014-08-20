package de.mq.mapping.util.json.support;

import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;

import de.mq.mapping.util.json.MapBasedResultBuilder;

public class MappingTestConstants {
	
	public static MapBasedResultBuilder newMappingBuilder() {
		return new MapBasedResultBuilderImpl();
	}
	
	public static MapBasedResponse newEnhancedMapBasedResponse(final Collection<?> sommeMappings, Map<String,Object> map) {
		final AbstractMapBasedResult result =  new AbstractMapBasedResult(){

			
			private static final long serialVersionUID = 1L;

			@SuppressWarnings("unchecked")
			@Override
			protected void configure() {
				this.mappings.clear();
				this.mappings.addAll((Collection<? extends Mapping>) sommeMappings);
				
			}};
			for(final Entry<String, Object> entry : map.entrySet()){
				result.put(entry.getKey(), entry.getValue());
			}
			return result;
	}

}
