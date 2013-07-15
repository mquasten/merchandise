package de.mq.merchandise.opportunity.support;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import junit.framework.Assert;

import org.junit.Test;
import org.springframework.util.ReflectionUtils;

public class ClassificationFileRepositoryMockTest {
	
	private final ClassificationRepository classificationRepository = new ClassificationFileRepositoryMock();
	
	@Test
	public final void testInit() {
		((ClassificationFileRepositoryMock)classificationRepository).loadCaches();
		
		Assert.assertEquals(109, classificationRepository.allActivityClassifications().size());
		Assert.assertEquals(81, classificationRepository.allProductClassifications().size());
	}
	
	@Test
	public final void activities() {
		final Map<String, int[]> ranges = new HashMap<>();
		ranges.put("A-A", new int[] {1,3});
		ranges.put("A-B", new int[] {5,9});
		ranges.put("A-C", new int[] {10,33});
		ranges.put("A-D", new int[] {35,35});
		ranges.put("A-E", new int[] {36,39});
		ranges.put("A-F", new int[] {41,43});
		ranges.put("A-G", new int[] {45,47});
		ranges.put("A-H", new int[] {49,53});
		ranges.put("A-I", new int[] {55,56});
		ranges.put("A-J", new int[] {58,63});
		ranges.put("A-K", new int[] {64,66});
		ranges.put("A-L", new int[] {68,68});
		ranges.put("A-M", new int[] {69,75});
		ranges.put("A-N", new int[] {77,82});
		ranges.put("A-O", new int[] {84,84});
		ranges.put("A-P", new int[] {85,85});
		ranges.put("A-Q", new int[] {86,88});
		ranges.put("A-R", new int[] {90,93});
		ranges.put("A-S", new int[] {94,96});
		ranges.put("A-T", new int[] {97,98});
		ranges.put("A-U", new int[] {99,99});
		
		
		((ClassificationFileRepositoryMock)classificationRepository).loadCaches();
		for(final Entry<Classification, Set<Classification>> entry : group(classificationRepository.allActivityClassifications()).entrySet()){
			
			Assert.assertTrue(entry.getKey().id().startsWith("A-"));
			Assert.assertTrue(entry.getKey().id().length()==3);
			final List<Classification> childList = new ArrayList<Classification>(entry.getValue());
			Collections.sort(childList, comparator("A-"));
			
			
			int range[]  = ranges.get(entry.getKey().id());
			Assert.assertNotNull(range);
			Assert.assertEquals(range[1] - range[0] +1 , childList.size());
			int lastNumber=-1;
			for(final Classification child : childList) {
				Assert.assertEquals(entry.getKey(), child.parent());
				int number = Integer.valueOf(child.id().replaceAll("A-", ""));
				Assert.assertTrue(number>=range[0] && number<=range[1]);
				if(lastNumber > 0   ){
					Assert.assertEquals(lastNumber, number-1); 
				}
				lastNumber=number;
			}
			
		}
	}

	private Comparator<Classification> comparator(final String replace) {
		return new Comparator<Classification>() {

			@Override
			public int compare(Classification c1, Classification c2) {
				
				return Integer.valueOf(c1.id().replaceAll(replace, "")) - Integer.valueOf(c2.id().replaceAll(replace, ""));
			}};
	}
	
	@Test
	public final void products() {
		final Map<String, int[]> ranges = new HashMap<>();
		ranges.put("P-0", new int[]{1,4});
		ranges.put("P-1", new int[]{11,18});
		ranges.put("P-2", new int[]{21,29});
		ranges.put("P-3", new int[]{31,39});
		ranges.put("P-4", new int[]{41,49});
		ranges.put("P-5", new int[]{53,54});
		ranges.put("P-6", new int[]{61,69});
		ranges.put("P-7", new int[]{71,73});
		ranges.put("P-8", new int[]{81,89});
		ranges.put("P-9", new int[]{91,99});
		
		((ClassificationFileRepositoryMock)classificationRepository).loadCaches();
		for(final Entry<Classification, Set<Classification>> entry : group(classificationRepository.allProductClassifications()).entrySet()){
		
			final List<Classification> childList = new ArrayList<Classification>(entry.getValue());
			Collections.sort(childList, comparator("P-"));
			int range[]  = ranges.get(entry.getKey().id());
			Assert.assertNotNull(range);
			
			Assert.assertEquals(range[1] - range[0] +1 , childList.size());
			int lastNumber=-1;
			for(final Classification child : childList) {
				Assert.assertEquals(entry.getKey(), child.parent());
				int number = Integer.valueOf(child.id().replaceAll("P-", ""));
				Assert.assertTrue(number>=range[0] && number<=range[1]);
				if(lastNumber > 0   ){
					Assert.assertEquals(lastNumber, number-1); 
				}
				Assert.assertEquals(Integer.valueOf(entry.getKey().id().replaceFirst("P-", "")).intValue(), (int) number/10  );
				lastNumber=number;
			}
		}
	}

	private Map<Classification, Set<Classification>> group(Collection<? extends Classification> classifications) {
		final Map<Classification, Set<Classification>> childs = new HashMap<>();
		for(final Classification classification : classifications){
			if(classification.parent()!=null){
				continue;
			}
			childs.put(classification, new HashSet<Classification>());
			
		}
		for(final Classification classification : classifications){
			
			if(classification.parent()==null){
				
				continue;
			}
		
			childs.get(classification.parent()).add(classification);
		}
		return childs;
	}
	
	@Test(expected=IllegalStateException.class)
	public final void resourceNotFound() throws IllegalArgumentException, IllegalAccessException {
		ClassificationFileRepositoryMock classificationRepository = new ClassificationFileRepositoryMock();
		final Field field = ReflectionUtils.findField(ClassificationFileRepositoryMock.class, "resource");
		field.setAccessible(true);
		field.set(classificationRepository, "dontLetMeGetMe.csv");
		classificationRepository.loadCaches();
		
	}

}
