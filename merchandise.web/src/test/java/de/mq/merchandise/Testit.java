package de.mq.merchandise;


import java.util.Arrays;
import java.util.List;



import org.junit.Assert;
import org.junit.Test;
import org.springframework.util.CollectionUtils;

public class Testit {
	
	@Test
	public  void sucks() {
		
		final double[] values = {1.0, 2.0, 3.0, 4.0};
		
		Assert.assertTrue(Double.class.isInstance(values[0]));
		System.out.println("sucks():"+ Arrays.asList(values).size());
		Arrays.asList(values).stream().forEach(value -> Assert.assertTrue(Double.class.isInstance(value)));
		
		
	}
	
	

	@Test
	public  void okWithSpringCollectionUtils() {
		
		final double[] values = {1.0, 2.0, 3.0, 4.0};
		@SuppressWarnings("unchecked")
		final List<Double> list = CollectionUtils.arrayToList(values);
		System.out.println("okWithSpringCollectionUtils():" + list.size());
		Assert.assertTrue(Double.class.isInstance(values[0]));
		
		list.forEach(value -> Assert.assertTrue(Double.class.isInstance(value)));
		
	}
	
	@Test
	public  void ok() {
		
		final Double[] values = {1.0, 2.0, 3.0, 4.0};
		
		Assert.assertTrue(Double.class.isInstance(values[0]));
		System.out.println("ok():" + Arrays.asList(values).size());
		Arrays.asList(values).stream().forEach(value -> Assert.assertTrue(Double.class.isInstance(value)));
		
	}

}
