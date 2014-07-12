package de.mq.merchandise.util.chouchdb.support;

import de.mq.merchandise.util.chouchdb.MapBasedResultRow;

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
