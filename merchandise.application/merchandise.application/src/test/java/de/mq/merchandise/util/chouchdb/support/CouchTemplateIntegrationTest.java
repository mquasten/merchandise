package de.mq.merchandise.util.chouchdb.support;

import java.io.IOException;
import java.util.List;

import junit.framework.Assert;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
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
	public final void singleKey() throws JsonGenerationException, JsonMappingException, IOException {
		final CouchDBTemplate couchDBTemplate = new CouchDBTemplate(restOperations, "petstore");
		final List<String> results = couchDBTemplate.forKey("qualityByArtist", "nicole", String.class);
		Assert.assertEquals(1, results.size());
	}
}
