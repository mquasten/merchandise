package de.mq.merchandise.util.chouchdb.support;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestOperations;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"/geocodingRepository.xml"})
public class CouchTemplateIntegrationTest {
	@Autowired
	private RestOperations restOperations;
	
	
	@Test
	public final void singleKey() {
		final CouchDBTemplate couchDBTemplate = new CouchDBTemplate(restOperations, "petstore");
		couchDBTemplate.forKey("qualityByArtist", "nicole");
	}
}
