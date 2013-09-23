package de.mq.merchandise.opportunity.support;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Map;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestOperations;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/documentRepository.xml" })
@Ignore
public class DocumentRepositoryIntegrationTest {

	private static final String URL = "http://localhost:5984/opportunities/4711";

	@Autowired
	private DocumentRepository documentRepository;

	@Autowired
	private RestOperations restTemplate;

	@After
	@Before
	public final void cleanUp() {
		try {
			restTemplate.delete(URL + "?rev=" + restTemplate.getForObject(URL, Map.class).get("_rev"));
		} catch (final HttpClientErrorException ex) {
			throwExceptionIfNot404(ex);
		}

	}

	private void throwExceptionIfNot404(final HttpClientErrorException ex) {
		if (ex.getStatusCode() != HttpStatus.NOT_FOUND) {
			throw ex;
		}
	}

	@Test
	public final void revision() {
		Assert.assertNotNull(documentRepository);

		final Opportunity opportunity = new OpportunityImpl();
		ReflectionTestUtils.setField(opportunity, "id", 4711L);
		final String rev = documentRepository.revisionFor(opportunity);
		Assert.assertNotNull(rev);
		for (int i = 0; i < 10; i++) {
			Assert.assertEquals(rev, documentRepository.revisionFor(opportunity));
		}

	}

	@Test
	public final void upload() throws FileNotFoundException {
		
		final InputStream inputStream = getClass().getResourceAsStream("/Kylie-Doll.jpg");
		final Opportunity opportunity = new OpportunityImpl();
		ReflectionTestUtils.setField(opportunity, "id", 4711L);
		documentRepository.assign(opportunity, "Kylie-Doll.jpg", inputStream, MediaType.IMAGE_JPEG);
		Assert.assertEquals(MediaType.IMAGE_JPEG, restTemplate.headForHeaders(URL + "/Kylie-Doll.jpg").getContentType());
	}

	@Test
	public final void delete() throws FileNotFoundException {
		upload();
		
		final Opportunity opportunity = new OpportunityImpl();
		ReflectionTestUtils.setField(opportunity, "id", 4711L);

		documentRepository.delete(opportunity, "Kylie-Doll.jpg");
		try {
			restTemplate.headForHeaders(URL + "/Kylie-Doll.jpg");
			Assert.fail(HttpClientErrorException.class.getName() + " expected.");
		} catch (final HttpClientErrorException ex) {
			Assert.assertEquals(HttpStatus.NOT_FOUND, ex.getStatusCode());
		}

	}

}
