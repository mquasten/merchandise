package de.mq.merchandise.util.chouchdb.support;

import de.mq.merchandise.util.chouchdb.MapBasedResultRow;

public class MapsCoordinatesResultImpl extends AbstractMapBasedResult{

	
	private static final long serialVersionUID = 1L;

	@Override
	protected void configure() {
		assignRowClass(SimpleMapBasedResultRowImpl.class);
		Mapping<MapBasedResultRow> parent = assignParentResultMapping("results");
		assignChildRowMapping(parent, "value", "geometry",  "location");
		assignChildRowMapping(parent, "key", "types");
		assignParentFieldMapping("status", "status");
		//assignChildRowMapping(parent, "id", "id");
		//assignParentFieldMapping("total_rows",  "info" );
		//assignParentFieldMapping("offset",  "description" );
	}

}
