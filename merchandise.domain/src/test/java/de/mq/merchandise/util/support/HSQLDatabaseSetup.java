package de.mq.merchandise.util.support;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.mockito.internal.matchers.StartsWith;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.stereotype.Component;

@Component
public class HSQLDatabaseSetup implements BeanFactoryPostProcessor, ApplicationListener<ContextClosedEvent> {
	
	@Override
	public void postProcessBeanFactory(final ConfigurableListableBeanFactory beanFactory) throws BeansException {
		/* start the shit, to create the schema, it must must not be blessed, untouched ... */
		beanFactory.getBean(EntityManagerFactory.class);
		final DataSource ds = beanFactory.getBean(DataSource.class);
		if ( ! isHSQL(ds) ) {
			return;
		}
		process(ds, "docs/lobs.sql");
		process(ds, "docs/products.sql"); 
		process(ds, "docs/activities.sql"); 
		
	}
	private boolean isHSQL(final DataSource ds) {
		try (final Connection con = ds.getConnection()) {
			
			if( con.getMetaData().getDriverName().startsWith("HSQL")){
				return true;
			}
				
			return false;	
		} catch (final SQLException ex) {
			 System.err.println(ex.getMessage());
			 return false;
		}
		
	}
	private void process(DataSource ds, final String file) {
		try(final Connection con = ds.getConnection()) {
		   processFile(con,  file);
		} catch (SQLException e) {
			System.out.println("Error processing: "+ file);
		}
	}
	private void processFile(final Connection connection, final String file) {
		try {
			create(connection, file);
		} catch (IOException | SQLException e) {
			
			System.err.println("Error processing "+ file +": "+ e.getMessage());
		}
	}
	
	
	
	
	
	private void create(final Connection connection, final String file) throws FileNotFoundException, IOException, SQLException {
		try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
			String line;
			int counter=0;
			while( (line = reader.readLine()) != null){
				if( line.trim().startsWith("--") || line.trim().startsWith("#") || line.trim().startsWith("//") )  {
					continue;
				}
				final Statement st = connection.createStatement(); 
				st.executeUpdate(line);
				st.close();
				counter++;
			}
			System.out.println("Processed: " + file + " statements:" + counter );
		}
	}
	@Override
	public void onApplicationEvent(final ContextClosedEvent event) {
		final DataSource ds = event.getApplicationContext().getBean(DataSource.class);
		if( ! isHSQL(ds) ) {
			return;
		}
		process(ds, "docs/shutdown.sql"); 
	}

}
