package de.mq.merchandise.opportunity.support;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;
import org.springframework.util.ReflectionUtils;

import de.mq.merchandise.util.EntityUtil;

@Repository
@Profile("mock")
class ClassificationFileRepositoryMock implements ClassificationRepository {

	private   String resource = "classifications.csv";

	static final String DELIMITER = "[|]";

	static final String PRODUCT_KIND = "P";

	static final String ACTIVITY_KIND = "A";

	static final String PARENT = "parent";

	static final String KIND = "kind";

	static final String PARENT_ID = "parent_id";

	final Set<ActivityClassification> activityClassifications = new HashSet<>();

	final Set<ProductClassification> productClassifications = new HashSet<>();
	
	

	@PostConstruct
	void loadCaches() {
		/*
		 * java.io is shit, one of the most biggest shit ever ...
		 */
		
		try (final BufferedReader reader = new BufferedReader(new InputStreamReader(new DataInputStream(getClass().getClassLoader().getResourceAsStream(resource))))) {
			handleLines(reader);
		} catch (final Exception ex) {
			throw new IllegalStateException("Error reading resource: " + resource , ex);
		}

	}

	private void handleLines(final BufferedReader bufferedReader) throws IOException {
		String header[] = null;
		final Map<String, Classification> idMap = new HashMap<>();
		final Map<String, String> parentMap = new HashMap<>();
		while (bufferedReader.ready()) {
			final String cols[] = bufferedReader.readLine().replaceAll("\\\"", "").split(DELIMITER);
			if (header == null) {
				header = cols;
				continue;
			}
			idMap.put(map2Entity(parentMap, csv2Map(header, cols)).id(), map2Entity(parentMap, csv2Map(header, cols)));
		}
		setParent(activityClassifications, idMap, parentMap);
		setParent(productClassifications, idMap, parentMap);
	}

	private void setParent(final Collection<? extends Classification> classifications, final Map<String, Classification> idMap, final Map<String, String> parentMap) {
		for (final Classification entity : classifications) {
			/*if (!parentMap.containsKey(entity.id())) {
				continue;
			}*/
			setField(entity, PARENT, idMap.get(parentMap.get(entity.id())));
		}
	}

	private Classification map2Entity(final Map<String, String> parentMap, final Map<String, String> fieldMap) {
		final Classification entity = createEntity(fieldMap.get(KIND));
		if (fieldMap.containsKey(PARENT_ID))
			;
		parentMap.put(fieldMap.get("id"), fieldMap.get(PARENT_ID));

		fieldMap.remove(KIND);
		fieldMap.remove(PARENT_ID);
		for (final Entry<String, String> entry : fieldMap.entrySet()) {

			setField(entity, entry.getKey(), entry.getValue());
		}
		if (entity instanceof ActivityClassification) {
			activityClassifications.add((ActivityClassification) entity);
		} else {
			productClassifications.add((ProductClassification) entity);
		}

		return entity;
	}

	private void setField(final Classification entity, final String fieldName, final Object value) {
		final Field field = ReflectionUtils.findField(entity.getClass(), fieldName);
		field.setAccessible(true);
		ReflectionUtils.setField(field, entity, value);
	}

	private Map<String, String> csv2Map(String[] header, final String[] cols) {
		final Map<String, String> fields = new HashMap<>();
		for (int i = 0; i < cols.length; i++) {
			fields.put(header[i], cols[i]);
		}
		return fields;
	}

	private Classification createEntity(final String col) {

		if (col.equalsIgnoreCase(ACTIVITY_KIND)) {
			return EntityUtil.create(ActivityClassificationImpl.class);
		} 
	    return EntityUtil.create(ProductClassificationImpl.class);
		
	}

	@Override
	public Collection<ActivityClassification> allActivityClassifications() {
		return activityClassifications;
	}

	@Override
	public Collection<ProductClassification> allProductClassifications() {
		return productClassifications;
	}

}
