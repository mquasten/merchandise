package de.mq.merchandise.util.chouchdb.support;

import de.mq.mapping.util.json.support.AbstractMapBasedResult;
import de.mq.mapping.util.json.support.MapBasedResultRow;
import de.mq.mapping.util.json.support.Mapping;
import de.mq.mapping.util.json.support.SimpleMapBasedResultRowImpl;


public class SimpleCouchDBResultImpl extends AbstractMapBasedResult{

	
	private static final long serialVersionUID = 1L;

	@Override
	protected void configure() {
		assignRowClass(SimpleMapBasedResultRowImpl.class);
		Mapping<MapBasedResultRow> parent = assignParentResultMapping("rows");
		assignChildRowMapping(parent, "value", "value");
		assignChildRowMapping(parent, "key", "key");
		//assignChildRowMapping(parent, "id", "id");
		//assignParentFieldMapping("total_rows",  "info" );
		//assignParentFieldMapping("offset",  "description" );
	}

}
