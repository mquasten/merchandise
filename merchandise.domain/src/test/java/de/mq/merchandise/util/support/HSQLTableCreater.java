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

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.stereotype.Component;

@Component
public class HSQLTableCreater implements BeanFactoryPostProcessor {
	
	String[] SQLS = new String[] {"CREATE MEMORY TABLE PUBLIC.COMMERCIAL_SUBJECT_DOCUMENTS(COMMERCIAL_SUBJECT_ID BIGINT NOT NULL,STORED_DOCUMENT VARBINARY(255),DOCUMENT_NAME VARCHAR(50) NOT NULL,PRIMARY KEY(COMMERCIAL_SUBJECT_ID,DOCUMENT_NAME),CONSTRAINT FKD8E441B01094C1A3 FOREIGN KEY(COMMERCIAL_SUBJECT_ID) REFERENCES PUBLIC.COMMERCIAL_SUBJECT(ID))", 
			                     "CREATE MEMORY TABLE PUBLIC.OPPORTUNITY_DOCUMENTS(OPPORTUNITY_ID BIGINT NOT NULL,STORED_DOCUMENT BLOB(16M),DOCUMENT_NAME VARCHAR(50) NOT NULL,PRIMARY KEY(OPPORTUNITY_ID,DOCUMENT_NAME),CONSTRAINT FKD311084C4DFF64D8 FOREIGN KEY(OPPORTUNITY_ID) REFERENCES PUBLIC.OPPORTUNITY(ID))"
	};
	@Override
	public void postProcessBeanFactory(final ConfigurableListableBeanFactory beanFactory) throws BeansException {
		
	    beanFactory.getBean(EntityManagerFactory.class);
		final DataSource ds = beanFactory.getBean(DataSource.class);
		try (final Connection con = ds.getConnection()) {
			
			if( ! con.getMetaData().getDriverName().startsWith("HSQL")){
				return;
			}
				
		    processSql(con);

			
		} catch (final SQLException ex) {
			 System.err.println(ex.getMessage());
		} 
		process(ds, "docs/products.sql"); 
		process(ds, "docs/activities.sql"); 
		
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private void processSql(final Connection con) throws SQLException {
		for(final String sql : SQLS ) {
			final Statement st = con.createStatement(); 
			st.executeUpdate(sql);
			st.close();
		}
	}
	
	
	
	
	private void create(final Connection connection, final String file) throws FileNotFoundException, IOException, SQLException {
		try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
			String line;
			int counter=0;
			while( (line = reader.readLine()) != null){
				final Statement st = connection.createStatement(); 
				st.executeUpdate(line);
				st.close();
				counter++;
			}
			System.out.println("Processed: " + file + " statements:" + counter );
		}
	}

}
