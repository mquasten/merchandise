package de.mq.merchandise.domain.subject;

import javax.sql.DataSource;

import org.springframework.stereotype.Component;

import de.mq.merchandise.domain.support.AbstractDatabaseSetup;

@Component
public class HsqlSetup extends AbstractDatabaseSetup {

	@Override
	protected void onInit(final DataSource dataSource) {
		
	}

	@Override
	protected void onDestroy(final DataSource dataSource) {
		if( ! isHSQL(dataSource)){
			return;
		}
		process(dataSource, "docs/shutdown.sql");
		
	}

}
