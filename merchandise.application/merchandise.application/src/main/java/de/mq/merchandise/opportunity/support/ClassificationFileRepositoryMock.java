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

	
	private final Set<ActivityClassification> activityClassifications = new HashSet<>();
	
	private final Set<ProcuctClassification> productClassifications = new HashSet<>();
	
	
	@PostConstruct
	void loadCaches(){
	    /* 
	     * java.io is shit, one of the most biggest shit ever ... 
	    */
		try (BufferedReader reader  = new BufferedReader(new InputStreamReader(new DataInputStream(getClass().getClassLoader().getResourceAsStream("classifications.csv"))))) {
		   handleLines(reader);
		} catch ( IOException ex){
			
		}
		
		
		
	}




	private void handleLines(final BufferedReader bufferedReader) throws IOException {
		String header[] = null;
		final Map<String,Classification> idMap = new HashMap<>();
		final Map<String,String> parentMap = new HashMap<>();
		while(bufferedReader.ready()){
			final String cols[] = bufferedReader.readLine().replaceAll("\\\"", "").split("[|]");
			if( header == null){
				header=cols;
				continue;
			}
			idMap.put(map2Entity(parentMap, csv2Map(header, cols)).id(), map2Entity(parentMap, csv2Map(header, cols))); 
		}
		setParent(activityClassifications, idMap, parentMap);
		setParent(productClassifications, idMap, parentMap);
	}




	private void setParent(final Collection<? extends Classification> classifications, final Map<String, Classification> idMap, final Map<String, String> parentMap) {
		for(final Classification entity : classifications){
			if( ! parentMap.containsKey(entity.id()) ) {
				continue;
			}
		    setField(entity, "parent", idMap.get(parentMap.get(entity.id())));
		}
	}




	private Classification map2Entity(final Map<String, String> parentMap, final Map<String, String> fieldMap) {
		final Classification entity = createEntity(fieldMap.get("kind"));
		if( fieldMap.containsKey("parent_id"));
		parentMap.put(fieldMap.get("id"), fieldMap.get("parent_id"));
		
		fieldMap.remove("kind");
		fieldMap.remove("parent_id");
		for(final Entry<String,String> entry : fieldMap.entrySet()) {
			
			setField(entity, entry.getKey(), entry.getValue());
		}
		if (entity instanceof ActivityClassification) {
			activityClassifications.add((ActivityClassification) entity)	;
		} else {
			productClassifications.add((ProcuctClassification) entity);
		}
		
		return entity;
	}




	private void setField(final Classification entity, final String fieldName , final Object value) {
		final Field field = ReflectionUtils.findField(entity.getClass(), fieldName );
		field.setAccessible(true);
		ReflectionUtils.setField(field, entity, value);
	}




	private Map<String, String> csv2Map(String[] header, final String[] cols) {
		final Map<String,String> fields = new HashMap<>();
		for(int i=0; i < cols.length; i++){
			fields.put(header[i], cols[i]);
		}
		return fields;
	}




	private Classification createEntity(final String col) {
		
		if( col.equalsIgnoreCase("A")){
			return EntityUtil.create(ActivityClassificationImpl.class);
		} else if (col.equalsIgnoreCase("P")){
			return EntityUtil.create(ProductClassificationImpl.class);
		}
		throw new IllegalArgumentException("Wrong Kind of classification: " + col);
	}
	
	
	
	
	@Override
	public Collection<ActivityClassification> allActivityClassifications() {
		return activityClassifications;
	}

	@Override
	public Collection<ProcuctClassification> allProductClassifications() {
		return productClassifications;
	}
	
	
	

}
