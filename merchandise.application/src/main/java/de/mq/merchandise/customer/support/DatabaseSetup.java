package de.mq.merchandise.customer.support;

import javax.sql.DataSource;

import org.springframework.stereotype.Component;

import de.mq.merchandise.domain.support.AbstractDatabaseSetup;

@Component
public class DatabaseSetup  extends AbstractDatabaseSetup {

	@Override
	protected void onInit(final DataSource dataSource) {
		process(dataSource, "docs/deleteCustomer.sql");
		process(dataSource, "docs/createCustomer.sql");
		
	}

	@Override
	protected void onDestroy(final DataSource dataSource) {
		process(dataSource, "docs/deleteCustomer.sql");
		if( ! isHSQL(dataSource)){
			return ; 
		}
		 
			
	  process(dataSource, "docs/shutdown.sql");
		
	}

}
