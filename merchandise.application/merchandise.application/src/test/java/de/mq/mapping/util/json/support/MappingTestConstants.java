package de.mq.mapping.util.json.support;

import de.mq.mapping.util.json.MapBasedResultBuilder;

public class MappingTestConstants {
	
	public static MapBasedResultBuilder newMappingBuilder() {
		return new MapBasedResultBuilderImpl();
	}

}
